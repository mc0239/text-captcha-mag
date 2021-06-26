create sequence ingested_article_seq start 1 increment 1;
create table ingested_article
(
    id                bigint not null,
    article_url       text,
    article_text      text,
    article_url_hash  varchar(510),
    article_text_hash varchar(510),
    created_at        timestamp,
    primary key (id)
);

create sequence ner_captcha_task_seq start 1 increment 1;
create table ner_captcha_task
(
    id                bigint not null,
    article_url_hash  varchar(510),
    article_text_hash varchar(510),
    created_at        timestamp,
    tokens            text,
    primary key (id)
);


create sequence ner_captcha_task_response_seq start 1 increment 1;
create table ner_captcha_task_response
(
    id              bigint not null,
    captcha_task_id bigint,
    created_at      timestamp,
    marked_tokens   text,
    primary key (id)
);

alter table ner_captcha_task_response
add constraint fk_ner_captcha_task_id foreign key (captcha_task_id) references ner_captcha_task;

-- TODO add ner_captcha_task_response table