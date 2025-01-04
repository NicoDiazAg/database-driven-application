//Author: Nicolas Diaz-Aguilar
//Manages connection between UI and database operations.
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudyAbroadService {
	private final Database db;
	private String currentUserId;
	private String userRole;

	public StudyAbroadService() {
		this.db = new Database();
	}

	//Logs a user out.
	public void logout() {
		currentUserId = null;
		userRole = null;
		db.closeConnection();
	}

	//Signs the user in.
	public boolean login(String userId, String password) {
		db.openConnection(true);
		userRole = db.verifyLogin(userId, password);
		if (userRole != null) {
			db.closeConnection();
			db.openConnection(false);
			currentUserId = userId;
			return true;
		}
		db.closeConnection();
		return false;
	}

	//Returns user role that gets stored during login.
	public String getUserRole() {
		return userRole;
	}

	//Returns student profile from the current student user.
	public Student getStudentProfile() {
		return db.getStudentProfile(currentUserId);
	}

	//Gets available programs from the database.
	public List<Program> getAvailablePrograms() {
		return db.getAvailablePrograms();
	}

	//Gets detailed information for a specific program.
	public Map<String, String> getProgramDetails(int programId) {
		return db.showProgramDetails(programId);
	}

	//Sends signal to submit an application.
	public boolean submitApplication(int sessionId, String statement) {
		if (userRole.equals("student")) {
			Student student = getStudentProfile();
			int studentId = student.getStudentId();
			//Switch to admin connection for stored procedure
			db.openConnection(true);
			boolean result = db.submitApplication(studentId, sessionId, statement);
			db.openConnection(false);
			return result;
		}
		return false;
	}

	//Sends signal to database for updating a program.
	public boolean updateProgramDetails(Program program) {
		//Switch to admin connection for stored procedure
		db.openConnection(true);
		boolean result = db.updateProgramDetails(program.getProgramId(), currentUserId, program.getName(), program.getMinGPAReq());
		db.openConnection(false);
		return result;
	}

	//Sends signal to database to retrieve student applications.
	public List<StudentApplication> getStudentApplications() {
		if (userRole.equals("student")) {
			Student student = getStudentProfile();
			if (student != null) {
				return db.getStudentApplications(student.getStudentId());
			}
		}
		return new ArrayList<>();
	}

	//Sends signal to database to get pending reviews.
	public List<StudentApplication> getPendingReviews() {
		if (userRole.equals("staff")) {
			return db.getPendingReviews(currentUserId);
		}
		return new ArrayList<>();
	}

	//Sends signal to database to process application.
	public boolean processApplication(StudentApplication application, String status) {
		if (userRole.equals("staff")) {
			db.openConnection(true);
			boolean result = db.processApplication(application.getSubmittedBy(), application.getAssocSession(), status);
			db.openConnection(false);
			return result;
		}
		return false;
	}

	//Sends a signal to enroll a student in a session.
	public boolean enrollStudent(int studentId, int sessionId) {
		if (userRole.equals("student")) {
			db.openConnection(true);
			boolean result = db.enrollStudent(studentId, sessionId);
			db.openConnection(false);
			return result;
		} else {
			return false;
		}
	}
}
