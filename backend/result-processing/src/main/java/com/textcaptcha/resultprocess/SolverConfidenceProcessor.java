package com.textcaptcha.resultprocess;

import com.textcaptcha.data.repository.CaptchaTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SolverConfidenceProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CaptchaTaskRepository repository;

    public SolverConfidenceProcessor(CaptchaTaskRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void logTaskCount() {
        logger.error("There are " + repository.count() + " tasks in database.");
    }

}
