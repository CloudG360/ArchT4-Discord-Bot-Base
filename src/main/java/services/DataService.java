package main.java.services;

import main.java.Main;

import java.sql.*;
import java.util.List;


public class DataService {

    private Connection con = null;

    public DataService (String dbname){
        connectToDB(dbname);
    }

    public ResultSet retriveEntry(String table, String id){

        if(con == null){
            return null;
        }

        try {

            //Process Data

            Statement statement = con.createStatement();

            String sql = "SELECT * FROM "+ table + " WHERE id='"+id+"';";

            Main.getResources().coreService.SendInfoToHome("SQL DB INTERACTION", sql, "---");
            ResultSet results = statement.executeQuery(sql);


            return results;
        } catch (Exception err){
            return null;
        }
    }

    public boolean editEntry(String table, String id, String key, String val){
        if(con == null){
            return false;
        }

        try {

            //Process Data

            Statement statement = con.createStatement();

            String sql = "UPDATE "+table+" SET "+key+"='"+val+"' WHERE id='"+id+"';";

            Main.getResources().coreService.SendInfoToHome("SQL DB INTERACTION", sql, "---");
            statement.execute(sql);


        } catch (Exception err){
            return false;
        }

        return true;
        //There will be a return statement.
    }

    public boolean insertEntry(String table, String id, List<String> columnNames, List<String> values){
        if(con == null){
            return false;
        }


        try {

            //Process Data

            Statement statement = con.createStatement();

            String sql = "INSERT INTO "+table+" (id";

            for (String cname: columnNames) {
                sql = sql.concat(", "+cname);
            }

            sql = sql.concat(") VALUES ('"+id+"'");

            for (String vname: values) {
                sql = sql.concat(", '"+vname+"'");
            }

            sql = sql.concat(");");
            Main.getResources().coreService.SendInfoToHome("SQL DB INTERACTION", sql, "---");

            statement.execute(sql);


        } catch (Exception err){
            return false;
        }

        return true;
        //There will be a return statement.
    }

    public void connectToDB(String dbname){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException err){
            Main.getResources().coreService.SendErrorToHome("DBAccess Error", "ClassNotFound: com.mysql.jdbc.Driver", "connectToDB() - DataService");
            return;
        }

        String username = Main.getResources().botAdministratorConfig.get("sql-username").toString();
        String password =Main.getResources().botAdministratorConfig.get("#sql-password").toString();
        String ip = Main.getResources().botAdministratorConfig.get("#sql-servername").toString();


        try {
            con = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+dbname, username, password);
        } catch (SQLException err) {
            Main.getResources().coreService.SendErrorToHome("DBAccess Error", "An SQLException occurred while connecting to a DB", "connectToDB() - DataService");
        }

    }

    public void cleanup(){
        try {
            con.close();
        } catch (SQLException err){
            Main.getResources().coreService.SendErrorToHome("DBClose Error", "An SQLException occurred while closing a connection to a DB", "cleanup() - DataService");

        }
    }

}
