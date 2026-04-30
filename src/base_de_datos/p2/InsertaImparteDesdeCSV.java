package cursos;

import java.sql.*;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;
public class InsertaImparteDesdeCSV implements DataBaseTask {
    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        String sql = "INSERT INTO imparte VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement prepSt = conn.prepareStatement(sql); Scanner scanner = new Scanner(new FileInputStream(data)); ){

            try {
                while (scanner.hasNextLine()) {
                    String linea = scanner.nextLine();
                    if (linea.trim().isEmpty() || linea == null)
                        continue;

                    try {
                    String[] argumentos = linea.split(",");

                    prepSt.setInt(1, Integer.parseInt(argumentos[0].trim()));
                    prepSt.setInt(2, Integer.parseInt(argumentos[1].trim()));
                    prepSt.setInt(3, Integer.parseInt(argumentos[2].trim()));
                    prepSt.setInt(4, Integer.parseInt(argumentos[3].trim()));
                    prepSt.setDate(5, Date.valueOf(argumentos[4].trim()));
                    }
                    catch (Exception e) {
                        throw new BBDDException(e, "Insertando");

                    }
                    if (prepSt.executeUpdate() != 1){
                        throw new SQLException("");
                    }



                }

            }
            catch (SQLException e){
                throw e;
            }
            catch (Exception e){
                throw new BBDDException(e, "Insertando"); 
            } 
        }

        catch (SQLException e){
            throw e;
        }
        catch (Exception e){
            throw new BBDDException(e, "Insertando"); 
        }
    }
}