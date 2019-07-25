package strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy {

    public PreparedStatement makePreparedStstement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("DELETE FROM TB_USER");
        return ps;
    }

}
