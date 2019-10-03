CREATE TABLE IF NOT EXISTS table_test_second (
                                                 id                          UUID NOT NULL CONSTRAINT pk_table_test_second PRIMARY KEY,
                                                 criteria_number             INT,
                                                 content                     VARCHAR(255)
);

INSERT INTO table_test_second (id, criteria_number, content) VALUES ('07621b34-35dc-4b8f-94f4-b0f7e98b4088', 0, 'content 1');