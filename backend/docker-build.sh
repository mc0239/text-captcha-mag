docker build --tag text-captcha-task-manager -f dockerfiles/TaskManager.WithBuild.Dockerfile .
docker build --tag text-captcha-text-ingest -f dockerfiles/TextIngest.WithBuild.Dockerfile .
docker build --tag text-captcha-result-processing -f dockerfiles/ResultProcessing.WithBuild.Dockerfile .