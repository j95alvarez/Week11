package projects.service;

import java.util.List;

import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

/**
 * Acts as a pass-through between the main application file that 
 * runs the menu (ProjectsApp.java) and the DAO file in the data 
 * layer (ProjectDao.java)
 */
public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();
	
	public Project addProject(Project project) {
		// TODO Auto-generated method stub
		return projectDao.insertProject(project);
	}

	public List<Project> fetchAllProjects() {
		// TODO Auto-generated method stub
		
		return projectDao.fetchAllProjects();
	}

	/**
	 * Calls the project DAO to retrieve all project rows without accompanying details
	 * (materials, steps and categories).
	 * 
	 * @return A list of project records
	 */
	public Project fetchProjectById(Integer projectId) {
		// TODO Auto-generated method stub
		return projectDao.fetchProjectById(projectId).orElseThrow(
				() -> new NoSuchElementException("Project with ID=" +projectId 
						+ " does not exist."));
		
//		return projectDao.fetchProjectById(projectId).orElseThrow(
//				() -> new NoSuchElementException(
//				"Project with Project ID=" + projectId 
//				+ " does not exist."));
	}

	public void modifyProjectDetails(Project project) {
		// TODO Auto-generated method stub
		if (!projectDao.modifyProjectDetails(project)) {
			throw new DbException("Project with ID=" + project.getProjectId() + " does not exist.");
		}
	}

	public void deleteProject(Integer projectId) {
		// TODO Auto-generated method stub
		if (!projectDao.deleteProject(projectId)) {
			throw new DbException("Project with ID=" + projectId + " does not exist.");
		}
	}
	
}
