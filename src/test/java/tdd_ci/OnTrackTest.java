package tdd_ci;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;

public class OnTrackTest {
	
	private OnTrack onTrack;

    @Before
    public void setUp() {
        onTrack = new OnTrack();
    }
	
    @Test
    public void testAuthenticateUserSuccess() {
        Assert.assertTrue(onTrack.authenticateUser("samadhi", "samadhi_pass"));
    }

    @Test
    public void testAuthenticateUserNonExistentUser() {
        Assert.assertFalse(onTrack.authenticateUser("user_not_exist", "password"));
    }

    @Test
    public void testAuthenticateUserWrongPassword() {
        Assert.assertFalse(onTrack.authenticateUser("samadhi", "wrong_password"));
    }
	
	@Test
    public void testGetEnrolledCourses() {
        List<Course> courses = onTrack.getEnrolledCourses("samadhi");
        Assert.assertEquals(2, courses.size());
        Assert.assertEquals("software_qa", courses.get(0).getName());
    }

    @Test
    public void testGetAssignments() {
        List<Assignment> assignments = onTrack.getAssignments("sit707");
        Assert.assertEquals(2, assignments.size());
        Assert.assertEquals("tdd_ci", assignments.get(0).getTitle());
    }

    @Test
    public void testInitiateTaskSubmission() {
    	String result = onTrack.initiateTaskSubmission("samadhi", "sit707", "task1");
    	Assert.assertEquals("Task submission initiated", result);

        // Test with invalid parameters
        Assert.assertEquals("Student not enrolled in the course", onTrack.initiateTaskSubmission("samadhi", "non_existing_course", "task1"));
        Assert.assertEquals("Course has no assignments", onTrack.initiateTaskSubmission("samadhi", "sit714", "task1"));
        Assert.assertEquals("Assignment not found in the course", onTrack.initiateTaskSubmission("samadhi", "sit707", "non_existing_assignment"));
    }

    @Test
    public void testUploadFile() {
    	Assert.assertEquals("File uploaded successfully", onTrack.uploadFile("samadhi", "task1", "assignment.pdf"));
    	Assert.assertEquals("Invalid file format", onTrack.uploadFile("samadhi", "task1", "assignment.docx"));

        // Test with invalid parameters
    	Assert.assertEquals("Student not found", onTrack.uploadFile("non_existing_student", "task1", "assignment.pdf"));
    	Assert.assertEquals("Assignment not found", onTrack.uploadFile("samadhi", "non_existing_assignment", "assignment.pdf"));
    }

    @Test
    public void testProvideLearningOutcome() {
    	// First upload the file to ensure a submission object is created
        onTrack.uploadFile("samadhi", "task1", "assignment.pdf");
    	String result = onTrack.provideLearningOutcome("samadhi", "task1", "Outcome 1", "Justification text");
    	Assert.assertEquals("Learning outcome provided successfully", result);

        // Test with invalid parameters
    	Assert.assertEquals("Student not found", onTrack.provideLearningOutcome("non_existing_student", "task1", "Outcome 1", "Justification text"));
    	Assert.assertEquals("Assignment not found", onTrack.provideLearningOutcome("samadhi", "non_existing_assignment", "Outcome 1", "Justification text"));
    }

    @Test
    public void testSubmitTask() {
    	/// First provide the learning outcome to ensure a submission object is created
        onTrack.uploadFile("samadhi", "task1", "assignment.pdf");
        onTrack.provideLearningOutcome("samadhi", "task1", "Outcome 1", "Justification text");

    	String result = onTrack.submitTask("samadhi", "task1", "Outcome 1", "Justification text", "assignment.pdf");
        assertEquals("Task submitted successfully", result);

        // Test with invalid parameters
        assertEquals("Student not found", onTrack.submitTask("non_existing_student", "task1", "Outcome 1", "Justification text", "assignment.pdf"));
        assertEquals("Assignment not found", onTrack.submitTask("samadhi", "non_existing_assignment", "Outcome 1", "Justification text", "assignment.pdf"));
    }

    @Test
    public void testSendConfirmation() {
    	String result = onTrack.sendConfirmation("samadhi", "task1");
        assertEquals("Confirmation message sent", result);

        // Test with invalid parameters
        assertEquals("Student not found", onTrack.sendConfirmation("non_existing_student", "task1"));
    }

    @Test
    public void testNotifyTutor() {
    	String result = onTrack.notifyTutor("tutor1", "task1");
        assertEquals("Tutor notified", result);

        // Test with invalid parameters
        assertEquals("Tutor not found", onTrack.notifyTutor("non_existing_tutor", "task1"));
        assertEquals("Assignment not found", onTrack.notifyTutor("tutor1", "non_existing_assignment"));
    }
    
    @Test
    public void testViewChatMessages() {
        List<String> chatMessages = onTrack.viewChatMessages("samadhi");
        assertTrue(chatMessages.isEmpty());

        // Add some chat messages for testing
        onTrack.notifyTutor("tutor1", "task1");
        chatMessages = onTrack.viewChatMessages("samadhi");
        assertFalse(chatMessages.isEmpty());
        assertEquals("New submission received for assignment task1 in course sit707", chatMessages.get(0));

        // Test with invalid parameters
        chatMessages = onTrack.viewChatMessages("non_existing_student");
        assertTrue(chatMessages.isEmpty());
    }

}
