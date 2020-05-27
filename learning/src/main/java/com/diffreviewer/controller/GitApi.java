package com.diffreviewer.controller;

import com.diffreviewer.entities.MergeRequest;
import com.diffreviewer.entities.Status;
import com.diffreviewer.entities.User;
import com.diffreviewer.repos.UserRepo;
import io.gitea.ApiClient;
import io.gitea.ApiException;
import io.gitea.Configuration;
import io.gitea.api.IssueApi;
import io.gitea.api.RepositoryApi;
import io.gitea.auth.ApiKeyAuth;
import io.gitea.model.Comment;
import io.gitea.model.PullRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GitApi {

    /// Input url which enter user.
    private String url;  //https://gitea.novalab.live/novalab-pool/diff-reviewer/pulls/3 "https://try.gitea.io/AlexKushch/test/pulls/2"
    private final MergeRequest mergeRequest = new MergeRequest();

    public GitApi() {
    }

    public GitApi(String url) {
        this.url = url;
    }

    public MergeRequest GetPR(User user) {
        /// Set configuration and access token.
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://try.gitea.io/api/v1");

        ApiKeyAuth accessToken = (ApiKeyAuth) defaultClient.getAuthentication("AccessToken");
        accessToken.setApiKey("1c507ac020604c7a30315569fd6ccde908a64a25");  // 4399ec28ea4be7de3d5bfbc22c0e8c9da1058c62

        /// Divide url by '/' and get owner, repo and index from url.
        String [] split = url.split("/");

        String owner = split[3]; // Owner in url always the third.
        String repo = split[4]; // Repo goes after owner
        Long index = Long.parseLong(split[split.length-1]); // Index always the last in url.

        /// Get PR info.
        RepositoryApi repositoryApi = new RepositoryApi();
        PullRequest result = new PullRequest();
        try {
            result = repositoryApi.repoGetPullRequest(owner, repo, index);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        mergeRequest.setTitlePR(result.getTitle());  // Get title.
        mergeRequest.setDescriptionPR(result.getBody()); // Get description.
        if(!user.getUsername().equals(result.getUser().getLogin())) {
            System.out.println("Wrong user of MR");
            return null;
        }
        mergeRequest.setCreatorPR(user);  // Get creator.

        /// Class 'PullRequest' has two field. State(open, close), Merged(merged, not merged).
        String state = result.getState();
        if(state.equals("open")){       // First of all check by State. If is 'open' then
            if(result.getMergedBy() != null){   // Check by merged.
                mergeRequest.setStatusPR(Status.MERGED);
            } else {
                mergeRequest.setStatusPR(Status.NOT_MERGED);
            }
        } else {
            mergeRequest.setStatusPR(Status.CLOSED);
        }

        /// All comments are located in list of all comments. Here issues comments and PR comments.
        /// But has a different, comments from PR have a link, issue hasn't.
        List<Comment> comments = new ArrayList<>();
        IssueApi issueApi = new IssueApi();
        try {
            comments = issueApi.issueGetRepoComments(owner, repo, "");
            for (Comment comment : comments) {
                if (comment.getPullRequestUrl().equals(url)) {
//                    mergeRequest.addRequestComment(new RequestComment(user, comment.getBody()));
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }

        mergeRequest.setDiffURL(result.getDiffUrl());

        return mergeRequest;
    }
}
