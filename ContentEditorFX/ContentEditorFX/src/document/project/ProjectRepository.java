package document.project;

import java.util.ArrayList;
import java.util.UUID;

public class ProjectRepository {

	private static ProjectRepository instance = null;
	private static ArrayList<ProjectEnvironment> projectList;
	private static ProjectEnvironment activeProjectEnvironment;
	
	public ProjectRepository() {
		if(instance != null) {
			throw new RuntimeException("Duplicate Project Repository");
		}
		instance = this;
		projectList = new ArrayList<ProjectEnvironment>();
	}
	
	public static ArrayList<ProjectEnvironment> getProjectList() {
		return projectList;
	}
	
	public static void addProject(ProjectEnvironment project) {
		projectList.add(project);
	}
	
	public static ProjectEnvironment getProject(UUID uuid) {
		for(int i = 0; i < projectList.size(); i++) {
			if(projectList.get(i).getUUID().equals(uuid))
				return projectList.get(i);
		}
		return null;
	}
	
	public static ProjectEnvironment getActiveProjectEnvironment() {
		return activeProjectEnvironment;
	}

	public static void createNewProjectEnvironment() {
		ProjectEnvironment newEnvironment = new ProjectEnvironment();
		addProject(newEnvironment);
		activeProjectEnvironment = newEnvironment;
	}
	
}
