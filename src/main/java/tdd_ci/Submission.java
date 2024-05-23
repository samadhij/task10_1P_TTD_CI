package tdd_ci;

public class Submission {
	
	private String studentId;
    private String assignmentId;
    private String alignment;
    private String justification;
    private String fileName;

    public Submission(String studentId, String assignmentId, String alignment, String justification, String fileName) {
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.alignment = alignment;
        this.justification = justification;
        this.fileName = fileName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public String getAlignment() {
        return alignment;
    }

    public String getJustification() {
        return justification;
    }

    public String getFileName() {
        return fileName;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
