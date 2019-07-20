package dao;

import domain.User;

import java.sql.*;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeNewConnection();

        PreparedStatement ps = c.prepareStatement("INSERT INTO TB_USER(ID, NAME, PASSWORD) VALUES(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        ps.executeUpdate();
        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeNewConnection();

        PreparedStatement ps = c.prepareStatement("SELECT * FROM TB_USER WHERE id=?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();

        User user = new User();
        user.setId(rs.getString("ID"));
        user.setName(rs.getString("NAME"));
        user.setPassword(rs.getString("PASSWORD"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

}
