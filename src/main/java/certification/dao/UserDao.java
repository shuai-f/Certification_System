package certification.dao;


import certification.pojo.User;

public interface UserDao {
    public User findUserById(int id);
}
