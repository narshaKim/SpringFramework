package component;

import strategy.ResultSetStrategy;
import strategy.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcContext {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException {
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

    public Object workWithStatementStrategy(StatementStrategy strategy, ResultSetStrategy resultSetStrategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Object result = null;

        try {
            c = dataSource.getConnection();
            ps = strategy.makePreparedStstement(c);
            rs = ps.executeQuery();
            result = resultSetStrategy.getResult(rs);
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

        return result;
    }

    public void executeSql(final String query, final Object... parameters) throws SQLException {
        workWithStatementStrategy(new StatementStrategy() {
            public PreparedStatement makePreparedStstement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement(query);
                int idx = 0;
                for(Object object : parameters) {
                    String str = String.valueOf(object);
                    if(object instanceof String) {
                        ps.setString(++idx, str);
                    }
                    else if(object instanceof Integer) {
                        ps.setInt(++idx, Integer.parseInt(str));
                    }
                    else if(object instanceof Long) {
                        ps.setLong(++idx, Long.parseLong(str));
                    }
                    else if(object instanceof Double) {
                        ps.setDouble(++idx, Double.parseDouble(str));
                    }
                    else if (object instanceof Float) {
                        ps.setFloat(++idx, Float.parseFloat(str));
                    }
                    else if(object instanceof Boolean) {
                        ps.setBoolean(++idx, Boolean.parseBoolean(str));
                    }
                }
                return ps;
            }
        });
    }
}
