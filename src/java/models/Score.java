package models;
import java.math.BigDecimal;

public class Score {
    private User student;
    private Assignment assignment;
    private BigDecimal score;
    private String submissionDate;

    public Score() {
    }

    public Score(User student, Assignment assignment, BigDecimal score, String submissionDate) {
        this.student = student;
        this.assignment = assignment;
        this.score = score;
        this.submissionDate = submissionDate;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }
}
