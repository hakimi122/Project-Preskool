package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int userId;
    private String username;
    private String password;
    private String fullName;
    private Date dob;
    private String gender;
    private String avatar;
    private String phone;
    private String email;
    private String address;
    private String role; // 'student', 'teacher', 'headmaster'
    private boolean active; // 1: active, 0: inactive
    private List<ClassRoom> classes = new ArrayList();
    private String historyPassword;
    
    private List<Subject> subjects = new ArrayList();
    
    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
       
    public String getHistoryPassword() {
        return historyPassword;
    }

    public void setHistoryPassword(String historyPassword) {
        this.historyPassword = historyPassword;
    }

    public User(int userId, String username, String password, String fullName, Date dob, String gender, String avatar, String phone, String email, String address, String role, boolean active, String historyPassword) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
        this.active = active;
        this.historyPassword = historyPassword;
    }   

    public User() {
    }
    
    public User(String username, String password, String fullName,
            Date dob, String gender, String avatar, String phone, 
            String email, String address, String role, boolean active) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
        this.active = active;
    }

    public List<ClassRoom> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassRoom> classes) {
        this.classes = classes;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
