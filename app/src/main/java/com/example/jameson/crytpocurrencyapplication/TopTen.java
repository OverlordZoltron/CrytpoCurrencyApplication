package com.example.jameson.crytpocurrencyapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.NumberFormat;
import android.icu.util.Output;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopTen extends AppCompatActivity {
    TextView topTen;
    TableLayout stk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
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

        stk = (TableLayout) findViewById(R.id.tLayout);


        Bundle extras = getIntent().getExtras();

        String result = extras.getString(MainActivity.TOP_TEN);


        init();

        /****************** Start Parse Response JSON Data *************/

        String OutputData = "";
        JSONArray jsonResponse;

        try {

            /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
            jsonResponse = new JSONArray(result);

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
                String rank = jsonChildNode.optString("rank").toString();
                String name = jsonChildNode.optString("name").toString();

                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                double priceUSD = Double.parseDouble(jsonChildNode.optString("price_usd").toString());

                String price = formatter.format(priceUSD);


                TableRow tbrow = new TableRow(this);
                TextView t1v = new TextView(this);
                t1v.setText(rank);
                //t1v.setTextColor(Color.WHITE);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(this);
                t2v.setText(name);
                //t2v.setTextColor(Color.WHITE);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                TextView t3v = new TextView(this);
                t3v.setText(price);
                //t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                stk.addView(tbrow);


                /*
                OutputData += " Rank: "+ rank +" "
                        + "Name: "+ name +" "
                        + "Price: "+ price +"  "
                        +"\n";

                */

            }
            /****************** End Parse Response JSON Data *************/

            //topTen.setText(OutputData);

            //Show Parsed Output on screen (activity)
            //info.setText( OutputData );

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


    public void init() {
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Rank ");
        tv0.setTypeface(null, Typeface.BOLD);
        //tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Currency Name ");
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setGravity(Gravity.CENTER);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Price(USD) ");
        tv2.setTypeface(null, Typeface.BOLD);
        tv2.setGravity(Gravity.CENTER);
        //tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);
    }

}
