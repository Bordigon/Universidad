package entregas.base_de_datos.pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import entregas.Env;
import java.time.LocalDate;
import java.sql.Date;

public class Main{

	private Connection conn;
	private String host = "localhost:3306";
	private String db = "sakila";
	private String user = Env.getUser();
	private String pass = Env.getPass();
	private String url = "jdbc:mysql://" + host + "/" + db;

	public Main(){
		try{
			conn = DriverManager.getConnection(url, user, pass);
			System.out.println("Conectaod a la base de datos");                  
		}catch(SQLException e){
			System.err.println("Error: " + e.getMessage());
		}
	}


	private void ejemploConsulta2() throws SQLException{
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM ACTOR");   //obtener información de la base de datos
		System.out.println("Consulta ejecutada");
		System.out.println("ID " +"nombre");
		while(rs.next()){					//rs.next() va de fila en fila
			int id = rs.getInt("actor_id");			//rs.getInt()
			String nombre = rs.getString("first_name");	//rs.getString()
			System.out.println(id+" " + nombre);
		}
		st.close();
		rs.close();

	}

	

	private void ejemploPSBasico(int param) throws SQLException{

		PreparedStatement pst = conn.prepareStatement("SELECT * FROM ACTOR WHERE actor_id <= ?");
		pst.setInt(1, param);
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			int id = rs.getInt(1);
			String nombre = rs.getString(2);
			System.out.println(id+" " + nombre);
		
		}
		
		pst.close();
		rs.close();	

	}
	
	private String[] frase(String xd){
		String[] result = xd.split(",");
		System.out.println(result[0].trim() + result[1].trim());
		return result;
	}

	private LocalDate fecha(){
		LocalDate f = LocalDate.of(1,1,1);

		System.out.println(f);
		return f;
	
	}


	public static void main(String[] args){
		Main p = new Main();

		/*
		try{p.ejemploConsulta2();
		}catch(SQLException e){System.out.println("Error: " + e.getMessage());}
		

		try{p.ejemploPSBasico(5);}catch(SQLException e){System.out.println("Error: " + e.getMessage());}
		*/

		p.frase("car, jeep, scooter");
		p.fecha();

	}
}





