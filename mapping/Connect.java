package mapping;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
    public Connection connectPostgres() throws Exception {
        Connection con = null;
        try{
            if(con == null){
                Class.forName("org.postgresql.Driver");
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prevcoupure", "postgres", "root");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }



        return con;
    }
}
