package com.example.bearbear.tschatbot;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by bear&bear on 6/5/2016.
 */
public class HttpData extends AsyncTask<String, Void, String>{

    private HttpClient mHttpClient;
    private HttpPost mHttpPost;
    private HttpResponse mHttpResponse;
    private HttpEntity mHttpEntity;
    private InputStream in;
    private String url;
    private HttpGetDataListener listener;
    public HttpData(String url, HttpGetDataListener listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
        protected String doInBackground(String... params) {
        try {
            mHttpClient = new DefaultHttpClient();
            mHttpPost = new HttpPost(url);
            mHttpResponse = mHttpClient.execute(mHttpPost);
            mHttpEntity = mHttpResponse.getEntity();
            in = mHttpEntity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        }catch(Exception e) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.getDataUrl(s);
        super.onPostExecute(s);
    }
}
