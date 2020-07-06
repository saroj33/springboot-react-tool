package io.srjhelp.ppmtool.services;

import io.srjhelp.ppmtool.domain.Backlog;
import io.srjhelp.ppmtool.domain.Project;
import io.srjhelp.ppmtool.exceptions.ProjectIdException;
import io.srjhelp.ppmtool.repositories.BacklogRepository;
import io.srjhelp.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public  Project saveOrUpdateProject(Project project){
        try {
            String projectIdentifier=project.getProjectIdentifier().toUpperCase();
            project.setProjectIdentifier(projectIdentifier);
            //if new project  then ID is null and then we create backlog for it.
            if(project.getId()==null){
                Backlog backlog=new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            }
            if(project.getId()!=null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            }
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");

        }
    }
    public  Project findProjectByIdentifier(String projectID){
        Project project= projectRepository.findByProjectIdentifier(projectID.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Project ID '"+projectID+"' doesn't exist.");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project  project = projectRepository.findByProjectIdentifier(projectId);
        if (project == null){
            throw new ProjectIdException("Cannot find Project with ID '"+projectId+"'. This project does not exist");
        }

        projectRepository.delete(project);
    }
}
