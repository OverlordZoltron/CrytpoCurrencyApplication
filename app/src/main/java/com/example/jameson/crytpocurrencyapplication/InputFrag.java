package com.example.jameson.crytpocurrencyapplication;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputFrag extends Fragment {
    public static final int TIMEOUT = 5000;
    View view;
    Button btnBuy;
    TextView txtInput;
    EditText txtMoney;
    Spinner spCurrency;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InputFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static InputFrag newInstance(String param1, String param2) {
        InputFrag fragment = new InputFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_input, container, false);
        txtMoney = view.findViewById(R.id.txtInput);
        btnBuy = view.findViewById(R.id.btnBuy);
        spCurrency = view.findViewById(R.id.spCurrency);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // WebServer Request URL
                String input = spCurrency.getSelectedItem().toString();

                String serverURL = "https://api.coinmarketcap.com/v1/ticker/" + input + "/";
                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation().execute(serverURL);
            }
        });
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                    String name = jsonChildNode.optString("name");
                    double money = Double.valueOf(txtMoney.getText().toString());
                    double priceUSD = Double.parseDouble(jsonChildNode.optString("price_usd"));

                    double amount = money / priceUSD;
                    //NumberFormat formatter = new DecimalFormat("#.#####");
                    //String boughtAmount = formatter.format(amount);



                    OutputData += "You have bought:  \n\n"+ amount + " " + name + " currency!";


                }
                /****************** End Parse Response JSON Data *************/

                //Show Parsed Output on screen (activity)
                //info.setText( OutputData );
                onButtonPressed(Uri.parse(OutputData));

            } catch (JSONException e) {

                e.printStackTrace();
            }


        }

    }
}
