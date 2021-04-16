/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.AVRANDOM;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

/**
 *
 * @author PHUOCLE
 */
public class JAVA {

    static String link = "http://210.211.108.18:8081/gachthe/gach-the";

    public static void main(String[] args) throws Exception {
        String amount = "";//TELCO AMOUNT: 10.000 , 20.000, 30.000, 50.000, 100.000, 2000.000,300.000, 500.000;1000.000(only Viettel)
        String pin = "";//Pin is card number
        String checksum = "";//Sha1(SECRET_KEY::PRIVATEKEY::SERIAL::PIN::AMOUNT::TYPE);
        String type = "";//Type : gateway : VIETTEL, MOBIFONE, VINAPHONE
        String serial = "";//Serial: is serial
        String SECRET_KEY = "";//SECRET_KEY: key 
        String val = "0";
        JSONObject json = new JSONObject();
        json.put("amount", amount);
        json.put("pin", pin);
        json.put("checksum", checksum);
        json.put("type", type);
        json.put("serial", serial);
        json.put("SECRET_KEY", SECRET_KEY);
        val = JAVA.getResponse(link, json.toString());
        System.out.println("val: " + val);
        org.json.JSONObject valjson = new org.json.JSONObject(val);
        int code = valjson.getInt("errorcode");
        System.out.println("success: " + code);

    }

    public static String getResponse(String url, String request) throws Exception {
        URL sendUrl = new URL(url);
        System.out.println(url);
        URLConnection urlCon = sendUrl.openConnection();
        urlCon.setDoOutput(true);
        urlCon.setDoInput(true);
        HttpURLConnection httpConnection = (HttpURLConnection) urlCon;
        httpConnection.setRequestMethod("POST");
        httpConnection.setConnectTimeout(30000);
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setRequestProperty("Content-Length", Integer.toString(request.length()));
        PrintStream ps = new PrintStream(httpConnection.getOutputStream(), true, "utf-8");

        try {
            ps.write(request.getBytes("utf-8"));
            ps.flush();
            String str = httpConnection.getResponseMessage();
            InputStream is = httpConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            str = "";
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            is.close();
            httpConnection.disconnect();
            return new String(sb.toString().getBytes("utf-8"), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("401 for URL")) {
                return "401";
            } else {
                return "";
            }

        }

    }
}
