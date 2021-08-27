CREATE SEQUENCE captcha_flow_seq START 1 INCREMENT 1;
CREATE TABLE captcha_flow (
    id               int8 NOT NULL,
    uuid             uuid NOT NULL,
    complete_sanity  bool,
    complete_trusted bool,
    created_at       timestamp,
    modified_at      timestamp,
    CONSTRAINT captcha_flow_pkey PRIMARY KEY (id)
);

ALTER TABLE captcha_task_response
ADD COLUMN captcha_flow_id int8 NULL,
ADD CONSTRAINT fk_captcha_flow_id FOREIGN KEY (captcha_flow_id) REFERENCES captcha_flow(id);