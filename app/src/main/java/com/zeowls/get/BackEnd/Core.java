package com.zeowls.get.BackEnd;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class Core {

    Context context;


    public Core(Context context) {
        this.context = context;
    }

    private String Domain = "http://bubble-zeowls-stage.herokuapp.com";

    private String getRequest(String url) throws IOException {
        String data;
        BufferedReader reader;
        URL url1 = new URL(url);
        Log.d("url", url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "627562626c6520617069206b6579");
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.connect();

        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        return data;
    }

    private String postRequest(String url, JSONObject params) throws IOException {
        URL url1 = new URL(Domain + url);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setRequestProperty("Authorization", "627562626c6520617069206b6579");
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStream stream = connection.getOutputStream();
        DataOutputStream writer = new DataOutputStream(stream);

        Log.d("WARN", params.toString());
        // The LogCat prints out data like:
        // ID:test,Email:test@gmail.com,Pwd:test
        writer.writeBytes(params.toString());
        writer.flush();
        writer.close();
        stream.close();

        String data;
        BufferedReader reader;
        InputStream inputStream = connection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        Log.d("data", data);
        return data;
    }

    public JSONArray getShopItems(int id) throws JSONException {
        JSONArray jsonArray = null;
        try {
            String response = getRequest(Domain + "/GetShopItems/" + id + "/JSON");
            if (!response.equals("0")) {
                jsonArray = new JSONArray(response);
                //  json = jsonArray.getJSONObject(0);

//                putItemsDB(json);
            } else {
                Log.d("getShopItems", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return jsonArray;
    }

    public JSONObject getShop(int id) throws JSONException {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetShop/" + id + "/JSON");
            if (!response.equals("0")) {
                JSONArray jsonArray = new JSONArray(response);
                json = jsonArray.getJSONObject(0);
//                putShopsDB(json);
            } else {
                Log.d("getShopItems", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONObject getItem(int id) throws JSONException {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetItem/" + id + "/JSON");
            if (!response.equals("0")) {
                JSONArray jsonArray = new JSONArray(response);

                json = jsonArray.getJSONObject(0);
//                putItemsDB(json);
            } else {
                Log.d("get Items", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONObject getAllShops() {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetAllShops/JSON");
            if (!response.equals("0")) {
                json = new JSONObject(response);
//                putShopsDB(json);
            } else {
                Log.d("getAllShops", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public int getCredentials(String username, String password) throws JSONException {
        JSONObject json = new JSONObject();
        int result = 0;
        try {
            json.put("email", username);
            json.put("password", password);
            String response = postRequest("/login", json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getInt("response");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;
    }

    public int signUpFBUser(JSONObject json, String fb_token) throws JSONException {
        int result = 0;
        try {
            json.put("fb_token", fb_token);
            String response = postRequest("/FBlogin", json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getInt("response");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;
    }

    public int signUpUser(String username, String password, String mobile) throws JSONException {
        JSONObject json = new JSONObject();
        int result = 0;
        try {
            json.put("email", username);
            json.put("password", password);
            json.put("mobile", mobile);
            String response = postRequest("/signup", json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getInt("response");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject getUserCart(int userId) {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/getUserShopCart/" + userId + "/");
            if (!response.equals("0")) {
                json = new JSONObject(response);
            } else {
                Log.d("addToShopCart", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return json;
    }

    public int makeOrder(int item_id, int user_id, int quantity, String shipping_address) {

        JSONObject json = new JSONObject();
        int result = 0;
        try {
            json.put("item_id", item_id);
            json.put("user_id", user_id);
            json.put("quantity", quantity);
            json.put("shipping_address", shipping_address);
            String response = postRequest("/makeOrder", json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getInt("response");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;

    }

    public JSONObject getUserOrders(int user_id) {
        JSONObject json = new JSONObject();
        JSONObject responseJson = new JSONObject();

        try {
            json.put("user_id", user_id);
            responseJson = new JSONObject(postRequest("/getOrdersByUser", json));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJson;
    }

    public JSONObject editUserOrder(int order_id, String shipping_address) {
        JSONObject json = new JSONObject();
        JSONObject responseJson = new JSONObject();

        try {
            json.put("order_id", order_id);
            json.put("shipping_address", shipping_address);
            responseJson = new JSONObject(postRequest("/editOrdersByUser", json));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJson;
    }

    public JSONObject registerDevice(int user_id, String token) {
        JSONObject json = new JSONObject();
        JSONObject responseJson = new JSONObject();
        try {
            json.put("user_id", user_id);
            json.put("device_token", token);
            responseJson = new JSONObject(postRequest("/registerDevice", json));
            Log.d("registerDeviceToken", token + "User:" + user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJson;

    }


    public JSONArray getHomePage() {
        JSONArray json = null;
        try {
            String response = getRequest(Domain + "/HomePage/JSON");
            if (!response.equals("0")) {
                json = new JSONArray(response);
            } else {
                Log.d("get Items By Cat id", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }


}
