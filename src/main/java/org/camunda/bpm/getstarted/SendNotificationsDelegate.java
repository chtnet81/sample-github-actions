package org.camunda.bpm.getstarted;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *  External class triggered from Send Notification Activity
 */
@Component
public class SendNotificationsDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution execution) {
        logger.info("executing task send notifications : " + execution);
        logger.info("completed task: {}", execution.getCurrentActivityName());
    }



}
