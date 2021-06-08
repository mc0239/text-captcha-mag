alter table captcha_task
drop column article_uid,
add column article_url text,
add column article_uid varchar(510);