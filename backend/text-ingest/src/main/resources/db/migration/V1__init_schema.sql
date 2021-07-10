CREATE SEQUENCE captcha_task_seq START 1 INCREMENT 1;
CREATE TABLE captcha_task (
    task_type         varchar(31) NOT NULL,
    id                int8 NOT NULL,
    article_text_hash varchar(512),
    article_url_hash  varchar(512),
    task_content      text,
    created_at        timestamp,
    CONSTRAINT captcha_task_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE ingested_article_seq START 1 INCREMENT 1;
CREATE TABLE ingested_article (
    id                int8 NOT NULL,
    article_text      text,
    article_text_hash varchar(512),
    article_url       text,
    article_url_hash  varchar(512),
    created_at        timestamp,
    CONSTRAINT ingested_article_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE captcha_task_response_seq START 1 INCREMENT 1;
CREATE TABLE captcha_task_response (
    task_type        varchar(31) NOT NULL,
    id               int8 NOT NULL,
    captcha_task_id  int8 NULL,
    response_content text NULL,
    created_at       timestamp NULL,
    CONSTRAINT captcha_task_response_pkey PRIMARY KEY (id),
    CONSTRAINT fk_captcha_task_id FOREIGN KEY (captcha_task_id) REFERENCES captcha_task(id)
);