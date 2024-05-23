package tdd_ci;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnTrack {
	// maps usernames to passwords.
	private Map<String, String> users = new HashMap<>();
	// maps student IDs to lists of enrolled courses
    private Map<String, List<Course>> studentCourses = new HashMap<>();
    // maps course IDs to lists of assignments
    private Map<String, List<Assignment>> courseAssignments = new HashMap<>();
    // maps assignment IDs to submissions
    private Map<String, Submission> submissions = new HashMap<>();
    // maps course IDs to tutor IDs
    private Map<String, String> courseTutors = new HashMap<>();
    // maps tutor IDs to chat messages
    private Map<String, List<String>> tutorChats = new HashMap<>();

    public OnTrack() {
        // Mock data
        users.put("samadhi", "samadhi_pass");
        users.put("tutor1", "tutor_pass");

        Course course1 = new Course("sit707", "software_qa");
        Course course2 = new Course("sit714", "enterprise_system");
        studentCourses.put("samadhi", List.of(course1,course2));

        Assignment assignment1 = new Assignment("task1", "tdd_ci");
        Assignment assignment2 = new Assignment("task2", "code_coverage");
        courseAssignments.put("sit707", List.of(assignment1,assignment2));
        
        courseTutors.put("sit707", "tutor1");
    }

    public boolean authenticateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public List<Course> getEnrolledCourses(String studentId) {
        return studentCourses.getOrDefault(studentId, Collections.emptyList());
    }

    public List<Assignment> getAssignments(String courseId) {
        return courseAssignments.getOrDefault(courseId, Collections.emptyList());
    }

    public String initiateTaskSubmission(String studentId, String courseId, String assignmentId) {
    	// Check if student is enrolled in the course
        List<Course> courses = studentCourses.get(studentId);
        if (courses == null) {
            return "Student not enrolled in any courses";
        }
        boolean courseFound = false;
        for (Course course : courses) {
            if (course.getId().equals(courseId)) {
                courseFound = true;
                break;
            }
        }
        if (!courseFound) {
            return "Student not enrolled in the course";
        }

        // Check if course has the assignment
        List<Assignment> assignments = courseAssignments.get(courseId);
        if (assignments == null) {
            return "Course has no assignments";
        }
        boolean assignmentFound = false;
        for (Assignment assignment : assignments) {
            if (assignment.getId().equals(assignmentId)) {
                assignmentFound = true;
                break;
            }
        }
        if (!assignmentFound) {
            return "Assignment not found in the course";
        }

        return "Task submission initiated";
    }

    public String uploadFile(String studentId, String assignmentId, String fileName) {
    	 // Check if the student and assignment exist
        if (!studentCourses.containsKey(studentId)) {
            return "Student not found";
        }
        // Find the course ID associated with the assignment ID
        String courseId = getCourseIdByAssignmentId(assignmentId);
        if (courseId == null) {
            return "Assignment not found";
        }
        // Check if the student is enrolled in the course
        List<Course> enrolledCourses = studentCourses.get(studentId);
        boolean enrolledInCourse = enrolledCourses.stream().anyMatch(course -> course.getId().equals(courseId));
        if (!enrolledInCourse) {
            return "Student not enrolled in the course";
        }
        
     // Retrieve or create the submission object
        Submission submission = submissions.getOrDefault(assignmentId, new Submission(studentId, assignmentId, "", "", ""));
        
        // Check if the file format is valid
        if (!fileName.endsWith(".pdf")) {
            return "Invalid file format";
        }

        // Set the file name in the submission
        submission.setFileName(fileName);

        // Update or add the submission object to the submissions map
        submissions.put(assignmentId, submission);

        return "File uploaded successfully";
    }

    public String provideLearningOutcome(String studentId, String assignmentId, String alignment, String justification) {
    	// Check if the student and assignment exist
        if (!studentCourses.containsKey(studentId)) {
            return "Student not found";
        }
        // Find the course ID associated with the assignment ID
        String courseId = getCourseIdByAssignmentId(assignmentId);
        if (courseId == null) {
            return "Assignment not found";
        }
        
    	// Retrieve or create the submission object
    	Submission submission = submissions.get(assignmentId);
    	if (submission == null) {
    		return "Submission not found";
    	}
        submission.setAlignment(alignment);
        submission.setJustification(justification);
        return "Learning outcome provided successfully";
    }

    public String submitTask(String studentId, String assignmentId, String alignment, String justification, String fileName) {
        // Check if the student and assignment exist
        if (!studentCourses.containsKey(studentId)) {
            return "Student not found";
        }
        // Check if the assignment exists
        String courseId = getCourseIdByAssignmentId(assignmentId);
        if (courseId == null) {
            return "Assignment not found";
        }

        // Retrieve the existing submission object created in uploadFile function
        Submission submission = submissions.get(assignmentId);
        if (submission == null) {
            return "Submission not found";
        }
     
        submissions.put(assignmentId, submission);
        sendConfirmation(studentId, assignmentId);
        
        // Notify the tutor based on the course ID
        String tutorId = courseTutors.get(courseId);
        if (tutorId == null) {
            return "Tutor not found for the course";
        }
        notifyTutor(tutorId, assignmentId);

        return "Task submitted successfully";
    }

    public String sendConfirmation(String studentId, String assignmentId) {
        if (!studentCourses.containsKey(studentId)) {
            return "Student not found";
        }
        // Logic to send confirmation message
        // Mocking the confirmation message logic here
        return "Confirmation message sent";
    }

    private String getCourseIdByAssignmentId(String assignmentId) {
        for (Map.Entry<String, List<Assignment>> entry : courseAssignments.entrySet()) {
            for (Assignment assignment : entry.getValue()) {
                if (assignment.getId().equals(assignmentId)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public String notifyTutor(String tutorId, String assignmentId) {
        // Check if the tutor exists
        if (!users.containsKey(tutorId) || !users.get(tutorId).equals("tutor_pass")) {
            return "Tutor not found";
        }
        // Notify tutor about the new submission
        String courseId = getCourseIdByAssignmentId(assignmentId);
        if (courseId == null) {
            return "Assignment not found";
        }
        List<String> chatMessages = tutorChats.getOrDefault(tutorId, new ArrayList<>());
        chatMessages.add("New submission received for assignment " + assignmentId + " in course " + courseId);
        tutorChats.put(tutorId, chatMessages);
        return "Tutor notified";
    }
    
    public List<String> viewChatMessages(String studentId) {
    	List<Course> enrolledCourses = studentCourses.getOrDefault(studentId, Collections.emptyList());
    	if (enrolledCourses.isEmpty()) {
    		return Collections.emptyList();  // No courses enrolled by the student
    	}
    
    	String courseId = enrolledCourses.get(0).getId(); // Assuming a student is enrolled in only one course
    	String tutorId = courseTutors.get(courseId);
    	if (tutorId == null) {
    		return Collections.emptyList();  // No tutor assigned for the course
    	}
    	return tutorChats.getOrDefault(tutorId, Collections.emptyList());
    }
}

