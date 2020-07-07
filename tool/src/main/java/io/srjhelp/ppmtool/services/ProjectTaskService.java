package io.srjhelp.ppmtool.services;

import io.srjhelp.ppmtool.domain.Backlog;
import io.srjhelp.ppmtool.domain.Project;
import io.srjhelp.ppmtool.domain.ProjectTask;
import io.srjhelp.ppmtool.exceptions.ProjectNotFoundException;
import io.srjhelp.ppmtool.repositories.BacklogRepository;
import io.srjhelp.ppmtool.repositories.ProjectRepository;
import io.srjhelp.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            //set the bl to pt
            projectTask.setBacklog(backlog);
            Integer BacklogSequence = backlog.getPTSequence();
            BacklogSequence++;
            backlog.setPTSequence(BacklogSequence);
            //Add Sequence to Project Task
            projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority when priority is null
            if (projectTask.getPriority() == null) { //In the future we need projectTask.getPriority()==0 to handle the form
                projectTask.setPriority(3);
            }
            //INITIAL status when status is null
            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Project Not Found");
        }

    }

    public Iterable<ProjectTask>findBacklogById(String id){
        Project project= projectRepository.findByProjectIdentifier(id);
        if (project==null){
            throw new ProjectNotFoundException("Project with ID: '"+id+"' does not exist");
        }
    return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public  ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){
        //make sure we are searching on the right backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog==null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exists");
        }


        //make sure our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

        if (projectTask==null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if (!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
        }

        return projectTaskRepository.findByProjectSequence(pt_id);
    }

    public  ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id,String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);
        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id,String pt_id) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);

        //hackish way need to improve
//        Backlog backlog=projectTask.getBacklog();
//        List<ProjectTask> pts = backlog.getProjectTasks();
//        pts.remove(projectTask);
//        backlogRepository.save(backlog);
        projectTaskRepository.delete(projectTask);
    }
}
