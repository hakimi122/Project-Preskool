package models;

public class Material {

    private int materialId;
    private String materialName;
    private Subject subject;
    private String materialDescription;
    private String fileName;
    private User createdByUser;
    private int subjectId;
    private int createdById;
    private String materialForRole;

    public Material() {
    }

    public Material(int materialId, String materialName, Subject subject, String materialDescription, String fileName, User createdByUser, int subjectId, int createdById, String materialForRole) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.subject = subject;
        this.materialDescription = materialDescription;
        this.fileName = fileName;
        this.createdByUser = createdByUser;
        this.subjectId = subjectId;
        this.createdById = createdById;
        this.materialForRole = materialForRole;
    }

    public String getMaterialForRole() {
        return materialForRole;
    }

    public void setMaterialForRole(String materialForRole) {
        this.materialForRole = materialForRole;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

   
}
