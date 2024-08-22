
package dal.interfaces;

import java.util.List;
import models.ClassRoom;
import models.User;

public interface IClassDAO {

    public List<ClassRoom> getClassList();

    public ClassRoom getClassById(int classId);

    public ClassRoom isCharged(int teacherId);

    public List<User> getRegularTeacherList(int teacherId);

    public int SaveClass(ClassRoom updateClass);

    public int DeleteClass(int classId);

    public List<ClassRoom> getClassInCharge(int userId);

    public Boolean isClassNameExisted(String className, int year, int classId);

}
