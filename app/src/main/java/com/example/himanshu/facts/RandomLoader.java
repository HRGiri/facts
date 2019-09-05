package com.example.himanshu.facts;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

public class RandomLoader extends AsyncTaskLoader<String> {
    private  URL mUrl;
    public RandomLoader(Context context, URL url) {
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        NetworkingFunctionality nf=new NetworkingFunctionality();


        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = nf.makeHttpRequest(mUrl);
        } catch (IOException e) {
            // TODO Handle the IOException
            Log.e("RandomLoader","IOException");
        }
        QueryUtils queryUtils = new QueryUtils(jsonResponse);
        // Extract relevant fields from the JSON response and create an {@link Event} object
        String message = queryUtils.getNumber();

        // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
        return message;
    }
}
