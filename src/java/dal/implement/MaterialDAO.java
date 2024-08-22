
package dal.implement;

import context.DBContext;
import dal.interfaces.IMaterialDAO;
import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Material;
import models.Subject;
import models.User;


public class MaterialDAO extends context.DBContext implements IMaterialDAO {

    Connection connection = null;
    DBContext dBContext = new DBContext();

    @Override
    public List<Material> getMaterialListForRole(String role) {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT m.*, s.subjectId, s.subjectName, u.userId, "
                + "u.fullName, u.email FROM Material m "
                + "JOIN Subject s ON m.subjectId = s.subjectId "
                + "JOIN [User] u ON m.createdBy = u.userId ";
        if (!role.equals("headmaster")) {
            sql += " where materialForRole = ?";
        }
        try {
            connection = openConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            if (!role.equals("headmaster")) {
                ps.setString(1, role);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Material material = new Material();
                material.setMaterialId(rs.getInt("materialId"));
                material.setMaterialName(rs.getString("materialName"));
                material.setMaterialDescription(rs.getString("materialDescription"));
                material.setFileName(rs.getString("fileName"));
                material.setMaterialForRole(rs.getString("materialForRole"));

                // Get and set Subject
                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("subjectId"));
                subject.setSubjectName(rs.getString("subjectName"));
                material.setSubject(subject);

                // Get and set User
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                material.setCreatedByUser(user);

                materials.add(material);
            }
            return materials;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @Override
    public Material getMaterialById(int materialId) {
        String sql = "SELECT m.*, s.subjectId, s.subjectName, u.userId, u.fullName, u.email "
                + " FROM Material m "
                + "JOIN Subject s ON m.subjectId = s.subjectId "
                + "JOIN [User] u ON m.createdBy = u.userId where materialId = ?";
        try {
            connection = openConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            Material material = new Material();

            if (rs.next()) {
                material.setMaterialId(rs.getInt("materialId"));
                material.setMaterialName(rs.getString("materialName"));
                material.setMaterialDescription(rs.getString("materialDescription"));
                material.setFileName(rs.getString("fileName"));
                material.setMaterialForRole(rs.getString("materialForRole"));

                // Get and set Subject
                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("subjectId"));
                subject.setSubjectName(rs.getString("subjectName"));
                material.setSubject(subject);

                // Get and set User
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                material.setCreatedByUser(user);
            }
            return material;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @Override
    public int DeleteMaterial(int subjectId) {
        int numberOfChange = 0;
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "Delete Material WHERE materialId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, subjectId);
            numberOfChange = st.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
            }
        }
        return numberOfChange;
    }

    @Override
    public int SaveMaterial(Material materialSave) {
        int numberOfChange = 0;
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "";
        if (materialSave.getMaterialId() != -1) { //update
            sql = "Update Material set materialName = ?, subjectId = ?, materialDescription = ?, fileName = ?, createdBy = ?, materialForRole = ? where materialId = ?";
        } else {
            sql = "Insert into Material(materialName, subjectId, materialDescription, fileName, createdBy,materialForRole) values(?,?,?,?,?,?)";
        }
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            int count = 0;
            st.setString(++count, materialSave.getMaterialName());
            st.setInt(++count, materialSave.getSubjectId());
            st.setString(++count, materialSave.getMaterialDescription());
            st.setString(++count, materialSave.getFileName());
            st.setInt(++count, materialSave.getCreatedById());
            st.setString(++count, materialSave.getMaterialForRole());

            if (materialSave.getMaterialId() != -1) {
                st.setInt(++count, materialSave.getMaterialId());
            }
            numberOfChange = st.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return numberOfChange;
    }
}
