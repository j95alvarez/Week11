package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;
	
	// @formatter:off
	private List<String> operations = List.of("1) Add a project"
			+ "\n 2) List projects"
			+ "\n 3) Select a project"
			+ "\n 4) Update project details"
			+ "\n 5) Delete a project");
	// @ formatter:on
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ProjectsApp().processUserSelections();
	}

	/**
	 * Displays the menu selections, gets a selection from the user, and 
	 * acts on the selection
	 */
	private void processUserSelections() {
		// TODO Auto-generated method stub
		boolean done = false;
		
		while (!done) {
			try {
				int selection = getUserSelection();
				
				switch (selection) {
					case -1:
						done = exitMenu();
						break;
					case 1:
						// Collect Project details and saves it to the project table
						createProject();
						break;
					case 2:
						// Lists the projects to the user
						listProjects();
						break;
					case 3:
						selectProject();
						break;
					case 4:
						updateProjectDetails();
						break;
					case 5:
						deleteProject();
						break;
					default:
						System.out.println("\n" + selection + " is not a valid selection. Try again.");
						break;
				}
				
			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again.");
			}
		}
	}
	
	private void deleteProject() {
		// TODO Auto-generated method stub
		listProjects();
		
		Integer projectId = getIntInput("Enter the ID of the project to delete");
		
		projectService.deleteProject(projectId);
		System.out.println("Project " + projectId + " was deleted successfully.");
		
		if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId))
			curProject = null;
	}

	private void updateProjectDetails() {
		// TODO Auto-generated method stub
		if (curProject == null) {
			System.out.println("\n Please select a project.");
			return;
		}
		
		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
		
		Project project = new Project();
		
		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		
		projectService.modifyProjectDetails(project);
		
		curProject = projectService.fetchProjectById(curProject.getProjectId());
	}

	private void selectProject() {
		// TODO Auto-generated method stub
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		
		/* Unselect the current project. */
		curProject = null;
		
		curProject = projectService.fetchProjectById(projectId);
	}

	/**
	 * Lists the projects that the user has created
	 */
	private void listProjects() {
		// TODO Auto-generated method stub
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects");
		
		projects.forEach(project -> System.out.println(" " 
		+ project.getProjectId() + ": " + project.getProjectName()));
	}

	/**
	 * Collects the project details and saved into the Project object
	 */
	private void createProject() {
		// TODO Auto-generated method stub
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}

	private boolean exitMenu() {
		// TODO Auto-generated method stub
		System.out.println("Exiting the menu.");
		return true;
	}

	/**
	 * Prints the operations and then accept user input as an Integer
	 * @return	int input from user
	 */
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		return Objects.isNull(input) ? -1 : input;
	}
	
	/**
	 * Prints each available selection on a separate line in the console
	 */
	private void printOperations() {
		System.out.println("\n These are the available selections. Press the Enter key to quit: ");
		operations.forEach(line -> System.out.println(" " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project");
		} else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}
	
	/**
	 * Takes a single parameter of type String. This method 
	 * accepts input from the user and converts it to an Integer, which may be null.
	 * Called by getUserSelection() and will also be called by other data collection 
	 * methods that require an Integer 
	 * @param prompt	User input that will be converted into an Integer
	 * @return			Converted integer that may be null
	 */
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if (Objects.isNull(input))
			return null;
		
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}
	
	/**
	 * Takes a single parameter of type String. This method 
	 * accepts input from the user and converts it to a BigDecimal, which may be null.
	 * Called by getUserSelection() and will also be called by other data collection 
	 * methods that require a BigDecimal
	 * @param prompt
	 * @return
	 */
	private BigDecimal getDecimalInput(String prompt) {
		// TODO Auto-generated method stub
		String input = getStringInput(prompt);
		
		if (Objects.isNull(input))
			return null;
		
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	/**
	 * Lowest level input method. Other input methods call this method and converts 
	 * the input value to the appropriate type. This will also be called by methods
	 * that need to collect String data from the user.
	 * @param prompt	User input that will be converted into a String
	 * @return			String input
	 */
	private String getStringInput(String prompt) {
		// TODO Auto-generated method stub
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}

}
