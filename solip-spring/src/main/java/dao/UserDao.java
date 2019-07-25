package dao;

import component.JdbcContext;
import domain.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserDao {

    JdbcContext jdbcContext;

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(final User user) throws SQLException {
        jdbcContext.executeSql("INSERT INTO TB_USER(ID, NAME, PASSWORD) VALUES(?,?,?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(final String id) throws SQLException {
        List<Map<String, Object>> result = jdbcContext.querySql("SELECT * FROM TB_USER WHERE id=?", id);
        Map<String, Object> item = result.get(0);
        User user = new User(item.get("ID").toString(), item.get("NAME").toString(), item.get("PASSWORD").toString());
        return user;
    }

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("DELETE FROM TB_USER");
    }

    public long getCount() throws SQLException {
        List<Map<String, Object>> result = jdbcContext.querySql("SELECT COUNT(*) AS COUNT FROM TB_USER");
        long count = (Long)result.get(0).get("COUNT");
        return count;
    }

}
