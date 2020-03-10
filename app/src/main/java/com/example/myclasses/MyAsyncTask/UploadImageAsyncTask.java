package com.example.myclasses.MyAsyncTask;


import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;


public class UploadImageAsyncTask extends AsyncTask<Void, Void, String> {

    private WebServiceConfig config;
    private String url;
    private OnAsyncResult onAsyncResult;
    private Boolean resultFlag;
    private String charset = "UTF-8";


    private HashMap<String, String> textParams;
    private HashMap<String, File> fileParams;


    public UploadImageAsyncTask(String url, OnAsyncResult listener, HashMap<String, String> textParams, HashMap<String, File> fileParams, WebServiceConfig config) {
        this.url = url;
        this.onAsyncResult = listener;
        resultFlag = false;
        this.textParams = textParams;
        this.fileParams = fileParams;
        this.config = config;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            MultipartUtility multipart;
            multipart = new MultipartUtility(String.valueOf(url), charset, config);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

					/*multipart.addFormField("description", "Cool Pictures");
					multipart.addFormField("keywords", "Java,upload,Spring");*/

            /*multipart.addFormField("type", "image");
            multipart.addFormField("uploadFor", "service");*/


            for (Map.Entry<String, String> entry : textParams.entrySet()) {
                Log.e("Task", entry.getKey() + ":" + entry.getValue());

//                Log.e("Task", "doInBackground: " + entry.getValue());
                multipart.addFormField(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, File> entry : fileParams.entrySet()) {
                Log.e("Task", entry.getKey() + ":" + entry.getValue());
                multipart.addFilePart(entry.getKey(), entry.getValue());
            }

            String response = multipart.finish();
            Log.e("AsyncTask", "Response:: " + response);
            resultFlag = true;
            return response;
        } catch (SocketTimeoutException e1) {
            resultFlag = false;
            return WebServiceConfig.CONNECTION_TIMEOUT_ERROR;

        } catch (Exception e) {
            e.printStackTrace();
            resultFlag = false;
            return WebServiceConfig.UNEXPECTED_ERROR;
        }
    }

    @Override
    protected void onCancelled() {
        onAsyncResult.OnCancelled("onCancelled");
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String result) {
        if (resultFlag) {
            if (onAsyncResult != null) {
                onAsyncResult.OnSuccess(result);
            }
        } else {
            if (onAsyncResult != null) {
                onAsyncResult.OnFailure(result);
            }
        }
    }
}