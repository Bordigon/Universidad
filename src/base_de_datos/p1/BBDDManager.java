package entregas.base_de_datos.p1;

import java.sql.*;

public class BBDDManager {
	private String host = "localhost:3306";
	private String db = "cursos_db";
	private String user = "";
	private String pass = "";
	private String url = "";
	private Connection conn;

    public BBDDManager(String user, String password)  {
    	this.user = user;
    	this.pass = password;
    }
    
    private void conectar(StringWriter result) {
    	url = url();
    	try {
    		this.conn = DriverManager.getConnection(url, user, pass);
    	}
    	catch (SQLException e){
    		result.add("Connection:" + e.getMessage() + ";");
    	}
    }

    public String url() {
    	return "jdbc:mysql://" + host + "/" + db;
    }
    public StringWriter run(DataBaseTask[] tasks, String[] dataArray, boolean autoCommit) {
        StringWriter result = new StringWriter();
        conectar(result);
        //autocomit
        try {
        	this.conn.setAutoCommit(autoCommit);
        }
        catch (SQLException e){
    		result.add("Connection:" + e.getMessage() + ";");
    	}
        //tareas
        for (int i = 0; i<tasks.length; i++) {
        	try {
        		tasks[i].run(this.conn, dataArray[i]);
        	}
        	catch (BBDDException e) {
        		result.add("Task:" + e.when() + ";" + e.getMessage() + ";");
        		try {
        			this.conn.commit();
        		}
        		catch (Exception x){
        			result.add("Otro:" + x.getMessage() + ";");
        		}
        	}
        	catch (SQLException e) {
        		result.add("SQL:" + e.getMessage() + ";");
        		try {
        			this.conn.rollback();
        		}
        		catch (Exception x){
        			result.add("Otro:" + x.getMessage() + ";");
        		}
        	}
        	catch (Exception e) {
        		result.add("Otro:" + e.getMessage() + ";");
        	}
        	
        }
        try {
        	this.conn.close();
        }
        catch (SQLException e) {
            result.add("Connection:" + e.getMessage() + ";");
        }
        catch (Exception e) {
            result.add("Otro:" + e.getMessage() + ";");
        }
        result.add("fin");
        return result;
    }
}
