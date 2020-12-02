package com.example.uplan.models;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Clima extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... address) {
        try {
            URL url = new URL(address[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int data = isr.read();
            String contenido = "";
            char c;
            while(data != -1 ){
                c = (char) data;
                contenido = contenido + c ;
                data = isr.read();
            }
            return contenido;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
