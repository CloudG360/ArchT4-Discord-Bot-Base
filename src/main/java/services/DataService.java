package main.java.services;

import com.mysql.cj.jdbc.MysqlDataSource;

import main.java.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


public class DataService {

    public ResultSet retriveEntry(String db, String table, String id){
        MysqlDataSource datSource = getDataSource();
        if(datSource == null){
            return null;
        }

        Connection con = null;

        try {
            con = datSource.getConnection();

            //Process Data

            Statement statement = con.createStatement();

            String sql = "SELECT * FROM " + db + "." + table + " WHERE id="+id;

            ResultSet results = statement.executeQuery(sql);

            con.close();

            return results;
        } catch (Exception err){
            return null;
        }


        //There will be a return statement.
    }

    public boolean editEntry(String db, String table, String id, String key, String val){
        MysqlDataSource datSource = getDataSource();
        if(datSource == null){
            return false;
        }

        Connection con = null;

        try {
            con = datSource.getConnection();

            //Process Data

            Statement statement = con.createStatement();

            String sql = "UPDATE " +db+"."+table + " SET "+key+"="+val+" WHERE id="+id;

            statement.execute(sql);

            con.close();

        } catch (Exception err){
            return false;
        }

        return true;
        //There will be a return statement.
    }

    public boolean insertEntry(String db, String table, String id, List<String> columnNames, List<String> values){
        MysqlDataSource datSource = getDataSource();
        if(datSource == null){
            return false;
        }

        Connection con = null;

        try {
            con = datSource.getConnection();

            //Process Data

            Statement statement = con.createStatement();

            String sql = "INSERT INTO "+db+"."+table+" (id";

            for (String cname: columnNames) {
                sql = sql.concat(", "+cname);
            }

            sql = sql.concat(") VALUES ('"+id+"',");

            for (String vname: values) {
                sql = sql.concat(", '"+vname+"'");
            }

            sql = sql.concat(")");

            statement.execute(sql);

            con.close();

        } catch (Exception err){
            return false;
        }

        return true;
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
