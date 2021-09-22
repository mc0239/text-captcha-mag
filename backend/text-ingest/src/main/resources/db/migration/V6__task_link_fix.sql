ALTER SEQUENCE captcha_flow_seq RENAME TO captcha_link_seq;

ALTER TABLE captcha_task_response
RENAME CONSTRAINT fk_captcha_flow_id TO fk_captcha_link_id;