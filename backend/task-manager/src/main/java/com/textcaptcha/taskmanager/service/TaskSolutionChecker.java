package com.textcaptcha.taskmanager.service;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.taskmanager.pojo.SolutionCheckerResult;

import java.util.List;

public interface TaskSolutionChecker<CT extends CaptchaTask> {

    SolutionCheckerResult checkSolution(CT task, List<Integer> solution);

}
