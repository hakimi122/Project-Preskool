
package models;

import java.sql.Date;
import java.util.List;

public class Exam {
    private int examId,subjectId, teacherId, slotId;
    private String examType, room;
    private Date examDate;
    private User Teacher;
    private Subject Subject;
    private TimeSlot Slot;
    private List<User> students;

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }
    
    
    
    public Exam() {
    }

    public Exam(int examId, int subjectId, int teacherId, int slotId, String examType, Date examDate) {
        this.examId = examId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.slotId = slotId;
        this.examType = examType;
        this.examDate = examDate;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    
    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public User getTeacher() {
        return Teacher;
    }

    public void setTeacher(User Teacher) {
        this.Teacher = Teacher;
    }

    public Subject getSubject() {
        return Subject;
    }

    public void setSubject(Subject Subject) {
        this.Subject = Subject;
    }

    public TimeSlot getSlot() {
        return Slot;
    }

    public void setSlot(TimeSlot Slot) {
        this.Slot = Slot;
    }
    
    
    
}
