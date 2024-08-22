package models;


public class Attendance {
    private Session session;
    private User student;
    private boolean isPresent;
    private String note;
    private String recordedTime;

    public Attendance() {
    }

    public Attendance(Session session, User student, boolean isPresent, String note, String recordedTime) {
        this.session = session;
        this.student = student;
        this.isPresent = isPresent;
        this.note = note;
        this.recordedTime = recordedTime;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(String recordedTime) {
        this.recordedTime = recordedTime;
    }
}
