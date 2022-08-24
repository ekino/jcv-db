CREATE TABLE mssql_type_table (
                                  id                  UNIQUEIDENTIFIER primary key,
                                  column_bigint       bigint,
                                  column_int          int,
                                  column_smallint     smallint,
                                  column_tinyint      tinyint,
                                  column_decimal      decimal(10,5),
                                  column_numeric      numeric(10,5),
                                  column_money        money,
                                  column_smallmoney   smallmoney,
                                  column_float        float,
                                  column_real         real,
                                  column_date         date,
                                  column_datetime2    datetime2,
                                  column_datetime     datetime,
                                  column_datetimeoffset   datetimeoffset,
                                  column_smalldatetime    smalldatetime,
                                  column_time             time,
                                  column_char             char,
                                  column_text             text,
                                  column_varchar          varchar(255),
                                  column_point                geometry,
                                  column_linestring           geometry,
                                  column_polygon              geometry,
                                  column_multipoint           geometry,
                                  column_multilinestring      geometry,
                                  column_multipolygon         geometry,
                                  column_null                 int
);

INSERT INTO mssql_type_table(
    id,
    column_bigint,
    column_int,
    column_smallint,
    column_tinyint,
    column_decimal,
    column_numeric,
    column_money,
    column_smallmoney,
    column_float,
    column_real,
    column_date,
    column_datetime2,
    column_datetime,
    column_datetimeoffset,
    column_smalldatetime,
    column_time,
    column_char,
    column_text,
    column_varchar,
    column_point,
    column_linestring,
    column_polygon,
    column_multipoint,
    column_multilinestring,
    column_multipolygon,
    column_null
)
VALUES (
           'AAF7393D-3EB1-4D4A-B022-208D582B3B3F',
           9223372036854775806,
           2147483646,
           32766,
           254,
           12345.67898,
           12345.67898,
           922337203685456.5808,
           214736.3648,
           98472983.329043,
           4.079324,
           '9999-12-31',
           '9999-12-31 23:59:59',
           '9999-12-31 23:59:59',
           '9999-12-31 23:59:59',
           '2079-06-06',
           '00:11:12',
           'c',
           'text',
           'varchar',
           geometry::STGeomFromText('POINT (3 4 7)', 0),
           geometry::STGeomFromText('LINESTRING(1 1, 2 4, 3 9)', 0),
           geometry::STPolyFromText('POLYGON((0 0, 0 3, 3 3, 3 0, 0 0), (1 1, 1 2, 2 1, 1 1))',10),
           geometry::STGeomFromText('MULTIPOINT((2 3), (7 8 9.5))', 23),
           geometry::Parse('MULTILINESTRING((0 2, 1 1), (1 0, 1 1))'),
           geometry::Parse('MULTIPOLYGON(((0 0, 0 3, 3 3, 3 0, 0 0), (1 1, 1 2, 2 1, 1 1)), ((9 9, 9 10, 10 9, 9 9)))'),
           null
       );

CREATE TABLE table_test (
                            id                          UNIQUEIDENTIFIER primary key,
                            criteria_number             INT,
                            content                     VARCHAR(255)
);

INSERT INTO table_test (id, criteria_number, content) VALUES ('a849d2f7-7814-4d37-989e-a7d6ee4b5e05', 0, 'content 1');
INSERT INTO table_test (id, criteria_number, content) VALUES ('f9b5b79d-606e-4251-8967-12dbc5611c70', 1, 'content 2');
INSERT INTO table_test (id, criteria_number, content) VALUES ('63a36810-c582-42ec-9ab6-322f15f0c3b9', 2, 'content 3');
INSERT INTO table_test (id, criteria_number, content) VALUES ('642a00a5-18f2-47cf-9e5c-0161156ff0a0', 3, 'content 4');
