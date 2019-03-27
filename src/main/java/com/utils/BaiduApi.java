package com.utils;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018-11-14 0014.
 */
public class BaiduApi {
    /**
     * 百度地图通过经纬度来获取地址,传入参数纬度lat、经度lng
     */
    public static void getAddress(String Lat,String Lng) {

        try {
            String url = "http://api.map.baidu.com/geocoder/v2/?location="+Lng+","+Lat +
                    "&output=json&pois=1&ak=bUkWOrg4N9nsgbm6O27sKfBGF2jcNNp5";
            String json = loadJSON(url);
            JSONObject obj = JSONObject.fromObject(json);
            if(obj.get("status").toString().equals("0")){
                String address=obj.getJSONObject("result").getString("formatted_address");
                System.out.println("地址：" + address);
            }else{
                System.out.println("未找到相匹配的地址！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 百度地图通过地址来获取经纬度，传入参数address
     * @param address
     * @return
     */
    public static Map<String,Double> getLngAndLat(String address){
        Map<String,Double> map=new HashMap<String, Double>();
        String url = "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=bUkWOrg4N9nsgbm6O27sKfBGF2jcNNp5";
        String json = loadJSON(url);
        System.out.println(json);
        JSONObject obj = JSONObject.fromObject(json);
        if(obj.get("status").toString().equals("0")){
            double lng=obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
            double lat=obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
            map.put("lng", lng);
            map.put("lat", lat);
            System.out.println("经度：" + lng + "--- 纬度：" + lat);
        }else{
            System.out.println("未找到相匹配的经纬度！");
        }
        return map;
    }
    public static String loadJSON (String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
        }
        return json.toString();
    }

    public static void main(String[] args) {
        long num = 16463;
        int sss = 16389;
        System.out.println();
        BaiduApi.getLngAndLat("江苏省苏州市高新区金山路131号");
//        while(true){
////            BaiduApi.getLngAndLat("江苏省苏州市高新区金山路130号");
//            BaiduApi.getAddress("120.53103559545453","31.299955993414333");
//            num ++;
//            System.out.println(num);
//
//
//        }
    }
}
