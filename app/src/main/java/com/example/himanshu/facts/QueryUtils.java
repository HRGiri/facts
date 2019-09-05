package com.example.himanshu.facts;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class QueryUtils {
    private String mJsonResponse;

    public QueryUtils(String jsonResponse){
        mJsonResponse=jsonResponse;
    }
    public String getNumber(){
        String text="";

        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            JSONObject root = new JSONObject(mJsonResponse);
            text = root.getString("text");


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing JSON results", e);
        }

        return text;

    }
}
