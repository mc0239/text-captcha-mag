package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.service.TaskInstanceKeeper;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CorefTaskInstanceKeeper implements TaskInstanceKeeper<CorefCaptchaTask> {

    @Loggable
    Logger logger;

    private static final long INSTANCE_EXPIRATION = 1000 * 60 * 2;

    private final Map<UUID, IssuedTaskInstance<CorefCaptchaTask>> issuedTasks;

    public CorefTaskInstanceKeeper() {
        this.issuedTasks = new ConcurrentHashMap<>();
    }

    @Override
    public UUID issue(CorefCaptchaTask task) {
        UUID taskInstanceId = UUID.randomUUID();
        issuedTasks.put(taskInstanceId, new IssuedTaskInstance<>(taskInstanceId, task));
        return taskInstanceId;
    }

    @Override
    public IssuedTaskInstance<CorefCaptchaTask> invalidate(UUID instanceId) {
        IssuedTaskInstance<CorefCaptchaTask> i = issuedTasks.get(instanceId);
        if (i != null && !i.isExpired(INSTANCE_EXPIRATION)) {
            issuedTasks.remove(instanceId);
            return i;
        }
        return null;
    }

    @Scheduled(fixedDelay = INSTANCE_EXPIRATION * 4)
    private void cleanupExpiredInstances() {
        int countRemoved = 0;
        for (Iterator<Map.Entry<UUID, IssuedTaskInstance<CorefCaptchaTask>>> i = issuedTasks.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<UUID, IssuedTaskInstance<CorefCaptchaTask>> entry = i.next();
            if (entry.getValue().isExpired(INSTANCE_EXPIRATION)) {
                i.remove();
                countRemoved++;
            }
        }

        if (countRemoved > 0) {
            logger.trace("Cleaned up " + countRemoved + " expired task instances.");
        }
    }


}
