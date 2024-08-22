
package dal.interfaces;

import java.util.List;
import models.User;

public interface IUserDAO {

    User login(String userName, String passWord);

    public void updateProfile(User user);

    public int changePassword(int id, String newPassword, String historyPassword);

    public List<User> getTeacherList();

    public int SaveUser(String type, User user);

    public User checkExist(String column, String value, int userid);

    public int DeleteUser(int userId);

    public User getUserById(int userId);

    public List<User> getStudentList();

}
