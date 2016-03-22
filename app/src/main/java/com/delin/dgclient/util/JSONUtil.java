package com.delin.dgclient.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class JSONUtil {


    public static HashMap<String,String> toHashMap(String jsonString) {
        HashMap hashMap = new HashMap<String,String>();
        try {
            Gson gson = new Gson();
             hashMap= gson.fromJson(jsonString, HashMap.class);
        } catch (Exception e) {

        }
        return hashMap;
    }

    public static ArrayList<HashMap> toArrayList(String jsonString) {
        ArrayList<HashMap> arrayList = new ArrayList<HashMap>();
        try {
            Gson gson = new Gson();
            arrayList = gson.fromJson(jsonString,
                    new TypeToken<List<HashMap>>() {
                    }.getType());
        } catch (Exception e) {
        }
        return arrayList;
    }

    public static LinkedList<HashMap<String,String>> toLinkedList(String jsonString) {
        LinkedList<HashMap<String,String>> linkedList = new LinkedList<HashMap<String,String>>();
        try {
            Gson gson = new Gson();
            linkedList = gson.fromJson(jsonString,
                    new TypeToken<List<HashMap<String, String>>>() {
                    }.getType());
        } catch (Exception e) {
        }
        return  linkedList;
    }

    public static String toJSON(HashMap map){
        Gson gson = new Gson();
        String jsonObject= gson.toJson(map);
        return jsonObject;
    }

    public static String toJSON(ArrayList arrayList){
        Gson gson = new Gson();
        String jsonObject= gson.toJson(arrayList);
        return jsonObject;
    }

}