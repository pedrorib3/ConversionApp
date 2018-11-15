package com.example.pedro.conversionapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button convert;
    private Spinner spinnerSourceUnit;
    private Spinner spinnerDestUnit;
    private TextView conversionRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerSourceUnit= findViewById(R.id.spinUnits);
        spinnerDestUnit=findViewById(R.id.spinUnits2);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.units,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSourceUnit.setAdapter(adapter);
        spinnerDestUnit.setAdapter(adapter);

        convert = findViewById(R.id.convert);
        convert.setOnClickListener(this);
        conversionRate = findViewById(R.id.output);
    }

    @Override
    public void onClick(View view) {
        EditText quantity= findViewById(R.id.editQuant);
        try{
            float sourceQuantity = Float.valueOf(quantity.getText().toString());
            String sourceUnit = spinnerSourceUnit.getSelectedItem().toString();
            String destUnit=spinnerDestUnit.getSelectedItem().toString();

            getConversion(sourceQuantity,sourceUnit,destUnit);
        }
        catch(NumberFormatException ex) {
            Toast.makeText(getApplicationContext(), "Please Enter A Valid Input!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    void getConversion(float sourceQuantity,String sourceUnit ,String destUnit) {
        GetConversionRate httpRate;
        httpRate = new GetConversionRate(sourceQuantity,sourceUnit,destUnit);
        Thread thr = new Thread(httpRate);
        thr.start();
    }

    String getUCUMrep(String unit){
        switch (unit) {
            case "Ångstrom":
                return "Ao";
            case "Smoot":
                return "%5Bsmoot%5D";
            case "inch":
                return "%5Bin_i%5D";
            case "foot":
                return "%5Bft_i%5D";
            case "yard":
                return "%5Byd_i%5D";
            case "mile":
                return "%5Bmi_i%5D";
            case "nautical mile":
                return "%5Bnmi_i%5D";
            case"mil":
                return "%5Bmil_i%5D";
            case "rod":
                return "%5Brd_us%5D";
            case "fathom":
                return"%5Bfth_us%5D";
            case"furlong":
                return"%5Bfur_us%5D";
            case"km":
                return"km";
            case"m":
                return "m";
            case"cm":
                return "cm";
            case"mm":
                return "mm";
            case"μm":
                return"um";
            case"nm":
                return"nm";
        }
        return"";
}

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        catch (IOException e) {
            return e.getMessage();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    return e.getMessage();
                }
            }
        }
        return response.toString();
    }

    private String transform(String Conversion) {
        String destQuantity=null;
        try {
            JSONObject json = new JSONObject(Conversion);
            destQuantity=json.getJSONObject("UCUMWebServiceResponse").getJSONObject("Response").getString("ResultQuantity");
            Log.d("ADebugTag", "Value: " + destQuantity);
        } catch (Exception e) {
            destQuantity = "Error on returned string!\n" + Conversion;
        }
        return destQuantity;
    }

    private void writeConversion(final Float sourceQuant,final String sourceUnit,final String destQuant,final String destUnit) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message= sourceQuant+" "+sourceUnit+" = "+destQuant+" "+destUnit;
                conversionRate.setText(message);
            }
        });
    }

//**************************************************************************
//Internal class to call HTTP operation in a separate thread

    class GetConversionRate implements Runnable {
        private float sourceQuantity;
        private String sourceUnit;
        private String destUnit;

        GetConversionRate(float toSourceQuantity,String toSourceUnit,String toDestUnit) {
            sourceQuantity=toSourceQuantity;
            sourceUnit = toSourceUnit;
            destUnit=toDestUnit;
        }

        @Override
        public void run() {
            URL url;
            HttpURLConnection urlConnection;
            String sourceUCUM=getUCUMrep(sourceUnit);
            String destUCUM=getUCUMrep(destUnit);

            try {
                url = new URL("https://ucum.nlm.nih.gov/ucum-service/v1/ucumtransform/"+ sourceQuantity +"/from/"+ sourceUCUM +"/to/"+ destUCUM );
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setUseCaches(false);
                int responseCode = urlConnection.getResponseCode();
                if(responseCode == 200) {  // code 200 means OK
                    String response = readStream(urlConnection.getInputStream());
                    String destQuantity= transform(response);
                    Log.d("ADebugTag", "Value: " + response);
                    writeConversion(sourceQuantity,sourceUnit,destQuantity,destUnit);
                }
                //else
                    //writeRate("Code: " + responseCode);
            } catch (java.io.IOException e) {
                e.printStackTrace(); }
        }
    }


}
