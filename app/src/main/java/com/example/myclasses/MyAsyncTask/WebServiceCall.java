package com.example.myclasses.MyAsyncTask;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.example.myclasses.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;


@SuppressWarnings("unused")
public class WebServiceCall {

    private WebServiceConfig config;
    private ProgressBar progressBar;
    private UploadImageAsyncTask uploadImageAsyncTask;
    private GetAsyncTask getAsyncTask;
    private Context mContext;

    private String url;
    private Object model;

    private OnResultListener OnResultListener;
    private ConnectionCheck cd;
    private boolean isInternetAvailable;
    private Dialog mdialog;


    /**
     * @param mContext         = context for progress dialog
     * @param url              = web service url
     * @param textParams       = data to be sent using POST method
     * @param model            = model class to be used to parse JSON using GSON. Pass String.class if you want result as string(without parsing)
     * @param showDialog       = boolean to specify whether to show progress dialog or not
     * @param OnResultListener = listener to get result(success/fail) after web service call
     */
    public WebServiceCall(Context mContext, String url, LinkedHashMap<String, String> textParams, Object model, boolean showDialog, OnResultListener OnResultListener) {
        this.url = url;
        this.model = model;
        this.mContext = mContext;
        this.OnResultListener = OnResultListener;
        cd = new ConnectionCheck();
        config = new WebServiceConfig();
        isInternetAvailable = cd.isNetworkConnected(mContext);
        if (isInternetAvailable) {
            getData(textParams, showDialog);
        } else {
//            new ConnectionCheck().showConnectionDialog(mContext).show();
        }
    }

    /**
     * @param mContext         = context for progress dialog
     * @param url              = web service url
     * @param textParams       = data to be sent using POST method
     * @param fileParams       = file path list to be sent using multipart
     * @param model            = model class to be used to parse JSON using GSON. Pass String.class if you want result as string(without parsing)
     * @param showDialog       = boolean to specify whether to show progress dialog or not
     * @param OnResultListener = listener to get result(success/fail) after web service call
     */
    public WebServiceCall(Context mContext, String url, LinkedHashMap<String, String> textParams, LinkedHashMap<String, File> fileParams, Object model, boolean showDialog, OnResultListener OnResultListener) {
        this.url = url;
        this.model = model;
        this.mContext = mContext;
        this.OnResultListener = OnResultListener;
        cd = new ConnectionCheck();
        isInternetAvailable = cd.isNetworkConnected(mContext);
        config = new WebServiceConfig();
        if (isInternetAvailable) {
            getDataWithFile(textParams, fileParams, showDialog);
        } else {
//            new ConnectionCheck().showConnectionDialog(mContext).show();
        }

    }

    /**
     * @param mContext         = context for progress dialog
     * @param url              = web service url
     * @param textParams       = data to be sent using POST method
     * @param model            = model class to be used to parse JSON using GSON. Pass String.class if you want result as string(without parsing)
     * @param showDialog       = boolean to specify whether to show progress dialog or not
     * @param progressBar      = for show progressbar behalf of progress dialog
     * @param OnResultListener = listener to get result(success/fail) after web service call
     */
    public WebServiceCall(Context mContext, String url, LinkedHashMap<String, String> textParams, Object model, boolean showDialog, ProgressBar progressBar, OnResultListener OnResultListener) {
        this.url = url;
        this.model = model;
        this.mContext = mContext;
        this.OnResultListener = OnResultListener;
        this.progressBar = progressBar;
        cd = new ConnectionCheck();
        isInternetAvailable = cd.isNetworkConnected(mContext);
        config = new WebServiceConfig();
        if (isInternetAvailable) {
            getData(textParams, showDialog);
        } else {
//            new ConnectionCheck().showConnectionDialog(mContext).show();
        }
    }

    /**
     * @param mContext         = context for progress dialog
     * @param url              = web service url
     * @param textParams       = data to be sent using POST method
     * @param fileParams       = file path list to be sent using multipart
     * @param model            = model class to be used to parse JSON using GSON. Pass String.class if you want result as string(without parsing)
     * @param showDialog       = boolean to specify whether to show progress dialog or not
     * @param progressBar      = for show progressbar behalf of progress dialog
     * @param OnResultListener = listener to get result(success/fail) after web service call
     */
    public WebServiceCall(Context mContext, String url, LinkedHashMap<String, String> textParams, LinkedHashMap<String, File> fileParams, Object model, boolean showDialog, ProgressBar progressBar, OnResultListener OnResultListener) {
        this.url = url;
        this.model = model;
        this.mContext = mContext;
        this.OnResultListener = OnResultListener;
        this.progressBar = progressBar;
        cd = new ConnectionCheck();
        isInternetAvailable = cd.isNetworkConnected(mContext);
        config = new WebServiceConfig();
        if (isInternetAvailable) {
            getDataWithFile(textParams, fileParams, showDialog);
        } else {
//            new ConnectionCheck().showConnectionDialog(mContext).show();
        }
    }

    /**
     * @param mContext         = context for progress dialog
     * @param url              = web service url
     * @param textParams       = data to be sent using POST method
     * @param model            = model class to be used to parse JSON using GSON. Pass String.class if you want result as string(without parsing)
     * @param showDialog       = boolean to specify whether to show progress dialog or not
     * @param config           = WebService Custom Configuration
     * @param OnResultListener = listener to get result(success/fail) after web service call
     */
    public WebServiceCall(Context mContext, String url, LinkedHashMap<String, String> textParams, Object model, boolean showDialog, WebServiceConfig config, OnResultListener OnResultListener) {
        this.url = url;
        this.model = model;
        this.mContext = mContext;
        this.config = config;
        this.OnResultListener = OnResultListener;
        cd = new ConnectionCheck();
        isInternetAvailable = cd.isNetworkConnected(mContext);
        if (isInternetAvailable) {
            getData(textParams, showDialog);
        } else {
//            new ConnectionCheck().showConnectionDialog(mContext).show();
        }
    }

    /**
     * @param mContext         = context for progress dialog
     * @param url              = web service url
     * @param textParams       = data to be sent using POST method
     * @param fileParams       = file path list to be sent using multipart
     * @param model            = model class to be used to parse JSON using GSON. Pass String.class if you want result as string(without parsing)
     * @param showDialog       = boolean to specify whether to show progress dialog or not
     * @param config           = WebService Custom Configuration
     * @param OnResultListener = listener to get result(success/fail) after web service call
     */
    public WebServiceCall(Context mContext, String url, LinkedHashMap<String, String> textParams, LinkedHashMap<String, File> fileParams, Object model, boolean showDialog, WebServiceConfig config, OnResultListener OnResultListener) {
        this.url = url;
        this.model = model;
        this.mContext = mContext;
        this.config = config;
        this.OnResultListener = OnResultListener;
        cd = new ConnectionCheck();
        isInternetAvailable = cd.isNetworkConnected(mContext);
        if (isInternetAvailable) {
            getDataWithFile(textParams, fileParams, showDialog);
        } else {
//            new ConnectionCheck().showConnectionDialog(mContext).show();
        }
    }

    /**
     * @param mContext         = context for progress dialog
     * @param url              = web service url
     * @param textParams       = data to be sent using POST method
     * @param model            = model class to be used to parse JSON using GSON. Pass String.class if you want result as string(without parsing)
     * @param showDialog       = boolean to specify whether to show progress dialog or not
     * @param progressBar      = for show progressbar behalf of progress dialog
     * @param config           = WebService Custom Configuration
     * @param OnResultListener = listener to get result(success/fail) after web service call
     */
    public WebServiceCall(Context mContext, String url, LinkedHashMap<String, String> textParams, Object model, boolean showDialog, ProgressBar progressBar, WebServiceConfig config, OnResultListener OnResultListener) {
        this.url = url;
        this.model = model;
        this.mContext = mContext;
        this.OnResultListener = OnResultListener;
        this.progressBar = progressBar;
        this.config = config;
        cd = new ConnectionCheck();
        isInternetAvailable = cd.isNetworkConnected(mContext);
        if (isInternetAvailable) {
            getData(textParams, showDialog);
        } else {
//            new ConnectionCheck().showConnectionDialog(mContext).show();
        }
    }

    /**
     * @param mContext         = context for progress dialog
     * @param url              = web service url
     * @param textParams       = data to be sent using POST method
     * @param fileParams       = file path list to be sent using multipart
     * @param model            = model class to be used to parse JSON using GSON. Pass String.class if you want result as string(without parsing)
     * @param showDialog       = boolean to specify whether to show progress dialog or not
     * @param progressBar      = for show progressbar behalf of progress dialog
     * @param config           = WebService Custom Configuration
     * @param OnResultListener = listener to get result(success/fail) after web service call
     */
    public WebServiceCall(Context mContext, String url, LinkedHashMap<String, String> textParams, LinkedHashMap<String, File> fileParams, Object model, boolean showDialog, ProgressBar progressBar, WebServiceConfig config, OnResultListener OnResultListener) {
        this.url = url;
        this.model = model;
        this.mContext = mContext;
        this.OnResultListener = OnResultListener;
        this.progressBar = progressBar;
        this.config = config;
        cd = new ConnectionCheck();
        isInternetAvailable = cd.isNetworkConnected(mContext);
        if (isInternetAvailable) {
            getDataWithFile(textParams, fileParams, showDialog);
        } else {
//            new ConnectionCheck().showConnectionDialog(mContext).show();
        }
    }


    public interface OnResultListener {
        void onResult(boolean status, Object obj);

        void onAsync(AsyncTask asyncTask);

        void onCancelled();
    }

    private GetAsyncTask getData(final LinkedHashMap<String, String> textParams, final boolean showDialog) {

        try {
            if (showDialog) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    mdialog = new Dialog(mContext, R.style.DialogTheme);
                    mdialog.setTitle("");
                    mdialog.setCancelable(false);
                    mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    mdialog.setContentView(R.layout.layout_progress);
                    mdialog.show();
                }
            }
        } catch (Exception e) {

        }

        // Async Result
        final OnAsyncResult onAsyncResult = new OnAsyncResult() {
            @Override
            public void OnSuccess(String result) {
                if (showDialog) {
                    try {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        } else {
                            mdialog.dismiss();
                        }
                    } catch (Exception e) {

                    }
                }

                try {
                    JSONObject object = new JSONObject(result);
                    if (model == String.class) {
                        OnResultListener.onResult(true, result);
                    } else {
                        if (object.getBoolean("status")) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.setDateFormat("M/d/yy hh:mm a"); //Format of our JSON dates
                            Gson gson = gsonBuilder.create();
                            OnResultListener.onResult(true, gson.fromJson(result, (Class) model));
                        } else {
                            OnResultListener.onResult(false, object.getString("message"));
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFailure(String result) {
                if (showDialog) {
                    try {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        } else {
                            mdialog.dismiss();
                        }

                    } catch (Exception e) {

                    }
                }
                if (showDialog) {
                    getAsyncTask = new GetAsyncTask(url, this, textParams, config);
                    //Toast.makeText(SplashScreenActivity.this,result,Toast.LENGTH_LONG).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme);
                    builder.setTitle(R.string.error_);
                    builder.setMessage(result);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (showDialog) {
                                try {
                                    if (progressBar != null) {
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        dialog.dismiss();
                                    }
                                } catch (Exception e) {

                                }
                            }
                            getAsyncTask.execute();
                        }
                    });
                    OnResultListener.onResult(false, "Error");
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mdialog.dismiss();
                        }
                    });
                    builder.show();
                }

            }

            @Override
            public void OnCancelled(String result) {
                OnResultListener.onCancelled();
                if (showDialog) {
                    try {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        } else {
                            mdialog.dismiss();
                        }
                    } catch (Exception e) {

                    }
                }
            }
        };

        getAsyncTask = new GetAsyncTask(url, onAsyncResult, textParams, config);
        OnResultListener.onAsync(getAsyncTask);
        getAsyncTask.execute();
        return getAsyncTask;

    }

    private void getDataWithFile(final HashMap<String, String> textParams, final HashMap<String, File> fileParams, final boolean showDialog) {

        if (showDialog) {
            try {
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    mdialog = new Dialog(mContext);
                    mdialog.setTitle("");
                    mdialog.setCancelable(false);
                    mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    mdialog.setContentView(R.layout.layout_progress);
                    mdialog.show();
                }
            } catch (Exception e) {

            }
        }


        // Async Result
        OnAsyncResult onAsyncResult = new OnAsyncResult() {
            @Override
            public void OnSuccess(String result) {
                if (showDialog) {
                    try {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        } else {
                            mdialog.dismiss();
                        }
                    } catch (Exception e) {

                    }
                }

                try {

                    JSONObject object = new JSONObject(result);
                    if (model == String.class) {
                        OnResultListener.onResult(true, result);
                    }
                    if (object.getBoolean("status")) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setDateFormat("M/d/yy hh:mm a"); //Format of our JSON dates
                        Gson gson = gsonBuilder.create();
                        OnResultListener.onResult(true, gson.fromJson(result, (Class) model));
                    } else {
                        OnResultListener.onResult(false, object.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFailure(String result) {
                if (showDialog) {
                    try {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        } else {
                            mdialog.dismiss();
                        }
                    } catch (Exception e) {

                    }
                }

                uploadImageAsyncTask = new UploadImageAsyncTask(url, this, textParams, fileParams, config);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme);
                builder.setTitle("Error");
                builder.setMessage(result);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (showDialog) {
                            try {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.VISIBLE);
                                } else {
                                    mdialog.show();
                                }
                            } catch (Exception e) {

                            }
                        }
                        uploadImageAsyncTask.execute();
                    }
                });
                OnResultListener.onResult(false, "Error");
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mdialog.dismiss();
                    }
                });
                builder.show();
            }

            @Override
            public void OnCancelled(String result) {
                OnResultListener.onCancelled();
                if (showDialog) {
                    try {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        } else {
                            mdialog.dismiss();
                        }
                    } catch (Exception e) {

                    }
                }
            }
        };
        uploadImageAsyncTask = new UploadImageAsyncTask(url, onAsyncResult, textParams, fileParams, config);
        OnResultListener.onAsync(uploadImageAsyncTask);
        uploadImageAsyncTask.execute();
    }

}
