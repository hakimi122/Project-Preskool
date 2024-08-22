
package models;

public class ExamStudent {
    private int examId;
    private Exam Exam;
    
    private User Student;
    private int studentId;

    public ExamStudent() {
    }

    public ExamStudent(int examId, Exam Exam, User Student, int studentId) {
        this.examId = examId;
        this.Exam = Exam;
        this.Student = Student;
        this.studentId = studentId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public Exam getExam() {
        return Exam;
    }

    public void setExam(Exam Exam) {
        this.Exam = Exam;
    }

    public User getStudent() {
        return Student;
    }

    public void setStudent(User Student) {
        this.Student = Student;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    
    
}
