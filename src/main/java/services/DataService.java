package main.java.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import main.java.Main;
import main.java.Resources;
import org.json.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        dataSource.setUser(Main.getResources().botAdministratorConfig.get("sql-username"));
        dataSource.setPassword(Main.getResources().botAdministratorConfig.get("#sql-password"));
        try {
            dataSource.setAutoReconnect(true);
        } catch (Exception err){
            return null;
        }


        dataSource.setServerName(Main.getResources().botAdministratorConfig.get("sql-servername"));
        return dataSource;
    }

}
