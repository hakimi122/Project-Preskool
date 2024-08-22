
package models;

public class Reporting {
    private Integer totalStudents;
    private Integer totalTeachers;
    private Integer totalClasses;
    private Integer totalMaterial;

    public Integer getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Integer getTotalTeachers() {
        return totalTeachers;
    }

    public void setTotalTeachers(Integer totalTeachers) {
        this.totalTeachers = totalTeachers;
    }

    public Integer getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(Integer totalClasses) {
        this.totalClasses = totalClasses;
    }

    public Integer getTotalMaterial() {
        return totalMaterial;
    }

    public void setTotalMaterial(Integer totalMaterial) {
        this.totalMaterial = totalMaterial;
    }
    
    static public class StudentAnalysis {
        private String username;
        private int studentId;
        private int year;
        private String note;
        private String status;
        private int classId;
        private String className;

        public int getStudentId() {
            return studentId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        
        
        public void setStatus(String status) {
            this.status = status;
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
        
        
    }
    
}

