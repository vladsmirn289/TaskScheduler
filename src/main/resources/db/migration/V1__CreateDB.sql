SET client_encoding = 'UTF8';

CREATE TABLE client (
    id bigint NOT NULL,
    login character varying(255),
    password character varying(255)
);

CREATE TABLE client_roles (
    client_id bigint NOT NULL,
    roles character varying(255)
);

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE repeatable_task (
    id bigint NOT NULL,
    description character varying(255),
    end_date date,
    name character varying(255),
    period_mode integer,
    priority integer,
    start_date date,
    client_id bigint
);

CREATE TABLE task (
    id bigint NOT NULL,
    date date,
    description character varying(255),
    name character varying(255),
    priority integer,
    progress integer NOT NULL,
    client_id bigint,
    repeatable_task_id bigint
);

ALTER TABLE client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);

ALTER TABLE repeatable_task
    ADD CONSTRAINT repeatable_task_pkey PRIMARY KEY (id);

ALTER TABLE task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);


ALTER TABLE repeatable_task
    ADD CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES client(id);

ALTER TABLE client_roles
    ADD CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES client(id);

ALTER TABLE task
    ADD CONSTRAINT fk_repeatable_task_id FOREIGN KEY (repeatable_task_id) REFERENCES repeatable_task(id);

ALTER TABLE task
    ADD CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES client(id);