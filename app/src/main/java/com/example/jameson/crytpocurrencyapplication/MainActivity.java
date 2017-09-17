package com.example.jameson.crytpocurrencyapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public static final int TIMEOUT = 5000;
    public final static String TOP_TEN = "TOP TEN";
    public final static String BITCOIN_INFO = "BITCOIN INFO";
    public final static String ETHEREUM_INFO = "ETHEREUM INFO";
    private int sent = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getTopTen(View view){
        // WebServer Request URL
        String serverURL = "https://api.coinmarketcap.com/v1/ticker/?limit=10";

        // Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperation().execute(serverURL);
    }

    public void getBitcoinInfo(View view){
        // WebServer Request URL
        String serverURL = "https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=EUR";
        sent = 2;

        // Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperation().execute(serverURL);
    }

    public void getEthereumInfo(View view){
        // WebServer Request URL
        String serverURL = "https://api.coinmarketcap.com/v1/ticker/ethereum/?convert=EUR";
        sent = 3;

        // Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperation().execute(serverURL);
    }

    public void buyCryptoCurrency(View view){
        /*
        // WebServer Request URL
        String serverURL = "https://api.coinmarketcap.com/v1/ticker/?limit=10";

        // Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperation().execute(serverURL);
        */
        Intent intent = new Intent(getApplicationContext(),TopTen.class);

        //intent.putExtra(TOP_TEN);
        startActivity(intent);
    }


    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection c;
        //HttpClient httpClient = new DefaultHttpClient();
        //HttpGet httpGet = new HttpGet(URL);
        try {
            //   HttpResponse response = httpClient.execute(httpGet);
            //   StatusLine statusLine = response.getStatusLine();
            URL u = new URL(URL);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(TIMEOUT);
            c.setReadTimeout(TIMEOUT);
            c.connect();
            int statusCode = c.getResponseCode();
            //int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
//                HttpEntity entity = response.getEntity();
//                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
            } else {
                Log.d("JSON", "Failed to download file");
            }
        } catch (Exception e) {
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }
        return stringBuilder.toString();
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        //   TextView jsonParsed = (TextView)findViewById(R.id.jsonParsed);

        @Override
        protected String doInBackground(String... params) {
            /************ Make Post Call To Web Server ***********/
            return readJSONFeed(params[0]);
        }


        protected void onPostExecute(String result) {
            // NOTE: You can call UI Element here.
            if(sent == 2){
                //reset sent back to 1
                sent = 1;

                //Create an intent
                Intent intent = new Intent(getApplicationContext(),MoreInfo.class);

                intent.putExtra(BITCOIN_INFO, result);
                startActivity(intent);
            } else if (sent == 3){
                //reset sent back to 1
                sent = 1;

                Intent intent = new Intent(getApplicationContext(),MoreInfo.class);

                intent.putExtra(ETHEREUM_INFO, result);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getApplicationContext(),TopTen.class);

                intent.putExtra(TOP_TEN, result);
                startActivity(intent);
            }
        }

    }
}
