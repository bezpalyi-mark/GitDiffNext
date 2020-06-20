USE diff_reviewer;

INSERT INTO usr (password, username, active)
VALUES ('5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8', 'AlexKushch', 1),
       ('4813494D137E1631BBA301D5ACAB6E7BB7AA74CE1185D456565EF51D737677B2', 'rooted', 1),
       ('8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918', 'admin9', 1);

INSERT INTO user_role(user_id, role) VALUES (3, 'ADMIN'),
                                            (2, 'USER'),
                                            (1, 'USER');

INSERT INTO list_task(id, level_task, name, previous_id)
VALUES (1, 1, 'Day1', NULL), (2, 2, 'Day2', 1), (3, 3, 'Day3', 2);

INSERT INTO task (id, is_done, task_from_list_id, user_id) VALUES (1, 0, 3, 1),
                                                                        (2, 0, 2, 2),
                                                                        (3, 0, 1, 3),
                                                                        (4, 1, 1, 2);

INSERT INTO request(description_pr, diff_url, status, title_pr, creator_pr_id, task_id, approve_count)
VALUES ('', 'https://try.gitea.io/rooted/rootRepa/pulls/1', 'NOT_MERGED', 'RootPR', 2, 1, 0),
       ('Something', 'https://try.gitea.io/AlexKushch/test/pulls/2', 'NOT_MERGED', 'Added file.txt', 1, 2, 0),
       ('', 'https://try.gitea.io/admin9/AdminRepa/pulls/1', 'NOT_MERGED', 'second', 3, 3, 0);

INSERT INTO comment(text, user_id, request_id)
VALUES ('HELP ME MOM!', 1, 1),
       ('I DO NOT WANNA DIE!', 1, 1),
       ('MISSION COMPLETED.', 2, 2);

INSERT INTO request_reviewer(request_id, user_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (3, 2);