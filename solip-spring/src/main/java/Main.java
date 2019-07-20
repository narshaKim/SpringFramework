import dao.ConnectionMaker;
import dao.DConnectionMaker;
import dao.UserDao;
import domain.User;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        User user = new User();
        user.setId("user");
        user.setName("솔잎");
        user.setPassword("thfdlv");

        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        userDao.add(user);
        System.out.println(user.getId() + " : 등록 성공");

        User storedUser = userDao.get(user.getId());
        System.out.println(user.getName() + " : 조회 성공");
    }

}
