create sequence captcha_task_seq start 1 increment 1;
create table captcha_task
(
    id          int8 not null,
    article_uid varchar(255),
    tokens      TEXT,
    primary key (id)
);

create sequence captcha_response_seq start 1 increment 1;
create table captcha_response
(
    id                      int8 not null,
    marked_token_index_list TEXT,
    captcha_task_id         int8,
    primary key (id)
);

alter table captcha_response
add constraint fk_captcha_task_id foreign key (captcha_task_id) references captcha_task;
