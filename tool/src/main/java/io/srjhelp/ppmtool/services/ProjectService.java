package io.srjhelp.ppmtool.services;

import io.srjhelp.ppmtool.domain.Project;
import io.srjhelp.ppmtool.exceptions.ProjectIdException;
import io.srjhelp.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public  Project saveOrUpdateProject(Project project){
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
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
}
