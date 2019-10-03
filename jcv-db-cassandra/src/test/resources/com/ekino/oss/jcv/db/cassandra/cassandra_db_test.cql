CREATE KEYSPACE cassandratest WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE cassandratest;

CREATE TYPE cassandratest.type_test (
    test_field_int int,
    test_field_text text
);

CREATE TYPE cassandratest.complex_type (
    test_field_int int,
    test_type list<frozen<type_test>>
);

CREATE TABLE cassandratest.cassandra_table_type (
                        id uuid PRIMARY KEY,
                        ascii_field ascii,
                        bigint_field bigint,
                        boolean_field boolean,
                        date_field date,
                        decimal_field decimal,
                        double_field double,
                        duration_field duration,
                        float_field float,
                        inet_field inet,
                        int_field int,
                        smallint_field smallint,
                        text_field text,
                        time_field time,
                        timestamp_field timestamp,
                        tinyint_field tinyint,
                        varchar_field varchar,
                        varint_field varint,
                        map_field map<text, int>,
                        set_field set<int>,
                        list_field list<int>,
                        custom_type_field type_test,
                        list_udt_field list<frozen<type_test>>,
                        tuple_field tuple<text, int>,
                        complex_type list<frozen<complex_type>>,
                        column_null boolean
);

INSERT INTO cassandratest.cassandra_table_type (
                                                       id,
                                                       ascii_field,
                                                       bigint_field,
                                                       boolean_field,
                                                       date_field,
                                                       decimal_field,
                                                       double_field,
                                                       duration_field,
                                                       float_field,
                                                       inet_field,
                                                       int_field,
                                                       smallint_field,
                                                       text_field,
                                                       time_field,
                                                       timestamp_field,
                                                       tinyint_field,
                                                       varchar_field,
                                                       varint_field,
                                                       map_field,
                                                       set_field,
                                                       list_field,
                                                        custom_type_field,
                                                        list_udt_field,
                                                       tuple_field,
                                                       complex_type,
                                                        column_null
                                                      ) VALUES (
                                                                28f4dcde-a221-4133-8d72-4115c4d24038,
                                                                'A',
                                                                9223372036854775806,
                                                                true,
                                                                '9999-12-31',
                                                                12345.67898,
                                                                12345.67898,
                                                                P0000-00-00T89:09:09,
                                                                12345.67898,
                                                                '127.0.0.1',
                                                                2147483646,
                                                                32766,
                                                                'text field',
                                                                '00:11:12',
                                                                1556701239,
                                                                100,
                                                                'varchar field',
                                                                1,
                                                                   { 'fruit' : 1, 'legume': 2 },
                                                                   { 1 , 2, 3, 4 },
                                                                [ 1, 2, 3, 4 ],
                                                                   { test_field_int : 1, test_field_text: 'test'},
                                                                   [{ test_field_int: 1, test_field_text: 'test'}],
                                                                ('fruit', 1),
                                                                [{ test_field_int : 1, test_type: [{ test_field_int : 1, test_field_text: 'test'}]}],
                                                                null
                                                               );
