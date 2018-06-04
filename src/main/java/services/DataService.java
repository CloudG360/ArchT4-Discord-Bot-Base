package main.java.services;

import com.mysql.cj.jdbc.MysqlDataSource;

import main.java.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class DataService {

    public Object retriveEntry(String db, String table, String id, String columnRequest){
        MysqlDataSource datSource = getDataSource();
        if(datSource == null){
            return null;
        }

        Connection con = null;

        try {
            con = datSource.getConnection();

            //Process Data

            Statement statement = con.createStatement();

            String sql = "SELECT id, " + columnRequest + " FROM " + db + "." + table + "WHERE id="+id;

            ResultSet results = statement.executeQuery(sql);

            Object returnVal = results.getObject(columnRequest);

            con.close();

            return returnVal;
        } catch (Exception err){
            return null;
        }


        //There will be a return statement.
    }

    public MysqlDataSource getDataSource(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(Main.getResources().botAdministratorConfig.get("sql-username").toString());
        dataSource.setPassword(Main.getResources().botAdministratorConfig.get("#sql-password").toString());
        try {
            dataSource.setAutoReconnect(true);
        } catch (Exception err){
            return null;
        }


        dataSource.setServerName(Main.getResources().botAdministratorConfig.get("sql-servername").toString());
        return dataSource;
    }

}
