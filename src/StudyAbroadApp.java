//Author: Nicolas Diaz-Aguilar
//Main application class for Study Abroad Database Application.
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.List;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StudyAbroadApp extends Application {
	private Stage primaryStage;
	private StudyAbroadService service;
	private BorderPane mainLayout;
	private VBox loginForm;
	private VBox programList;

	@Override
	public void start(Stage stage) {
		this.primaryStage = stage;
		this.service = new StudyAbroadService();
		initializeUI();
	}

	//Initializes UI elements.
	private void initializeUI() {
		mainLayout = new BorderPane();;
		showLoginForm();

		Scene scene = new Scene(mainLayout, 800, 600);
		primaryStage.setTitle("Study Abroad Application");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	//Shows login form.
	private void showLoginForm() {
		loginForm = new VBox(15);
		loginForm.setAlignment(Pos.CENTER);
		loginForm.setPadding(new Insets(50));
		loginForm.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));

		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(10.0);
		dropShadow.setColor(Color.color(0, 0, 0, 0.1));
		loginForm.setEffect(dropShadow);

		//Title
		Label titleLabel = new Label("Study Abroad Login");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

		//Text field, password field and button
		TextField userIdField = new TextField();
		userIdField.setMaxWidth(200);
		PasswordField passwordField = new PasswordField();
		passwordField.setMaxWidth(200);
		Button loginButton = new Button("Login");
		loginButton.setMinWidth(75);

		loginButton.setOnAction(e -> handleLogin(userIdField.getText(), passwordField.getText()));

		loginForm.getChildren().addAll(titleLabel, new Label("User ID:"), userIdField, new Label("Password:"), passwordField, loginButton);

		mainLayout.setCenter(loginForm);
	}

	//Handles login attempt.
	private void handleLogin(String userId, String password) {
		if (service.login(userId, password)) {
			switch(service.getUserRole()) {
			case "student":
				showStudentInterface();
				break;
			case "staff":
				showStaffInterface();
				break;
			case "admin":
				showAdminInterface();
				break;
			}
		} else {
			showError("Login failed");
		}
	}

	//Shows student interface to users of student role.
	private void showStudentInterface() {
		GridPane studentView = new GridPane();
		studentView.setAlignment(Pos.CENTER);
		studentView.setHgap(10);
		studentView.setVgap(10);
		studentView.setPadding(new Insets(15));

		Student profile = service.getStudentProfile();
		Label profileLabel = new Label("Welcome, " + profile.getFirstName());
		profileLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		GridPane.setHalignment(profileLabel, HPos.CENTER);

		Button viewProgramsButton = new Button("View Programs");
		viewProgramsButton.setOnAction(e -> showPrograms());

		Button viewApplicationsButton = new Button("My Applications");
		viewApplicationsButton.setOnAction(e -> showStudentApplications());

		Button logoutButton = createLogoutButton();
		logoutButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white;");

		studentView.add(profileLabel, 0, 0, 2, 1);
		studentView.add(viewProgramsButton, 0, 1);
		studentView.add(viewApplicationsButton, 1, 1);
		studentView.add(logoutButton, 0, 2, 2, 1);
		GridPane.setHalignment(logoutButton, HPos.CENTER);

		mainLayout.setCenter(studentView);
	}

	//Shows staff interface to users of staff role.
	private void showStaffInterface() {
		GridPane staffView = new GridPane();
		staffView.setAlignment(Pos.CENTER);
		staffView.setHgap(10);
		staffView.setVgap(10);
		staffView.setPadding(new Insets(15));

		Label titleLabel = new Label("Staff Dashboard");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		GridPane.setHalignment(titleLabel,  HPos.CENTER);

		Button reviewApplicationsButton = new Button("Review Applications");
		reviewApplicationsButton.setOnAction(e -> showPendingReviews());

		Button logoutButton = createLogoutButton();
		logoutButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white;");

		staffView.add(titleLabel, 0, 0);
		staffView.add(reviewApplicationsButton, 0, 1);
		staffView.add(logoutButton, 0, 2);
		GridPane.setHalignment(reviewApplicationsButton, HPos.CENTER);
		GridPane.setHalignment(logoutButton, HPos.CENTER);

		mainLayout.setCenter(staffView);
	}

	//Shows admin interface to users of admin role.
	private void showAdminInterface() {
		GridPane adminView = new GridPane();
		adminView.setAlignment(Pos.CENTER);
		adminView.setHgap(10);
		adminView.setVgap(10);
		adminView.setPadding(new Insets(15));

		Label titleLabel = new Label("Admin Dashboard");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		GridPane.setHalignment(titleLabel, HPos.CENTER);

		Button manageProgramsButton = new Button("Manage Programs");
		manageProgramsButton.setOnAction(e -> showProgramManagement());

		Button logoutButton = createLogoutButton();
		logoutButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white;");

		adminView.add(titleLabel, 0, 0);
		adminView.add(manageProgramsButton, 0, 1);
		adminView.add(logoutButton, 0, 2);
		GridPane.setHalignment(manageProgramsButton, HPos.CENTER);
		GridPane.setHalignment(logoutButton, HPos.CENTER);

		mainLayout.setCenter(adminView);
	}

	//Displays current student applications.
	private void showStudentApplications() {
		VBox applicationsView = new VBox(10);
		applicationsView.setPadding(new Insets(20));

		applicationsView.getChildren().add(createBackButton());

		List<StudentApplication> applications = service.getStudentApplications();
		for (StudentApplication app : applications) {
			HBox appBox = new HBox(10);
			Label statusLabel = new Label("Status: " + app.getStatus());
			Label dateLabel = new Label("Submitted: " + app.getSubmissionDate());

			if (app.getStatus().equals("approved")) {
				Button enrollButton = new Button("Enroll");
				enrollButton.setOnAction(e -> {
					if (service.enrollStudent(app.getSubmittedBy(), app.getAssocSession())) {
						showSuccess("You have successfully enrolled in this program.");
						showStudentApplications();
					} else {
						showError("Error enrolling in program.");
					}
				});
				appBox.getChildren().add(enrollButton);
			}

			appBox.getChildren().addAll(statusLabel, dateLabel);
			applicationsView.getChildren().add(appBox);
		}
		mainLayout.setCenter(applicationsView);
	}

	private void processApplication(StudentApplication app) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Review Application");

		VBox content = new VBox(10);
		content.setPadding(new Insets(10));

		//Application details
		Label studentLabel = new Label("Student ID: " + app.getSubmittedBy());
		Label sessionLabel = new Label("Session ID: " + app.getAssocSession());
		Label dateLabel = new Label("Submitted: " + app.getSubmissionDate());
		Label statementLabel = new Label("Statement: " + app.getStatement());

		Button acceptButton = new Button("Approve");
		Button rejectButton = new Button("Reject");

		acceptButton.setOnAction(e -> {
			if (service.processApplication(app, "approved")) {
				showSuccess("Application approved successfully!");
				dialog.close();
				showPendingReviews();
			} else {
				showError("Failed to process application.");
			}
		});

		rejectButton.setOnAction(e -> {
			if (service.processApplication(app, "rejected")) {
				showSuccess("Application rejected successfully.");
				dialog.close();
				showPendingReviews();
			} else {
				showError("Failed to process application.");
			}
		});

		HBox buttons = new HBox(10, acceptButton, rejectButton);
		content.getChildren().addAll(studentLabel, sessionLabel, dateLabel, statementLabel, buttons);

		dialog.getDialogPane().setContent(content);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

		dialog.show();
	}

	//Displays pending reviews for staff members.
	private void showPendingReviews() {
		VBox reviewsView = new VBox(10);
		reviewsView.setPadding(new Insets(20));	

		reviewsView.getChildren().add(createBackButton());

		Label titleLabel = new Label("Pending Applications");
		titleLabel.setFont(new Font(18));
		reviewsView.getChildren().add(titleLabel);

		List<StudentApplication> pendingApps = service.getPendingReviews();
		if (pendingApps.isEmpty()) {
			reviewsView.getChildren().add(new Label("No pending applications"));
		} else {
			for (StudentApplication app : pendingApps) {
				VBox appBox = new VBox(5);
				appBox.setPadding(new Insets(10));

				Label studentLabel = new Label("Student ID: " + app.getSubmittedBy());
				Label sessionLabel = new Label("Session ID: " + app.getAssocSession());
				Label dateLabel = new Label("Submitted: " + app.getSubmissionDate());
				Button reviewButton = new Button("Review");

				reviewButton.setOnAction(e -> processApplication(app));
				appBox.getChildren().addAll(studentLabel, sessionLabel, dateLabel, reviewButton);
				reviewsView.getChildren().add(appBox);
			}
		}

		ScrollPane scrollPane = new ScrollPane(reviewsView);
		scrollPane.setFitToWidth(true);
		mainLayout.setCenter(scrollPane);
	}

	//Displays a program management window for admins.
	private void showProgramManagement() {
		VBox managementView = new VBox(10);
		managementView.setPadding(new Insets(20));

		managementView.getChildren().add(createBackButton());

		Label titleLabel = new Label("Program Management");
		titleLabel.setFont(new Font(18));

		List<Program> programs = service.getAvailablePrograms();
		for (Program prog : programs) {
			HBox programBox = new HBox(10);
			Label nameLabel = new Label(prog.getName());
			Button editButton = new Button("Edit");
			editButton.setOnAction(e -> showEditProgramForm(prog));
			programBox.getChildren().addAll(nameLabel, editButton);
			managementView.getChildren().add(programBox);
		}
		managementView.getChildren().add(0, titleLabel);
		mainLayout.setCenter(managementView);
	}

	//Form for admin editing a program.
	private void showEditProgramForm(Program program) {
		Dialog<Program> dialog = new Dialog<>();
		dialog.setTitle("Edit Program");

		TextField nameField = new TextField(program.getName());
		TextField gpaField = new TextField(String.valueOf(program.getMinGPAReq()));

		VBox form = new VBox(10);
		form.getChildren().addAll(new Label("Name:"), nameField, new Label("Minimum GPA:"), gpaField);

		dialog.getDialogPane().setContent(form);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.setResultConverter(button -> {
			if (button == ButtonType.OK) {
				program.setName(nameField.getText());
				try {
					program.setMinGPAReq(Double.parseDouble(gpaField.getText()));
				} catch (NumberFormatException e) {
					showError("Invalid GPA format!");
					return null;
				}
				return program;
			}
			return null;
		});

		dialog.showAndWait().ifPresent(updatedProgram -> {
			if (service.updateProgramDetails(updatedProgram)) {
				showSuccess("Program updated successfully!");
				showProgramManagement();
			} else {
				showError("Failed to update program. Check your permissions for managing this program.");
			}
		});
	}

	//Displays available programs.
	private void showPrograms() {
		programList = new VBox(15);
		programList.setPadding(new Insets(20));

		programList.getChildren().add(createBackButton());

		Label headerLabel = new Label("Available Programs");
		headerLabel.setFont(new Font(18));
		programList.getChildren().add(headerLabel);

		List<Program> programs = service.getAvailablePrograms();
		for (Program program : programs) {
			VBox programBox = new VBox(10);
			programBox.setPadding(new Insets(10));

			Label nameLabel = new Label(program.getName());
			HBox buttonBox = new HBox(10);

			Button applyButton = new Button("Apply");
			Button detailsButton = new Button("Details");

			applyButton.setOnAction(e -> showApplicationForm(program));
			detailsButton.setOnAction(e -> showProgramDetails(program));

			buttonBox.getChildren().addAll(applyButton, detailsButton);
			programBox.getChildren().addAll(nameLabel, buttonBox);
			programList.getChildren().add(programBox);
		}

		ScrollPane scrollPane = new ScrollPane(programList);
		scrollPane.setFitToWidth(true);
		mainLayout.setCenter(programList);

	}

	//Displays detailed information for a specific program.
	private void showProgramDetails(Program program) {
		Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle("Program Details: " + program.getName());

		Map<String, String> details = service.getProgramDetails(program.getProgramId());

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		int row = 0;
		String[] detailLabels = {
				"Program ID", "Name", "Type", "Destination", "Partner Institution", "Minimum GPA Required", "Duration (Weeks)", "Language Required", "Language Level"
		};
		String[] detailKeys = {
				"programId", "name", "type", "destination", "partnerInstitution", "minGPAReq", "durationWeeks", "langCode", "level"
		};

		for (int i = 0; i < detailKeys.length; i++) {
			Label labelName = new Label(detailLabels[i] + ":");
			Label labelValue = new Label(details.get(detailKeys[i]));

			grid.add(labelName,  0, row);
			grid.add(labelValue, 1, row);
			row++;
		}

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

		dialog.show();
	}

	//Displays application form.
	private void showApplicationForm(Program program) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Apply for " + program.getName());

		TextArea statementArea = new TextArea();
		dialog.getDialogPane().setContent(statementArea);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				return statementArea.getText();
			}
			return null;
		});

		dialog.showAndWait().ifPresent(statement -> {
			if (service.submitApplication(program.getProgramId(), statement)) {
				showSuccess("Application submitted successfully");
			} else {
				showError("Application submission failed. You might not meet all of the requirements for this program.");
			}
		});
	}

	//Creates a log out button that disconnects the user from the application and closes the connection with the database.
	private Button createLogoutButton() {
		Button logoutButton = new Button("Logout");
		logoutButton.setOnAction(e -> {
			service.logout();
			showLoginForm();
		});
		return logoutButton;
	}

	//Creates a back button that takes the user, according to their role, to their predetermined interface.
	private Button createBackButton() {
		Button backButton = new Button("Go Back");
		backButton.setOnAction(e -> {
			switch(service.getUserRole()) {
			case "student":
				showStudentInterface();
				break;
			case "staff":
				showStaffInterface();
				break;
			case "admin":
				showAdminInterface();
				break;
			}
		});
		return backButton;
	}

	//Error alert.
	private void showError(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message);
		alert.showAndWait();
	}

	//Success alert.
	private void showSuccess(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(message);
		alert.showAndWait();
	}

	//Main method.
	public static void main(String[] args) {
		launch(args);
	}
}
