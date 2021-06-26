docker build --tag text-captcha-task-manager -f dockerfiles/TaskManager.WithBuild.Dockerfile .
docker build --tag text-captcha-text-ingest -f dockerfiles/TextIngest.WithBuild.Dockerfile .