ALTER TABLE captcha_task_response
RENAME COLUMN is_sanity TO is_verify;

ALTER TABLE captcha_flow
RENAME COLUMN complete_sanity TO complete_verify;

ALTER TABLE captcha_task
ADD COLUMN confidence real;