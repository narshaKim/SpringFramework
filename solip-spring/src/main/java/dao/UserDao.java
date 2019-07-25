package dao;

import domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import strategy.AddStatement;
import strategy.DeleteAllStatement;
import strategy.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        StatementStrategy strategy = new AddStatement(user);
        jdbcContextWithStatementStrategy(strategy);
    }

    public User get(String id) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("SELECT * FROM TB_USER WHERE id=?");
            ps.setString(1, id);
            rs = ps.executeQuery();
            if(rs.next()) {
                String rs_id = rs.getString("id");
                String rs_name = rs.getString("name");
                String rs_password = rs.getString("password");
                user = new User(rs_id, rs_name, rs_password);
            }
            if(user==null)
                throw new EmptyResultDataAccessException(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if(ps!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c!=null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }

        return user;
    }

    public void deleteAll() throws SQLException {
        StatementStrategy strategy = new DeleteAllStatement();
        jdbcContextWithStatementStrategy(strategy);
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = strategy.makePreparedStstement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c!=null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = -1;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("SELECT COUNT(*) FROM TB_USER");
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if(ps!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c!=null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }

        return count;
    }

}
