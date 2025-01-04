//Author: Nicolas Diaz-Aguilar
//StudentApplication class to hold application's information.
public class StudentApplication {
	private int submittedBy;
	private int assocSession;
	private String submissionDate;
	private String statement;
	private String status;

	public StudentApplication(int submittedBy, int assocSession, String submissionDate, String statement, String status) {
		this.submittedBy = submittedBy;
		this.assocSession = assocSession;
		this.submissionDate = submissionDate;
		this.statement = statement;
		this.status = status;
	}
	
	//Getters and setters.

	public int getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(int submittedBy) {
		this.submittedBy = submittedBy;
	}

	public int getAssocSession() {
		return assocSession;
	}

	public void setAssocSession(int assocSession) {
		this.assocSession = assocSession;
	}

	public String getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
