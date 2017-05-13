package com.example.mrwen.otherclass;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fate on 2017/2/23.
 */

public class RegionDAO {
    private static String GET_PROVINCES = "SELECT provinceID,province FROM hat_province";
    private static String GET_CITIES = "SELECT cityID,city FROM hat_city WHERE father = ?";
    private static String GET_AREAS = "SELECT area FROM hat_area WHERE father = ?";
    private static String filePath = "data/data/com.example.mrwen.teacher/files/region.db";
     private static SQLiteDatabase database ;
             static {
               database = SQLiteDatabase.openDatabase(filePath,null,SQLiteDatabase.OPEN_READONLY);
             }
    public static List<Map<String, String>> getProvinces(){
        Cursor cursor = database.rawQuery(GET_PROVINCES, null);
        List<Map<String,String>> provinces = new ArrayList<>();
        while (cursor.moveToNext()){
            Map<String,String> map = new HashMap<>();
            String id = cursor.getString(0);
            String province = cursor.getString(1);
            map.put("id",id);
            map.put("name",province);
            provinces.add(map);
        }
        cursor.close();
        return provinces;
    }
    public static List<Map<String, String>> getCityByProvince(String provinceID){
        Cursor cursor = database.rawQuery(GET_CITIES, new String[]{provinceID});
        List<Map<String,String>> cities = new ArrayList<>();
        while (cursor.moveToNext()){
            Map<String,String> map = new HashMap<>();
            String id = cursor.getString(0);
            String city = cursor.getString(1);
            map.put("id",id);
            map.put("name",city);
            cities.add(map);
        }
        cursor.close();
        return cities;
    }
    public static List<String> getAreaByCity(String cityID){
        Cursor cursor = database.rawQuery(GET_AREAS, new String[]{cityID});
        List<String> areas = new ArrayList<>();
        while (cursor.moveToNext()){
            String area = cursor.getString(0);
            areas.add(area);
        }
        cursor.close();
        return areas;
    }
}
