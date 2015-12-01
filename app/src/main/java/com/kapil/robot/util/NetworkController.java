package com.kapil.robot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kapilboss on 06/05/15.
 */
public class NetworkController {

    public String doGet(String url){
        String responseStr=null;
        try {
            URL myUrl=new URL(url);
            HttpURLConnection myCon= (HttpURLConnection) myUrl.openConnection();
            myCon.setRequestMethod("GET");
            myCon.setRequestProperty("User-Agent","Mozilla/5.0");
            int responseCode=myCon.getResponseCode();
            if(responseCode!=HttpURLConnection.HTTP_OK)
                return null;
            BufferedReader br=new BufferedReader(new InputStreamReader(myCon.getInputStream()));
            String line;
            StringBuilder response=new StringBuilder();
            while((line=br.readLine())!=null){
                response.append(line);
            }
            responseStr= response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            return responseStr;
        }

        //return null;

    }


}
