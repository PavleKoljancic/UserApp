package com.example.userapp.token;

import com.example.userapp.datamodel.CacheLayer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.util.Base64;
import java.util.Date;
import java.util.HashSet;


public class TokenManager {

    private static TokenManager tokenManager = null;
    private  static JsonParser jsonParser = new JsonParser();
    private JsonObject payload=null;
    private String token = null;


    private TokenManager(String token) throws JSONException {

        this.token = token;
        initToken();

    }

    private void initToken() {
        if(token !=null) {

            Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        this.payload = (JsonObject) jsonParser.parse(payload);
        saveToken(token);

        }
        else
            this.payload =null;
    }

    private void saveToken(String token) {

        CacheLayer cacheLayer =CacheLayer.getInstance();
        cacheLayer.writeObject(token,"token");
    }

    public static boolean loadTokenFromFile() {
        CacheLayer cacheLayer =CacheLayer.getInstance();
        String token = (String ) cacheLayer.readObject("token");
        try {

            if(token!=null)
            {
                TokenManager.setToken(token);

                if(tokenManager.getExperation()-60*60*1000l>System.currentTimeMillis())
                {
                    TokenManager.setToken(null);
                    return false;
                }
                else
                    return true;

            }
        } catch (JSONException e) {

        }
        return  false;
    }

    public String getToken() {
        return this.token;
    }
    public Integer getId() throws JSONException {
        return this.payload.get("id").getAsInt();
    }


    public String getRole() throws JSONException {
        return this.payload.get("roles").getAsJsonArray().get(0).getAsString();
    }

    private long getExperation() throws JSONException {
        return this.payload.get("exp").getAsLong();

    }

    public static TokenManager getInstance()
    {
        if(tokenManager==null) {
            try {
                tokenManager = new TokenManager(null);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return  tokenManager;
    }

    public static void setToken(String token) throws JSONException {
        if(tokenManager==null)
             tokenManager = getInstance();
        tokenManager.token=token;
        tokenManager.initToken();

    }



    public static String bearer()
    {
        return "Bearer ";
    }



}
