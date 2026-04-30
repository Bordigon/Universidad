package cursos;
import java.sql.*;

public class DropTable implements DataBaseTask {
    
	private String table;
	public DropTable(String data){
		this.table = data;
	}

    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("DROP TABLE "+table);
            st.close();
        }
        catch (SQLException e) {
            throw e;
        }
        catch(Exception e) {
            throw new BBDDException(e, "");
        }

    }
}