package com.example.myclasses.MyAsyncTask;

/**
 * Created by prats on 3/3/2015.
 */
public interface OnAsyncResult {
    public void OnSuccess(String result);
    public void OnFailure(String result);
    public void OnCancelled(String result);
}