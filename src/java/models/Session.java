package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Session {

    private int sessionId;
    private int ClassId;
    private int SlotId;
    private int SubjectId;
    private int SupervisorId;
    private ClassRoom ClassRoom;
    private TimeSlot timeSlot;
    private String room;
    private Subject subject;
    private User teacher;
    private Date date;
    private boolean isAttended;
    private int index;
    private List<Attendance> attendances = new ArrayList<>();

    public Session() {
    }

    public int getClassId() {
        return ClassId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setClassId(int ClassId) {
        this.ClassId = ClassId;
    }

    public int getSlotId() {
        return SlotId;
    }

    public void setSlotId(int SlotId) {
        this.SlotId = SlotId;
    }

    public int getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(int SubjectId) {
        this.SubjectId = SubjectId;
    }

    public int getSupervisorId() {
        return SupervisorId;
    }

    public void setSupervisorId(int SupervisorId) {
        this.SupervisorId = SupervisorId;
    }

    public Session(int sessionId, ClassRoom aClass, TimeSlot timeSlot, User teacher, Date date, boolean isAttended, int index) {
        this.sessionId = sessionId;
        this.ClassRoom = aClass;
        this.timeSlot = timeSlot;
        this.teacher = teacher;
        this.date = date;
        this.isAttended = isAttended;
        this.index = index;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }
    
    

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public ClassRoom getClassRoom() {
        return this.ClassRoom;
    }

    public void setClassRoom(ClassRoom Class) {
        this.ClassRoom = Class;
    }

    public boolean isIsAttended() {
        return isAttended;
    }

    public void setIsAttended(boolean isAttended) {
        this.isAttended = isAttended;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAttended() {
        return isAttended;
    }

    public void setAttended(boolean attended) {
        isAttended = attended;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
