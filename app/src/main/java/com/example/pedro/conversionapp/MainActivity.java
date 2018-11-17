package com.example.pedro.conversionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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
    public boolean onCreateOptionsMenu(Menu menu) {

        String a[] = getResources().getStringArray(R.array.units);

        // Inflate your main_menu into the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Find the menuItem to add your SubMenu
        MenuItem myMenuItem = menu.findItem(R.id.about);
        SubMenu subm = myMenuItem.getSubMenu();

        for (int i=0; i< a.length; i++) {
            int id = i;
            subm.add(0,id,0,a[i]);
            //menu.add(0,id,0,a[i]);
        }

        // Inflating the sub_menu menu this way, will add its menu items
        // to the empty SubMenu you created in the xml
        getMenuInflater().inflate(R.menu.sub_menu, myMenuItem.getSubMenu());

        return (super.onCreateOptionsMenu(menu));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("App",item.getTitle().toString());
        Integer id = item.getItemId();
        String c_unit = item.getTitle().toString();

        if (id == 2131230726)
        {
            return true;
        }
        else {
            AboutUnits.setText(c_unit);
            Intent i = new Intent(this, AboutUnits.class);
            startActivity(i);
            return true;
        }
    }



    public void displayPopupWindow(String info) {
        Intent i = new Intent(this, AboutUnits.class);
        startActivity(i);

        /*
        View layout = getLayoutInflater().inflate(R.layout.popup, null);
        popup.setContentView(layout);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
        */
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
