package com.mmorano.fantasyfootballfeed.DataFetching;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataFetcher {

    protected String getResponse(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        try(CloseableHttpResponse response = httpClient.execute(request)){
            return EntityUtils.toString(response.getEntity());
        } catch (RuntimeException | IOException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    protected ArrayList<String> getResponses(HashMap<String, String> urls)  {
        ArrayList<String> results = new ArrayList<>();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(Runtime.getRuntime().availableProcessors());
        HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(connManager);
        CloseableHttpClient httpClient = clientBuilder.build();
        //create threads to run GET for each url
        ArrayList<ClientMultiThreaded> threads = new ArrayList<>();
        urls.forEach((id,url) -> {
            threads.add(new ClientMultiThreaded(httpClient, new HttpGet(url), id));
        });
        //run each request thread
        for(ClientMultiThreaded thread : threads)
            thread.start();
        //join threads
        for(ClientMultiThreaded thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //add result strings to output
        for(ClientMultiThreaded thread : threads)
            results.add(thread.getResponse());
        return results;
    }
}
