package io.srjhelp.ppmtool.services;

import io.srjhelp.ppmtool.domain.Project;
import io.srjhelp.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public  Project saveOrUpdateProject(Project project){
    //Lot of logic
        return  projectRepository.save(project);
    }
}
