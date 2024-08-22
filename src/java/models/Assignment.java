package models;

public class Assignment {
    private int assignmentId;
    private String googleFormLink;
    private User teacher;
    private Subject subject;
    private ClassRoom aClass;

    public Assignment() {
    }

    public Assignment(int assignmentId, String googleFormLink, User teacher, Subject subject, ClassRoom aClass) {
        this.assignmentId = assignmentId;
        this.googleFormLink = googleFormLink;
        this.teacher = teacher;
        this.subject = subject;
        this.aClass = aClass;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getGoogleFormLink() {
        return googleFormLink;
    }

    public void setGoogleFormLink(String googleFormLink) {
        this.googleFormLink = googleFormLink;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public ClassRoom getAClass() {
        return aClass;
    }

    public void setAClass(ClassRoom aClass) {
        this.aClass = aClass;
    }
}
