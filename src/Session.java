//Author: Nicolas Diaz-Aguilar
//Session class to hold session's information.
import java.time.LocalDate;

public class Session {
	private int sessionId;
	private int programId;
	private LocalDate startDate;
	private LocalDate endDate;
	private int capacity;
	private double cost;

	public Session(int sessionId, int programId, LocalDate startDate, LocalDate endDate, int capacity, double cost) {
		this.sessionId = sessionId;
		this.programId = programId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.capacity = capacity;
		this.cost = cost;
	}

	//Getters and setters.

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getProgramId() {
		return programId;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

}
