
package models;

public class StudentClass {

    private ClassRoom classrom;
    private User Student;
    private float scoreSemester1;
    private float scoreSemester2;

    private int studentId;
    private int classId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public float getScoreSemester1() {
        return scoreSemester1;
    }

    public void setScoreSemester1(float scoreSemester1) {
        this.scoreSemester1 = scoreSemester1;
    }

    public float getScoreSemester2() {
        return scoreSemester2;
    }

    public void setScoreSemester2(float scoreSemester2) {
        this.scoreSemester2 = scoreSemester2;
    }

   

    public StudentClass() {
    }

    public StudentClass(ClassRoom classrom, User Student) {
        this.classrom = classrom;
        this.Student = Student;
    }

    public ClassRoom getClassrom() {
        return classrom;
    }

    public void setClassrom(ClassRoom classrom) {
        this.classrom = classrom;
    }

    public User getStudent() {
        return Student;
    }

    public void setStudent(User Student) {
        this.Student = Student;
    }

}
