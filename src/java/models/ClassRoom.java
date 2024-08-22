package models;

import java.util.ArrayList;
import java.util.List;

public class ClassRoom {

    private int classId;
    private int level;
    private String className;
    private User TeacherInCharge;
    private int year;
    private int chargeId;
    private int limitNumberOfStudent;
    private boolean active;

    private List<User> students = new ArrayList();

    public ClassRoom() {
    }

    public ClassRoom(int classId, int level, String className, int chargeId, int year,
            int limitNumberOfStudent, boolean isActive) {
        this.classId = classId;
        this.className = className;
        this.level = level;
        this.year = year;
        this.chargeId = chargeId;
        this.limitNumberOfStudent = limitNumberOfStudent;
        this.active = isActive;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    
    public boolean IsActive() {
        return active;
    }

    public void setIsActive(boolean isActive) {
        this.active = isActive;
    }

    public int getChargeId() {
        return chargeId;
    }

    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public int getLimitNumberOfStudent() {
        return limitNumberOfStudent;
    }

    public void setLimitNumberOfStudent(int limitNumberOfStudent) {
        this.limitNumberOfStudent = limitNumberOfStudent;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public User getTeacherInCharge() {
        return TeacherInCharge;
    }

    public void setTeacherInCharge(User TeacherInCharge) {
        this.TeacherInCharge = TeacherInCharge;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

   

}
