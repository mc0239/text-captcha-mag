alter table captcha_task
add column created_at timestamp;

alter table captcha_response
add column created_at timestamp;