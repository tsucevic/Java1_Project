/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.factory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author dnlbe
 */


public class UrlConnectionFactory {
    
    private static final int TIMEOUT = 10000;
    private static final String REQUEST_METHOD = "GET";
    private static final String USER_AGENT = "User-Agent";
    private static final String MOZILLA = "Mozilla/5.0";
    
    public static HttpURLConnection getHttpUrlConnection(String path, int timeout, String requestMethod) throws MalformedURLException, IOException {
        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setReadTimeout(timeout);
        con.setConnectTimeout(timeout);
        con.setRequestMethod(requestMethod);
        con.addRequestProperty(USER_AGENT,MOZILLA);
        return con;
    }
    
    public static HttpURLConnection getHttpUrlConnection(String path) throws MalformedURLException, IOException {
        return getHttpUrlConnection(path, TIMEOUT, REQUEST_METHOD);
    }
}
