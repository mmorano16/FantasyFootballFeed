package com.mmorano.fantasyfootballfeed.DataFetching;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ClientMultiThreaded extends Thread{
    CloseableHttpClient httpClient;
    HttpGet httpget;
    String id;
    String response;

    public ClientMultiThreaded(CloseableHttpClient httpClient, HttpGet httpget, String id) {
        this.httpClient = httpClient;
        this.httpget = httpget;
        this.id = id;
        this.response = "";
    }

    public String getResponse(){
        return response;
    }

    @Override
    public void run() {
        try{
            //Executing the request
            CloseableHttpResponse httpresponse = httpClient.execute(httpget);

            //Displaying the status of the request.
            System.out.println("status of thread "+id+":"+httpresponse.getStatusLine());

            //Retrieving the HttpEntity and displaying the no.of bytes read
            response = EntityUtils.toString(httpresponse.getEntity());
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}