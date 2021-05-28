# text-captcha-mag

## Requirements

- `docker` & `docker-compose`

## Running

Docker compose file expects an existence of:
- `rsdo-ds3-ner-api:v1` image (see [SloNER repository](https://github.com/RSDO-DS3/SloNER),
- `text-captcha-task-manager` image (see `task-manager` directory),
- specified volume paths on host to exist (fix as appropriate).