--CREATE SEQUENCE project_id_seq;
--
--CREATE TABLE project
--(
--    id bigserial NOT NULL DEFAULT nextval('project_id_seq'),
--    dtype character varying(31) NOT NULL,
--    name varchar(255),
--    description varchar(255)
--    CONSTRAINT project_pkey PRIMARY KEY (id)
--);
--
--ALTER SEQUENCE project_id_seq OWNED BY project.id;