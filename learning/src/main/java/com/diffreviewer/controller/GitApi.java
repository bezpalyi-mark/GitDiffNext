package com.diffreviewer.controller;

import com.diffreviewer.entities.RequestComment;
import com.diffreviewer.entities.Status;
import com.diffreviewer.entities.User;
import com.diffreviewer.entities.request.SaveMergeRequest;
import io.gitea.ApiClient;
import io.gitea.ApiException;
import io.gitea.Configuration;
import io.gitea.api.IssueApi;
import io.gitea.api.RepositoryApi;
import io.gitea.auth.ApiKeyAuth;
import io.gitea.model.Comment;
import io.gitea.model.PullRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GitApi {

    public static final Logger LOGGER = LoggerFactory.getLogger(GitApi.class);

    /// Input url which enter user.
    private String url;  //https://gitea.novalab.live/novalab-pool/diff-reviewer/pulls/3 "https://try.gitea.io/AlexKushch/test/pulls/2"
    private final SaveMergeRequest request = new SaveMergeRequest();

    public GitApi() {
    }

    public GitApi(String url) {
        this.url = url;
    }

    public SaveMergeRequest getPR(User user) {
        /// Set configuration and access token.
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://try.gitea.io/api/v1");

        ApiKeyAuth accessToken = (ApiKeyAuth) defaultClient.getAuthentication("AccessToken");
        accessToken.setApiKey("1c507ac020604c7a30315569fd6ccde908a64a25");  // 4399ec28ea4be7de3d5bfbc22c0e8c9da1058c62

        /// Divide url by '/' and get owner, repo and index from url.
        String[] split = url.split("/");

        String owner = split[3]; // Owner in url always the third.
        String repo = split[4]; // Repo goes after owner
        Long index = Long.parseLong(split[split.length - 1]); // Index always the last in url.

        /// Get PR info.
        RepositoryApi repositoryApi = new RepositoryApi();
        PullRequest result = new PullRequest();
        try {
            result = repositoryApi.repoGetPullRequest(owner, repo, index);
        } catch (ApiException e) {
            LOGGER.error(e.getMessage());
        }

        request.setTitlePRName(result.getTitle());  // Get title.
        request.setDescriptionPR(result.getBody()); // Get description.
        if (!user.getUsername().equals(result.getUser().getLogin())) {
            LOGGER.error("Wrong user of MR");
            return null;
        }
        request.setCreatorPRName(user.getUsername());  // Get creator.

        /// Class 'PullRequest' has two field. State(open, close), Merged(merged, not merged).
        String state = result.getState();
        if (state.equals("open")) {       // First of all check by State. If is 'open' then
            if (result.getMergedBy() != null) {   // Check by merged.
                request.setStatusPRName(Status.MERGED.name());
            } else {
                request.setStatusPRName(Status.NOT_MERGED.name());
            }
        } else {
            request.setStatusPRName(Status.CLOSED.name());
        }

        /// All comments are located in list of all comments. Here issues comments and PR comments.
        /// But has a different, comments from PR have a link, issue hasn't.
        List<Comment> comments = new ArrayList<>();
        IssueApi issueApi = new IssueApi();
        try {
            comments = issueApi.issueGetRepoComments(owner, repo, "");
            for (Comment comment : comments) {
                if (comment.getPullRequestUrl().equals(url)) {
                    request.addRequestComment(new RequestComment(user, comment.getBody()));
                }
            }
        } catch (ApiException e) {
            LOGGER.error(e.getMessage());
        }

        request.setDiffURLName(result.getDiffUrl());

        return request;
    }
}
