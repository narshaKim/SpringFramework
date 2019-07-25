package strategy;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetStrategy {

    public Object getResult(ResultSet rs) throws SQLException;

}
