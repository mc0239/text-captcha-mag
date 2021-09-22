ALTER TABLE captcha_flow
RENAME TO captcha_link;

ALTER TABLE captcha_task_response
RENAME COLUMN captcha_flow_id TO captcha_link_id;