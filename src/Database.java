//Author: Nicolas Diaz-Aguilar
//Database class for Study Abroad Database Application.
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
	public static final String DATABASE = "exampledatabasename";
	public static final String HOST = "examplehost";
	public static final int PORT = 3305;
	public static final String USER_ACCOUNT = "exampleuseraccount";
	public static final String ADMIN_ACCOUNT = "exampleadminaccount";
	public static final String USER_PASS = "exampleuserpass";
	public static final String ADMIN_PASS = "exampleadminpass";

	private Connection connection;
	private boolean isAdmin;

	//Connection check.
	public boolean isConnected() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	//Opens a connection with database.
	public boolean openConnection(boolean adminAccess) {
		if (isConnected()) {
			closeConnection();
		}
		try {
			this.isAdmin = adminAccess;
			String username = adminAccess ? ADMIN_ACCOUNT : USER_ACCOUNT;
			String password = adminAccess ? ADMIN_PASS : USER_PASS;
			String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?user=" + username + "&password=" + password + "&useSSL=false";
			connection = DriverManager.getConnection(url);
			System.out.println("Database connected successfully. Connected as: " + username);
			return true;
		} catch (SQLException e) {
			System.err.println("Connection error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	//Closes connection with database.
	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.err.println("Error closing connection: " + e.getMessage());
		}
	}

	//Login verification using stored procedure.
	public String verifyLogin(String userId, String password) {
		String role = null;
		try {
			CallableStatement stmt = connection.prepareCall("{CALL sp_user_login(?, ?, ?)}");	
			stmt.setString(1, userId);
			stmt.setString(2, password);
			stmt.registerOutParameter(3, Types.VARCHAR);

			stmt.execute();
			role = stmt.getString(3);

			stmt.close();

			System.out.println("Login successful. Role: " + role);

		} catch (SQLException e) {
			System.err.println("Login error: " + e.getMessage());
		}
		return role;
	}

	//Get available programs.
	public List<Program> getAvailablePrograms() {
		List<Program> programs = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT * FROM view_available_programs"
					);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Program program = new Program(
						rs.getInt("programId"),
						rs.getString("name"),
						rs.getString("type"),
						rs.getString("destination"),
						rs.getInt("spots_available")
						);
				programs.add(program);
			}
		} catch (SQLException e) {
			System.err.println("Error getting programs: " + e.getMessage());
		}
		return programs;
	}

	//Shows specific program's details.
	public Map<String, String> showProgramDetails(int programId) {
		Map<String, String> programDetails = new HashMap<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT * FROM view_program_requirements WHERE programId = ?"
					);
			stmt.setInt(1, programId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				programDetails.put("programId", String.valueOf(rs.getInt("programId")));
				programDetails.put("name", rs.getString("name"));
				programDetails.put("type", rs.getString("type"));
				programDetails.put("destination", rs.getString("destination"));
				programDetails.put("partnerInstitution", rs.getString("partnerInstitution"));
				programDetails.put("minGPAReq", String.valueOf(rs.getDouble("minGPAReq")));
				programDetails.put("durationWeeks", String.valueOf(rs.getInt("durationWeeks")));
				programDetails.put("langCode", rs.getString("langCode"));
				programDetails.put("level", rs.getString("level"));
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.err.println("Error getting program details: " + e.getMessage());
		}
		return programDetails;
	}

	//Submit application using stored procedure.
	public boolean submitApplication(int studentId, int sessionId, String statement) {
		try {
			CallableStatement stmt = connection.prepareCall("{CALL sp_submit_application(?, ?, ?, ?)}");
			stmt.setInt(1, studentId);
			stmt.setInt(2, sessionId);
			stmt.setString(3, statement);
			stmt.registerOutParameter(4, Types.BOOLEAN);
			stmt.execute();
			return stmt.getBoolean(4);
		} catch (SQLException e) {
			System.err.println("Application submission error: " + e.getMessage());
			return false;
		}
	}

	//Student applications view method. Returns a list of student applications.
	public List<StudentApplication> getStudentApplications(int studentId) {
		List<StudentApplication> applications = new ArrayList<>();
		try {
			if (!isConnected()) {
				openConnection(false);
			}

			PreparedStatement stmt = connection.prepareStatement(
					"SELECT * FROM view_student_applications WHERE submittedBy = ?"
					);
			stmt.setInt(1, studentId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				StudentApplication app = new StudentApplication(rs.getInt("submittedBy"), rs.getInt("assocSession"), rs.getString("submissionDate"), rs.getString("statement"), rs.getString("status"));
				applications.add(app);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.err.println("Error getting student applications: " + e.getMessage());
		}
		return applications;
	}

	//Staff stored procedure method. Processes an application.
	public boolean processApplication(int submittedBy, int assocSession, String status) {
		try {
			CallableStatement stmt = connection.prepareCall("{CALL sp_process_application(?, ?, ?, ?)}");
			stmt.setInt(1, submittedBy);
			stmt.setInt(2, assocSession);
			stmt.setString(3, status);
			stmt.registerOutParameter(4, Types.BOOLEAN);
			stmt.execute();
			return stmt.getBoolean(4);
		} catch (SQLException e) {
			System.err.println("Error processing application: " + e.getMessage());
			return false;
		}
	}

	//Student view method. Retrieves student's information.
	public Student getStudentProfile(String userId) {
		Student student = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT * FROM view_student_profile WHERE userId = ?"
					);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				student = new Student(
						rs.getInt("studentId"),
						rs.getString("fName"),
						rs.getString("lName"),
						rs.getDouble("cumulativeGPA"),
						rs.getString("academicStanding")
						);		
			}
		} catch (SQLException e) {
			System.err.println("Error getting student profile: " + e.getMessage());
		}
		return student;
	}

	//Staff view method. Retrieves pending application reviews.
	public List<StudentApplication> getPendingReviews(String staffId) {
		List<StudentApplication> applications = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT * FROM view_pending_reviews WHERE staffId = ?"
					);
			stmt.setString(1, staffId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				applications.add(new StudentApplication (
						rs.getInt("submittedBy"),
						rs.getInt("assocSession"),
						rs.getString("submissionDate"),
						rs.getString("statement"),
						rs.getString("status")
						));
			}
		} catch (SQLException e) {
			System.err.println("Error getting pending reviews: " + e.getMessage());
		}
		return applications;
	}

	//Admin method using stored procedure. Updates program details.
	public boolean updateProgramDetails(int programId, String adminId, String name, double minGPAReq) {
		try {
			CallableStatement stmt = connection.prepareCall("{CALL sp_update_program(?, ?, ?, ?, ?)}");
			stmt.setInt(1, programId);
			stmt.setString(2, adminId);
			stmt.setString(3, name);
			stmt.setDouble(4, minGPAReq);
			stmt.registerOutParameter(5, Types.BOOLEAN);
			stmt.execute();
			return stmt.getBoolean(5);
		} catch (SQLException e) {
			System.err.println("Error updating program: " + e.getMessage());
			return false;
		}
	}

	//Enrolls a student in a session.
	public boolean enrollStudent(int studentId, int sessionId) {
		try {
			CallableStatement stmt = connection.prepareCall("{CALL sp_enroll_student(?, ?, ?)}");
			stmt.setInt(1, studentId);
			stmt.setInt(2, sessionId);
			stmt.registerOutParameter(3, Types.BOOLEAN);
			stmt.execute();
			return stmt.getBoolean(3);
		} catch (SQLException e) {
			System.err.println("Error enrolling student: " + e.getMessage());
			return false;
		}
	}
}
