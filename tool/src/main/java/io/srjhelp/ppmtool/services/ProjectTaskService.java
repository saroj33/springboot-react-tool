package io.srjhelp.ppmtool.services;

import io.srjhelp.ppmtool.domain.Backlog;
import io.srjhelp.ppmtool.domain.ProjectTask;
import io.srjhelp.ppmtool.repositories.BacklogRepository;
import io.srjhelp.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        Backlog backlog=backlogRepository.findByProjectIdentifier(projectIdentifier);
        //set the bl to pt
        projectTask.setBacklog(backlog);
        Integer BacklogSequence= backlog.getPTSequence();
        BacklogSequence++;
        backlog.setPTSequence(BacklogSequence);
        //Add Sequence to Project Task
        projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        //INITIAL priority when priority is null
        if (projectTask.getPriority()==null){ //In the future we need projectTask.getPriority()==0 to handle the form
            projectTask.setPriority(3);
        }
        //INITIAL status when status is null
        if (projectTask.getStatus()==""||projectTask.getStatus()==null){
            projectTask.setStatus("TO_DO");
        }
        return projectTaskRepository.save(projectTask);

    }

    public Iterable<ProjectTask>findBacklogById(String id){
    return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
}
