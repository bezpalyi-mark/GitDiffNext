INSERT INTO list_task(id, level_task, name, previous_id)
VALUES (1, 1, 'Day1', NULL), (2, 2, 'Day2', 1), (3, 3, 'Day3', 2);

INSERT INTO task (id, is_done, name, task_from_list_id, user_id) VALUES (1, 0, 'Second', 3, 1),
                                                                        (2, 0, 'First', 2, 2),
                                                                        (3, 0, 'Third', 1, 3),
                                                                        (4, 1, 'First', 1, 2);

INSERT INTO request(description_pr, diff_url, status, title_pr, creator_pr_id, task_id)
VALUES ('Help me, the One', 'google.com', 'NOT_MERGED', 'The One', 1, 1),
       ('Delete student', 'khpi.com', 'NOT_MERGED', 'Example', 2, 2),
       ('Requst 3', 'example.com', 'NOT_MERGED', 'Example', 3, 3);

INSERT INTO comment(text, user_id, request_id)
VALUES ('HELP ME MOM!', 1, 1),
       ('I DON\'T WANNA DIE!', 1, 1),
       ('MISSION COMPLETED.', 2, 2);

INSERT INTO request_reviewer(request_id, user_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (3, 2);