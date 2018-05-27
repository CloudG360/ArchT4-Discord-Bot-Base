package main.java.services;

import com.mysql.cj.jdbc.MysqlDataSource;

import main.java.Main;

import java.sql.Connection;


public class DataService {

    public void retriveEntry(String db, String table, String id, String columnRequest){
        MysqlDataSource datSource = getDataSource();
        if(datSource == null){
            return;
        }

        Connection con = null;

        try {
            con = datSource.getConnection();

            //Process Data

            con.close();
        } catch (Exception err){

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
