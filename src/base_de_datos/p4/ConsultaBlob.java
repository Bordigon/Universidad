package cursos;

import java.sql.*;
import java.io.*;
import java.util.*;

public class ConsultaBlob implements DataBaseTask {

    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {

        // Si data viene vacio no podemos crear el archivo ".jpg" de forma util,
        // se considera un error de archivo
        if (data == null || data.isEmpty()) {
            throw new BBDDException(new IOException("filtro vacio"), "error archivo");
        }

        String sql = "SELECT foto FROM edificio WHERE nombre = ? ORDER BY id ASC LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("no existe");
                }

                Blob blob = rs.getBlob(1);
                long longitud = blob.length();
                byte[] bytes = blob.getBytes(1, (int) longitud);

                try {
                    try (FileOutputStream fos = new FileOutputStream(data + ".jpg")) {
                        fos.write(bytes);
                    }
                } catch (IOException ioe) {
                    throw new BBDDException(ioe, "error archivo");
                }
            }
        }
    }
}