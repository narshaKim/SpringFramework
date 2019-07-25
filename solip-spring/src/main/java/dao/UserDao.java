package dao;

import component.JdbcContext;
import domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import strategy.ResultSetStrategy;
import strategy.StatementStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    JdbcContext jdbcContext;

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(final User user) throws SQLException {

        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            public PreparedStatement makePreparedStstement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("INSERT INTO TB_USER(ID, NAME, PASSWORD) VALUES(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
    }

    public User get(final String id) throws SQLException {
        StatementStrategy strategy = new StatementStrategy() {
            public PreparedStatement makePreparedStstement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("SELECT * FROM TB_USER WHERE id=?");
                ps.setString(1, id);
                return ps;
            }
        };
        ResultSetStrategy resultSetStrategy = new ResultSetStrategy() {
            public Object getResult(ResultSet rs) throws SQLException {
                User user = null;
                if(rs.next()) {
                    String rs_id = rs.getString("id");
                    String rs_name = rs.getString("name");
                    String rs_password = rs.getString("password");
                    user = new User(rs_id, rs_name, rs_password);
                }
                if(user==null)
                    throw new EmptyResultDataAccessException(1);
                return user;
            }
        };
        User user = (User) jdbcContext.workWithStatementStrategy(strategy, resultSetStrategy);
        return user;
    }

    public void deleteAll() throws SQLException {
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            public PreparedStatement makePreparedStstement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("DELETE FROM TB_USER");
                return ps;
            }
        });
    }

    public int getCount() throws SQLException {

        StatementStrategy strategy = new StatementStrategy() {
            public PreparedStatement makePreparedStstement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM TB_USER");
                return ps;
            }
        };

        ResultSetStrategy resultSetStrategy = new ResultSetStrategy() {
            public Object getResult(ResultSet rs) throws SQLException {
                Integer result = null;
                if(rs.next()) {
                    result = rs.getInt(1);
                }
                else {
                    throw new EmptyResultDataAccessException(1);
                }
                return result;
            }
        };

        int count = (Integer) jdbcContext.workWithStatementStrategy(strategy, resultSetStrategy);

        return count;
    }

}
