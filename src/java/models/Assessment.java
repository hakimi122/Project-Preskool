
package models;

public class Assessment {
    private int studentId, year;
    private String status, note;
    
    private float averageScore;
    
    public Assessment() {
    }

    public Assessment(int studentId, int year, String status, String rank) {
        this.studentId = studentId;
        this.year = year;
        this.status = status;
        this.note = rank;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

   
    
    
}
