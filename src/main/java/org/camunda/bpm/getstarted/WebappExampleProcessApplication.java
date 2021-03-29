package org.camunda.bpm.getstarted;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Attachment;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

import java.util.List;

@SpringBootApplication
@EnableProcessApplication
public class WebappExampleProcessApplication {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    public static void main(String... args) {
        SpringApplication.run(WebappExampleProcessApplication.class, args);
    }

    @EventListener
    private void processPostDeploy(PostDeployEvent event) {

        String processInstanceId = runtimeService.startProcessInstanceByKey("sample").getProcessInstanceId();
        logger.info("started instance sample");

        Task approveStep = getActiveTask(processInstanceId, "Approve Application");

        // write comments before completing task
        taskService.createComment(approveStep.getId(), processInstanceId, "all looks okay");
        // save some attachment etc
        taskService.saveAttachment(createSampleAttachment(approveStep));

        // retrieve all comments of approve task
        List<Comment> taskComments = taskService.getTaskComments(approveStep.getId());
        taskComments.forEach(comment -> logger.info("" + comment.getFullMessage()));
        //    taskService.complete(approveStep.getId());

    }

    private Attachment createSampleAttachment(Task task) {
        String attachmentType = "someAttachment";
        String processInstanceId = "someProcessInstanceId";
        String attachmentName = "attachmentName";
        String attachmentDescription = "attachmentDescription";
        String url = "http://camunda.org";

        Attachment attachment = taskService.createAttachment(
                attachmentType,
                task.getId(),
                processInstanceId,
                attachmentName,
                attachmentDescription,
                url);

        // when
        attachment.setDescription("updatedDescription");
        attachment.setName("updatedName");

        return attachment;
    }

    private Task getActiveTask(String processInstanceId, String taskName) {
        List<Task> tasks = taskService.createTaskQuery().active()
                .processInstanceId(processInstanceId).list();

        for (Task task : tasks) {
            if (taskName.equals(task.getName())) {
                return task;
            }
        }
        return null;
    }

    private ProcessInstance getProcessInstanceByName(String name) {

        // query for latest process definition with given name
        ProcessDefinition myProcessDefinition =
                repositoryService.createProcessDefinitionQuery()
                        .processDefinitionName(name)
                        .latestVersion()
                        .singleResult();


        List<ProcessInstance> processInstances =
                runtimeService.createProcessInstanceQuery()
                        .processDefinitionId(myProcessDefinition.getId())
                        .active() // we only want the unsuspended process instances
                        .list();

        return processInstances.get(0);
    }

}