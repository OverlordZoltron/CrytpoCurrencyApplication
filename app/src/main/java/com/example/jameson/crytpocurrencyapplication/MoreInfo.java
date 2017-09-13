package com.example.jameson.crytpocurrencyapplication;

import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.name;

public class MoreInfo extends AppCompatActivity {
    ImageView ivImage;
    TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ivImage = (ImageView) findViewById(R.id.ivInfo);
        info = (TextView) findViewById(R.id.txtInfo);
        info.setGravity(Gravity.CENTER_HORIZONTAL);

        Bundle extras = getIntent().getExtras();

        String bitcoinResult = extras.getString(MainActivity.BITCOIN_INFO);
        String ethereumResult = extras.getString(MainActivity.ETHEREUM_INFO);

        if(bitcoinResult != null){
            //display bitcoin Image
            ivImage.setImageResource(R.drawable.bitcoin_icon);
        } else {
            ivImage.setImageResource(R.drawable.ethereum_icon);
        }


        /****************** Start Parse Response JSON Data *************/

        String OutputData = "";
        JSONArray jsonResponse;

        try {

            /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
            if(bitcoinResult != null){
                jsonResponse = new JSONArray(bitcoinResult);
            } else {
                jsonResponse = new JSONArray(ethereumResult);
            }
            //jsonResponse = new JSONArray(result);

            /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
            /*******  Returns null otherwise.  *******/
            //division.setText(jsonResponse.getString("Division"));

            /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
            /*******  Returns null otherwise.  *******/
            //JSONArray jsonMainNode = jsonResponse.optJSONArray("roster");

            /*********** Process each JSON Node ************/


            int lengthJsonArr = jsonResponse.length();

            for(int i=0; i < lengthJsonArr; i++)
            {
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonResponse.getJSONObject(i);

                /******* Fetch node values **********/
                String name = jsonChildNode.optString("name");

                /*USD formatting*/
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                double priceUSD = Double.parseDouble(jsonChildNode.optString("price_usd"));
                String price = formatter.format(priceUSD);

                /* Using the US Currency format, get markey cap */
                double marketCap = Double.parseDouble(jsonChildNode.optString("market_cap_usd"));
                String mrktCap = formatter.format(marketCap);

                /*Bitcoin price Formatting*/
                formatter = new DecimalFormat("#0.000");
                double priceInBitcoin = Double.parseDouble(jsonChildNode.optString("price_btc"));
                String priceBTC = formatter.format(priceInBitcoin);

                /*Using the decimal format, format the available supply */
                formatter = new DecimalFormat("#,###.00");
                double avlSupp = Double.parseDouble(jsonChildNode.optString("available_supply"));
                String availSupply = formatter.format(avlSupp);

                /* Euro price formatting */
                formatter = NumberFormat.getCurrencyInstance(ULocale.GERMANY);
                double euro = Double.parseDouble(jsonChildNode.optString("price_eur"));
                String euroPrice = formatter.format(euro);

                /* Getting the percent change for the past 24hrs, and the past week */
                String percentChangeDay = jsonChildNode.optString("percent_change_24h");

                String percentChangeWeek = jsonChildNode.optString("percent_change_7d");


                OutputData += "Name:  "+ name +" "
                        + "\nPrice(USD):  "+ price +"  "
                        +"\nPrice in Bitcoin: " + priceBTC + " "
                        +"\nPrice in Euros: " + euroPrice + " "
                        +"\nPercent Change (24hrs): " + percentChangeDay + "% "
                        +"\nPercent Change (Week): " + percentChangeWeek + "% "
                        +"\nMarket Cap: " + mrktCap + " "
                        +"\nAvailable Supply: " + availSupply + " ";


            }

            /****************** End Parse Response JSON Data *************/

            //topTen.setText(OutputData);

            //Show Parsed Output on screen (activity)
            info.setText( OutputData );

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

}
