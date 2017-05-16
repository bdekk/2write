CREATE SEQUENCE public.user_id_seq

CREATE TABLE public.user
(
  id bigint NOT NULL DEFAULT nextval('user_id_seq'::regclass),
  firstname character varying(255),
  lastname character varying(255),
  CONSTRAINT user_pkey PRIMARY KEY (id)
)