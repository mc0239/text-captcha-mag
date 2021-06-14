create sequence article_seq start 1 increment 1;
create table article
(
    id                bigint not null,
    article_url       text,
    article_text      text,
    article_url_hash  varchar(510),
    article_text_hash varchar(510),
    created_at        timestamp,
    primary key (id)
);

alter table captcha_task
rename column article_uid to article_url_hash;

alter table captcha_task
add column article_text_hash varchar(510);

alter table captcha_task
alter column id set data type bigint;

alter table captcha_response
alter column id set data type bigint;

alter table captcha_response
alter column captcha_task_id set data type bigint;