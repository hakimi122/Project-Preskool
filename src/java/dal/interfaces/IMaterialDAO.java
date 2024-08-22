
package dal.interfaces;

import java.util.List;
import models.Material;

public interface IMaterialDAO {

    List<Material> getMaterialListForRole(String role);

    public Material getMaterialById(int materialId);

    public int DeleteMaterial(int subjectId);

    public int SaveMaterial(Material materialSave);
}
