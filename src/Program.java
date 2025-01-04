//Author: Nicolas Diaz-Aguilar
//Program class to hold program's information.
public class Program {
	private int programId;
	private String name;
	private String type;
	private String destination;
	private String partnerInstitution;
	private double minGPAReq;
	private int durationWeeks;
	private int spotsAvailable;

	public Program(int programId, String name, String type, String destination, int spotsAvailable) {
		this.programId = programId;
		this.name = name;
		this.type = type;
		this.destination = destination;
		this.spotsAvailable = spotsAvailable;
	}

	//Getters and setters.

	public int getProgramId() {
		return programId;
	}
	public void setProgramId(int programId) {
		this.programId = programId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getSpotsAvailable() {
		return spotsAvailable;
	}
	public void setSpotsAvailable(int spotsAvailable) {
		this.spotsAvailable = spotsAvailable;
	}

	public String getPartnerInstitution() {
		return partnerInstitution;
	}

	public void setPartnerInstitution(String partnerInstitution) {
		this.partnerInstitution = partnerInstitution;
	}

	public double getMinGPAReq() {
		return minGPAReq;
	}

	public void setMinGPAReq(double minGPAReq) {
		this.minGPAReq = minGPAReq;
	}

	public int getDurationWeeks() {
		return durationWeeks;
	}

	public void setDurationWeeks(int durationWeeks) {
		this.durationWeeks = durationWeeks;
	}

}
