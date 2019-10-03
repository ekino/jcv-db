CREATE TABLE IF NOT EXISTS postgres_type_table (
    id                          UUID NOT NULL CONSTRAINT pk_numeric_type_table PRIMARY KEY,
    column_boolean              BOOLEAN,
    column_char_varying         CHARACTER VARYING(200),
    column_varchar              VARCHAR(150),
    column_character            CHARACTER(200),
    column_char                 CHAR(200),
    column_text                 TEXT,
    column_unlimited_varchar    VARCHAR,
    column_smallint             SMALLINT,
    column_int                  INT,
    column_serial               SERIAL,
    column_float                FLOAT,
    column_real                 REAL,
    column_numeric              NUMERIC,
    column_date                 date,
    column_time                 TIME,
    column_timestamp            TIMESTAMP,
    column_timestamptz          TIMESTAMPTZ,
    column_interval             INTERVAL,
    column_json                 json,
    column_jsonb                jsonb,
    column_point                point,
    column_lseg                 lseg,
    column_box                  box,
    column_path                 path,
    column_polygon              polygon,
    column_circle               circle,
    column_line                 line,
    column_null                 boolean
);

INSERT INTO postgres_type_table (
                                 id,
                                 column_boolean,
                                 column_char_varying,
                                 column_varchar,
                                 column_character,
                                 column_char,
                                 column_text,
                                 column_unlimited_varchar,
                                 column_smallint,
                                 column_int,
                                 column_serial,
                                 column_float,
                                 column_real,
                                 column_numeric,
                                 column_date,
                                 column_time,
                                 column_timestamp,
                                 column_timestamptz,
                                 column_interval,
                                 column_json,
                                 column_jsonb,
                                 column_point,
                                 column_lseg,
                                 column_box,
                                 column_path,
                                 column_polygon,
                                 column_circle,
                                 column_line,
                                 column_null
                                 )
VALUES (
        '0840ad8f-db82-472d-9dff-bb6d64992222',
        true,
        'char varying',
        'varchar',
        'character',
        'char',
        'text',
        'unlimited varcharZOEFOEZUFHHZELF',
        0123,
        12355,
        3282,
        123.2235,
        125125.13,
        312,
        DATE '2019-06-07',
        TIME '17:12:28.5',
        TIMESTAMP '2019-06-07',
        TIMESTAMPTZ '2019-06-07',
        INTERVAL '100 days',
        '[{"key": "Hello World !"}]',
        '{"key": "Hello World !"}',
        point(1,2),
        lseg(point(1,2), point(2,3)),
        box(point(1,2), point(2,3)),
        path(polygon(box(point(1,2), point(2,3)))),
        polygon(box(point(1,2), point(2,3))),
        circle(point(1,2), 3),
        line(point(1.2, 123.1), point(-5, -123)),
        null
       );

CREATE TABLE IF NOT EXISTS table_test (
                                          id                          UUID NOT NULL CONSTRAINT pk_table_test PRIMARY KEY,
                                          criteria_number             INT,
                                          content                     VARCHAR(255)
);

INSERT INTO table_test (id, criteria_number, content) VALUES ('07621b34-35dc-4b8f-94f4-b0f7e98b4088', 0, 'content 1');
INSERT INTO table_test (id, criteria_number, content) VALUES ('24640375-108f-4b29-b21c-3092fc02c83e', 1, 'content 2');
INSERT INTO table_test (id, criteria_number, content) VALUES ('d032dddb-593e-4ce6-a85c-4c2cf07dfcc5', 2, 'content 3');
INSERT INTO table_test (id, criteria_number, content) VALUES ('048802f8-4f3e-4585-95ab-f88de3e6c78b', 3, 'content 4');