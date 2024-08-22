package models;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private int subjectId;
    private String subjectName;
    private boolean isTeaching;
    private List<User> teachers = new ArrayList();

    public List<User> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<User> teachers) {
        this.teachers = teachers;
    }
    
    public Subject() {
    }

    public Subject(int subjectId, String subjectName, boolean isTeaching) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.isTeaching = isTeaching;
    }

    public boolean isIsTeaching() {
        return isTeaching;
    }

    public void setIsTeaching(boolean isTeaching) {
        this.isTeaching = isTeaching;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
