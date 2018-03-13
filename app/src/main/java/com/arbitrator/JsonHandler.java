package com.arbitrator;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHandler extends AsyncTask<Void, Void, Void> {

    public String url;
    public int c;
    public String arr[][];

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected Void doInBackground(Void... voids) {

        HttpHandler hh = new HttpHandler();

        if (c == 1) {
            String jsonStr = hh.makeServiceCallget(url);

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    JSONObject jsonObj = (JSONObject) (jsonArr.get(0));
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from Server.");
            }
        } else if (c == 2) {
            try {
                JSONObject obj = new JSONObject();
                for (int i = 0; i < arr.length; i++)
                    obj.put(arr[i][0], arr[i][1]);
                //return obj;
                HttpHandler service = new HttpHandler();
                String jsonStr = service.makeServiceCallpost(url, obj);

                if (jsonStr != null) {
                    try {
                        JSONArray jsonArr = new JSONArray(jsonStr);
                        JSONObject jsonObj = (JSONObject) (jsonArr.get(0));
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from Server.");
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}
