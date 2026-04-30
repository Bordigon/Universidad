package cursos;

import java.sql.*;
import java.time.LocalDate;

public class InsertaUnaFilaImparte implements DataBaseTask {
/* *
* Inserta los datos proporcionados en una nueva fila de la tabla ` imparte `.
*
* ATENCION : para poner la fecha se recomienda usar :
* - java . time . LocalDate . of
* Para crear una LocalDate con year , month , day
* - java . sql . Date . valueof
* Para transformarla en Date
*
* @param conn La conexion ya abierta
* @param data Los datos a insertar en el formato : "7 , 3 , 2 , 4 , 14/03/2025" ,
* es decir , los datos pueden llevar espacios antes o despues .
* Cada valor es respectivamente el valor de las columnas :
* profesor_id , curso_id , n_modulo , aula_id , fecha .
*
* @throws BBDDException , si se produce cualquier error durante
* el proceso de los datos del archivo se debe fijar
* ` when ` a " Insertando "
* @throws SQLException , cuando se produzca la misma al ejecutar la
* insercion o si la ejecucion del comando no retorna 1.
*/

    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        //conn -> conexion abierta
        //data --> datos a insertar ("3,2 , 3,4, 14/03/2025") || profesor_id , curso_id , n_modulo , aula_id , fecha .
        //Aqui hay peligro de SQL injection porque se meten datos desde fuera a la DB, por lo que uso un prepared statement


        try {
            //Primero parseo el data: (profesor_id , curso_id , n_modulo , aula_id , fecha)
            String[] resultParse = data.split(",");

            //a int los 4 primeros
            int profesorId = Integer.parseInt(resultParse[0].trim());
            int cursoId = Integer.parseInt(resultParse[1].trim());
            int nModulo = Integer.parseInt(resultParse[2].trim());
            int aulaId = Integer.parseInt(resultParse[3].trim());
            //Y la fecha como array de Strings
            String[] fecha = resultParse[4].trim().split("/"); //divido la fecha en dia mes y anio
            //y ahora puedo parsear por int de nuevo
            int dia = Integer.parseInt(fecha[0].trim()); 
            int mes = Integer.parseInt(fecha[1].trim()); 
            int anio = Integer.parseInt(fecha[2].trim()); 
            // 
            LocalDate fechalocal  = LocalDate.of(anio, mes, dia);
            Date fechaSql = Date.valueOf(fechalocal);






            //try dentro del try para cerrar el prepStatement
            try (PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO imparte VALUES (?, ?, ?, ?, ?)")){

            

                prepStatement.setInt(1, profesorId);
                prepStatement.setInt(2, cursoId);
                prepStatement.setInt(3, nModulo);
                prepStatement.setInt(4, aulaId);
                prepStatement.setDate(5, fechaSql);
                int filas = prepStatement.executeUpdate();
                if (filas != 1) {
                    throw new SQLException("");
                }
            }
        } 
        catch (SQLException e) {
            throw e;          
        } 
        catch (Exception e) {
            throw new BBDDException(e, "Insertando");
        }



    }
}
