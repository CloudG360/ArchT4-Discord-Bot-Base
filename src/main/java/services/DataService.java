package main.java.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    private JsonObject requestServerData(String serverID, String path){
        /*
        * Plan - https://docs.google.com/spreadsheets/d/1Z-0bdDlv1vF0jKCfcSb09BsKElIwmprIZZDePXKYskE/edit?usp=sharing
        * The location should be the Server ID and the path should be a path to the data, for example:
        * UniqueUserData.PHistory (Returns as list)[0]["reason"]    or...
        * Roles.Moderator.Users (List)
        */

        String jsonContent = "";

        File serverDataLoc = new File("server/" + serverID);
        if(serverDataLoc.exists()) {
            try {
                FileReader read = new FileReader(serverDataLoc);
                BufferedReader reader = new BufferedReader(read);
                jsonContent = reader.readLine();

            } catch (Exception err) {
                return null;
            }
        } else {
            return null;
        }

        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(jsonContent, JsonElement.class).getAsJsonObject();

        String[] pathSplit = path.split(".");
        JsonObject lastObj = jsonObject;

        for(String str:pathSplit){
            try {
                lastObj = lastObj.get(str).getAsJsonObject();
            } catch (Exception err){
                return null;
            }
        }

        //Replace with result.
        return lastObj;
    }

    public String requestStringFromServer(String serverID, String path){
        JsonObject jsonobj = requestServerData(serverID, path);
        String string = null;
        try {
            string = jsonobj.getAsString();
        } catch (Exception err){
            return null;
        }
        return string;
    }

    public List<Object> requestListFromServer(String serverID, String path){
        JsonObject jsonobj = requestServerData(serverID, path);
        List<Object> list = null;
        try {
            list = new ArrayList<>();
            for (int i = 0; i < jsonobj.getAsJsonArray().size(); i++) {
                list.add(((Object)jsonobj.getAsJsonArray().get(i)));
            }
        } catch (Exception err){
            return null;
        }
        return list;
    }

}
