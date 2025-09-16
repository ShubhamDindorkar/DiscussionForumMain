
INSERT INTO topic (id, title) VALUES ((NEXT VALUE FOR topic_seq), 'Test1');

INSERT INTO message (id, create_time, content, topic_id) VALUES ((NEXT VALUE FOR message_seq), CURRENT_TIMESTAMP, 'CONTENT1', (SELECT id FROM topic));
INSERT INTO message (id, create_time, content, topic_id) VALUES ((NEXT VALUE FOR message_seq), CURRENT_TIMESTAMP, 'CONTENT2', (SELECT id FROM topic));
INSERT INTO message (id, create_time, content, topic_id) VALUES ((NEXT VALUE FOR message_seq), CURRENT_TIMESTAMP, 'CONTENT3', (SELECT id FROM topic));
