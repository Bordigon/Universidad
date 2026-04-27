package entregas.base_de_datos.p1;
import java.sql.*;

public class CreateTable implements DataBaseTask {
    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE imparte (profesor_id INT NOT NULL, curso_id INT NOT NULL, n_modulo INT NOT NULL, aula_id INT NOT NULL, fecha DATE NOT NULL, PRIMARY KEY (profesor_id, curso_id, n_modulo, aula_id, fecha), FOREIGN KEY (profesor_id) REFERENCES profesor(id), FOREIGN KEY (curso_id, n_modulo) REFERENCES modulo(curso_id, n_modulo), FOREIGN KEY (aula_id) REFERENCES aula(id))");
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