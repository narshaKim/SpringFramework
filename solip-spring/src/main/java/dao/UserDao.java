package dao;

import domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) throws SQLException {
        jdbcTemplate.update("INSERT INTO TB_USER(ID, NAME, PASSWORD) VALUES(?,?,?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(final String id) throws SQLException {
        return jdbcTemplate.queryForObject("SELECT * FROM TB_USER WHERE id = ?", new Object[]{id}, new RowMapper<User>() {
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user  = new User(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("password"));
                return user;
            }
        });
    }

    public List<User> getAll() throws SQLException {
        List<User> users = jdbcTemplate.query("SELECT * FROM TB_USER", new RowMapper<User>() {
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                return new User(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("password"));
            }
        });
        return users;
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("DELETE FROM TB_USER");
    }

    public long getCount() throws SQLException {
        //queryForInt가 없음
        Integer result = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("SELECT COUNT(*) AS COUNT FROM TB_USER");
            }
        }, new ResultSetExtractor<Integer>() {
            public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                resultSet.next();
                return resultSet.getInt(1);
            }
        });
        return result;
    }

}
