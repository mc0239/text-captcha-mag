ALTER TABLE captcha_task
RENAME COLUMN confidence TO model_confidence;

ALTER TABLE captcha_task
ADD COLUMN solver_confidence real;