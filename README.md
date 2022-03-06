# text-captcha-mag

❗ Moved to https://github.com/mcebular/text-captcha-mag ❗

## Requirements

- `docker` & `docker-compose` for the backend.
- a Chromium-based browser for the frontend.

## Running

In a best-case scenario, all parts of the backend should succesfuly start by
only running `docker-compose up` in the root directory of this repository.

Docker compose file expects an existence of the following images:
- `rsdo-ds3-ner-api:v1`: see [SloNER repository](https://github.com/RSDO-DS3/SloNER),
- `slo-coref`: see [Slovene coreference resolution repository](https://github.com/mc0239/slovene-coreference-resolution/tree/eval_api)
- `text-captcha-classla`: see `process/classla` directory,
- `text-captcha-text-ingest`: see `backend` directory,
- `text-captcha-task-manager`: see `backend` directory,
- `text-captcha-result-processing`: see `backend` directory,
- `text-captcha-integration-demo`: see `frontend/integration-demo` directory.

Other images, specified in Docker compose file, should be automatically pulled
from Docker Hub.

Checkout `docker-compose.yml` file before trying to `docker-compose up` it, as
it has some environment-specific stuff to set up
(notice the sections between `# <!!!` and `# !!!>`)
