package dao;

import domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {

    JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper = new RowMapper<User>() {
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("password"));
            return user;
        }
    };

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
        jdbcTemplate.update("INSERT INTO TB_USER(ID, NAME, PASSWORD) VALUES(?,?,?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(final String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM TB_USER WHERE id = ?", new Object[]{id}, this.userMapper);
    }

    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM TB_USER", this.userMapper);
        return users;
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM TB_USER");
    }

    public long getCount() {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) AS COUNT FROM TB_USER", Integer.class);
        return result;
    }

}
