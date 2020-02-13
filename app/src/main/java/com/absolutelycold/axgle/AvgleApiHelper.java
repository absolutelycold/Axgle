package com.absolutelycold.axgle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public  class AvgleApiHelper {


    public static final Integer CONNECT_NORMAL = 200;





    public static String getURLContent(String urlStr) {


        URL url = null;
        HttpURLConnection httpConn = null;
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(urlStr);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            httpConn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            httpConn.setConnectTimeout(10000);


            int collection_code = httpConn.getResponseCode();
            System.out.println("Code:" + collection_code);
            if (collection_code == CONNECT_NORMAL) {
                in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf8"));
            }
            else {
                return null;
            }


            String str = null;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
            in.reset();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                return null;
            }
        }

        String result = sb.toString();
        System.out.println("Result is : " + result);
        return result;
    }

    public static ArrayList<HashMap<String, Object>> allVideosCollection(int page, int limit) {

        int collectionsAIPGetCode;
        int coverGetCode;
        ArrayList<HashMap<String,Object>> collectionsInfo = new ArrayList<HashMap<String,Object>>();

        String url = "https://api.avgle.com/v1/collections/" + page + "?limit=" + limit;



        try {
            String content = getURLContent(url);
            if (content == null) {
                collectionsAIPGetCode = 404;
                return null;
            }
            JSONObject json = new JSONObject(content);
            JSONObject response = json.getJSONObject("response");
            JSONArray collections = response.getJSONArray("collections");

            for (int i = 0; i < collections.length(); i++) {
                JSONObject collect = collections.getJSONObject(i);
                String imgUrl = collect.getString("cover_url");
                String title = collect.getString("title");
                String keyword = collect.getString("keyword");
                Integer id = collect.getInt("id");
                Integer totalViews = collect.getInt("total_views");
                Integer videoCounts = collect.getInt("video_count");
                //Bitmap bitmap = getImageBitmap(imgUrl);

                HashMap hashMap = new HashMap();
                hashMap.put("id", id);
                hashMap.put("title", title);
                hashMap.put("keyword", keyword);
                hashMap.put("cover_url", imgUrl);
                //hashMap.put("cover_bitmap", bitmap);
                hashMap.put("total_views", totalViews);
                hashMap.put("video_count", videoCounts);
                collectionsInfo.add(hashMap);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return collectionsInfo;
    }

    public static HashMap<String, Object> getImageBitmap(String imageUrl) {

        HashMap<String, Object> bitmapBundle = new HashMap<>();
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            int current_collection_cover_code = connection.getResponseCode(); // save status to bitmapBundle
            System.out.println("Vising Statuscode:" + current_collection_cover_code);
            bitmapBundle.put("code", current_collection_cover_code);
            if (current_collection_cover_code == CONNECT_NORMAL) {
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bitmapBundle.put("bitmap", bitmap);
                return bitmapBundle;
            }
            else {
                bitmapBundle.put("bitmap", null);
                return bitmapBundle;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
