CREATE SEQUENCE public.project_id_seq

CREATE TABLE public.project
(
  id bigint NOT NULL DEFAULT nextval('project_id_seq'::regclass),
  description character varying(255),
  directory character varying(255),
  name character varying(255),
  title character varying(255),
  CONSTRAINT project_pkey PRIMARY KEY (id)
)