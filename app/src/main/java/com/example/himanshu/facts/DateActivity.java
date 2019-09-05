package com.example.himanshu.facts;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);Intent intent = getIntent();

        Button goButton = (Button)findViewById(R.id.date_go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText month = (EditText)findViewById(R.id.month);
                EditText date = (EditText)findViewById(R.id.date);
                int mm =-1;
                int dd=-1;
                try{
                    mm=Integer.parseInt(month.getText().toString());
                    dd=Integer.parseInt(date.getText().toString());
                }
                catch (NumberFormatException n){
                    Toast.makeText(DateActivity.this, "Please enter a number in the boxes, then click Go",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                ConnectivityManager connectivityManager = (ConnectivityManager) DateActivity.this.getSystemService(DateActivity.this.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!isConnected){
                    Toast.makeText(DateActivity.this,"You are not connected to any network",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String stringUrl = "http://numbersapi.com/";

                StringBuilder builder =new StringBuilder(stringUrl);
                builder.append(mm).append("/").append(dd).append("/").append("date").append("?json");
                stringUrl = builder.toString();

                TextView exView = (TextView) findViewById(R.id.textView3);
                exView.setVisibility(View.GONE);
                ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar3);
                progressBar.setVisibility(View.VISIBLE);

                NetworkingFunctionality nf = new NetworkingFunctionality();
                DateActivity.DateAsyncTask task = new DateActivity.DateAsyncTask();
                try {
                    task.execute(nf.createURL(stringUrl));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });




    }



    private class DateAsyncTask extends AsyncTask<URL,Void,String> {
        @Override
        protected String doInBackground(URL... urls) {
            NetworkingFunctionality nf=new NetworkingFunctionality();


            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = nf.makeHttpRequest(urls[0]);
            } catch (IOException e) {
                // TODO Handle the IOException
                Log.e("DateAsync","IOException");
            }
            QueryUtils queryUtils = new QueryUtils(jsonResponse);
            // Extract relevant fields from the JSON response and create an {@link Event} object
            String message = queryUtils.getNumber();

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return message;
        }
        @Override
        protected void onPostExecute(String message) {
            if (message == null) {
                return;
            }

            updateUI(message);
        }
    }

    private void updateUI(String message){
        ProgressBar progressBar =(ProgressBar)findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.GONE);
        if(message!="") {
            TextView exView = (TextView)findViewById(R.id.textView3);
            exView.setText(message);
            exView.setVisibility(View.VISIBLE);

        }
        else {
            Toast.makeText(DateActivity.this,"Check your internet connection",
                    Toast.LENGTH_LONG).show();
        }
    }
}
