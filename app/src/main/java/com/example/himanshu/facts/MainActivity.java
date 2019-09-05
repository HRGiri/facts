package com.example.himanshu.facts;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.facts.MESSAGE";
    private static final String[] type = {"trivia","math","date","year"};
    private static final int EARTHQUAKE_LOADER_ID = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView triviaView =(TextView)findViewById(R.id.trivia_view);
        triviaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NumberActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "trivia");
                startActivity(intent);

            }
        });

        TextView mathView =(TextView)findViewById(R.id.math_view);
        mathView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NumberActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "math");
                startActivity(intent);

            }
        });

        TextView yearView =(TextView)findViewById(R.id.year_view);
        yearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NumberActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "year");
                startActivity(intent);

            }
        });
        TextView randomView =(TextView)findViewById(R.id.random_view);
        randomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!isConnected){
                    Toast.makeText(MainActivity.this,"You are not connected to any network",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Random random = new Random();
                int index = random.nextInt(4);
                StringBuilder stringBuilder = new StringBuilder("http://numbersapi.com/random/");
                stringBuilder.append(type[index]).append("?json");
                String stringUrl = stringBuilder.toString();

                TextView exView = (TextView) findViewById(R.id.textView8);
                exView.setVisibility(View.GONE);
                ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);


                NetworkingFunctionality nf = new NetworkingFunctionality();
                MainActivity.RandomAsyncTask task = new MainActivity.RandomAsyncTask();
                try {
                    task.execute(nf.createURL(stringUrl));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });

        TextView dateView =(TextView)findViewById(R.id.date_view);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DateActivity.class);
                startActivity(intent);

            }
        });
    }


    private class RandomAsyncTask extends AsyncTask<URL,Void,String> {
        @Override
        protected String doInBackground(URL... urls) {
            NetworkingFunctionality nf=new NetworkingFunctionality();


            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = nf.makeHttpRequest(urls[0]);
            } catch (IOException e) {
                // TODO Handle the IOException
                Log.e("RandomAsync","IOException");
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
        ProgressBar progressBar =(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        if(message!="") {
            TextView exView = (TextView) findViewById(R.id.textView8);
            exView.setText(message);
            exView.setVisibility(View.VISIBLE);

        }
        else {
            Toast.makeText(MainActivity.this,"Check your internet connection",
                    Toast.LENGTH_LONG).show();
        }
    }
}
