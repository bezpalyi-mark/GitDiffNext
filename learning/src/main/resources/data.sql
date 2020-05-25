USE diff_reviewer;
INSERT INTO usr (password, username, active) VALUES ('password', 'krhv', 0), ('root', 'rooted', 0), ('admin', 'admin', 0);

--INSERT INTO user_role(user_id, role) VALUES (2, 'rhotm'), (3, 'writer'), (4, 'viewer');

--INSERT INTO task(name, previous_id)
--VALUES ('Day1', NULL), ('Day2', 1), ('Day3', 2);
--
--INSERT INTO request(description_pr, diff_url, status, title_pr, creator_pr_id)
--VALUES ('Help me, the One', 'google.com', 'WIP', 'The One', 1),
--       ('Delete student', 'khpi.com', 'Completed', 'Example', 2);
--
--INSERT INTO request_reviewer(request_id, user_id)
--VALUES (1, 2),
--       (2, 1);
--
--INSERT INTO comment(text, user_id, request_id)
--VALUES ('HELP ME MOM!', 1, 1),
--       ('I DON\'T WANNA DIE!', 1, 1),
--       ('MISSION COMPLETED.', 2, 1);