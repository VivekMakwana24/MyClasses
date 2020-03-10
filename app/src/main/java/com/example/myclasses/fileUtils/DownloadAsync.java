package com.example.myclasses.fileUtils;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.myclasses.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadAsync extends AsyncTask<String, String, String> {

    private String FOLDER_NAME = ".demo";
    private String FOLDER_PATH;

    private Context mContext;
    private Uri uri;
    private String fileName, ext, filePath;
    private Dialog mdialog;
    private OnDownloadListener mOnDownloadListener;

    public interface OnDownloadListener {
        String onResult(String result);
    }

    public DownloadAsync(Context mContext, Uri uri, String fileName, String ext, OnDownloadListener mOnDownloadListener) {
        this.mContext = mContext;
        this.uri = uri;
        this.ext = ext;
        this.fileName = CommonUtils.stripExtension(fileName);
        this.mOnDownloadListener = mOnDownloadListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mdialog = new Dialog(mContext);
        mdialog.setTitle("");
        mdialog.setCancelable(false);
        mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mdialog.setContentView(R.layout.layout_progress);
        mdialog.show();
    }

    @Override
    protected String doInBackground(String... URL) {
        try {
            filePath = writeStreamToFile(mContext, uri, ext);
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        if (mdialog != null && mdialog.isShowing())
            mdialog.dismiss();
        mOnDownloadListener.onResult(filePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String writeStreamToFile(Context mContext, Uri mUri, String ext) {
        FOLDER_PATH = Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME;
        InputStream inputStream = null;
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try {
            inputStream = mContext.getContentResolver().openInputStream(mUri);
//            File file = File.createTempFile(fileName, "." + ext, folder);
            File file = new File(FOLDER_PATH + "/" + fileName + "." + ext);
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
