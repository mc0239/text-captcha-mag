package com.textcaptcha.taskmanager.service;

import com.textcaptcha.taskmanager.pojo.SolutionProcessorResult;

import java.util.List;
import java.util.UUID;

public interface TaskSolutionProcessor {

    SolutionProcessorResult processSolution(UUID taskInstanceId, List<Integer> taskSolution);

}
