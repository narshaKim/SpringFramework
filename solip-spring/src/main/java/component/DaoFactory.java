package component;

import dao.UserDao;

public class DaoFactory {

    public UserDao userDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }

    private ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

}
