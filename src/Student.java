//Author: Nicolas Diaz-Aguilar
//Student class to hold student's information.
public class Student {
	private int studentId;
	private String firstName;
	private String lastName;
	private double cumulativeGPA;
	private String academicStanding;
	
	public Student(int studentId, String firstName, String lastName, double cumulativeGPA, String academicStanding) {
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cumulativeGPA = cumulativeGPA;
		this.academicStanding = academicStanding;
	}
	
	//Getters and setters.

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public double getCumulativeGPA() {
		return cumulativeGPA;
	}

	public void setCumulativeGPA(double cumulativeGPA) {
		this.cumulativeGPA = cumulativeGPA;
	}

	public String getAcademicStanding() {
		return academicStanding;
	}

	public void setAcademicStanding(String academicStanding) {
		this.academicStanding = academicStanding;
	}
	
	
	
}
