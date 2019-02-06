package com.team14.directionstest2;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.RoadsApi;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.maps.GeoApiContext;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.SnappedPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.RoadsApi;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.model.GeocodingResult;
//import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;
import com.google.maps.model.SpeedLimit;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Double.valueOf;

public class MainActivity extends AppCompatActivity implements DirectionCallback, View.OnClickListener{
    //private Button btnRequestDirection;
    //private GoogleMap googleMap;
    private String serverKey;
    private LatLng origin;
    private LatLng destination;
    private TextView view;
    private TextView view1;
    private TextView view2;
    private TextView view3;
    private TextView view4;
    private TextView view5;
    private EditText Input1;
    private EditText Input2;
    private String[] stockArr;
    ArrayList<LatLng> directionPosList;
    private GeoApiContext mContext;
    private Button but1;
    private Button but2;
    private Button but3;
    private Button but4;
    private Button but5;
    private int count;
    private double[] Latitudes;
    private double[] Longitudes;
    private int[] stre;
    private String currentString;
    private String CurrentStreet;
    private String Currentpremises;
    private int sif;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverKey = "AIzaSyAU-jSsThQo2f4Ne0ijd8qScR67JFaeHKY";
        origin = new LatLng(51.059324, 0.163243);
        destination = new LatLng(51.058562, 0.163186);
        view = (TextView) findViewById(R.id.textview);
        view1 = (TextView) findViewById(R.id.textView);
        view2 = (TextView) findViewById(R.id.textView2);
        view3 = (TextView) findViewById(R.id.textView3);
        view4 = (TextView) findViewById(R.id.textView4);
        view5 = (TextView) findViewById(R.id.textView5);
        but1 = (Button) findViewById(R.id.button2);
        but2 = (Button) findViewById(R.id.button3);
        but3 = (Button) findViewById(R.id.button4);
        but4 = (Button) findViewById(R.id.button5);
        but5 = (Button) findViewById(R.id.button6);
        Input1 = (EditText) findViewById(R.id.editText);
        Input2 = (EditText) findViewById(R.id.editText2);
        sif = 5;
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Forward();
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reverse();
            }
        });
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestVectorDirection();
            }
        });
        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LookUpAddress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mContext = new GeoApiContext().setApiKey("AIzaSyAU-jSsThQo2f4Ne0ijd8qScR67JFaeHKY");
        but5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDirection();
            }
        });
        count = 0;
    }
    public void requestDirection(){
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.BICYCLING)
                .execute(this);
    }
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody){
        if(direction.isOK()){
            Route route = direction.getRouteList().get(0);
            directionPosList = route.getLegList().get(0).getDirectionPoint();
            //String s = directionPosList.get(3).toString();
            //String b = directionPosList.get(6).toString();
            //String e = directionPosList.get(sif).toString();
            int a = directionPosList.size();
            String[] tt = new String[a];
            for(int i = 0 ;i < a; i++){
                String tmp = directionPosList.get(i).toString();
                tt[i] = tmp;
            }
            Latitudes = new double[a];
            Longitudes = new double[a];
            stockArr = tt;
            view.setText(Integer.toString(a));
            //view1.setText(b);
            //view2.setText(e);
            for(int i = 0; i < a;i++) {
                String[] data = tt[i].split(":", 2);
                String data2 = data[1].substring(2, (data[1].length() - 1));
                String[] latl = data2.split(",", 2);
                Latitudes[i] = Double.valueOf(latl[0]);
                Longitudes[i] = Double.valueOf(latl[1]);
            }
            //view2.setText(latl[0]);
            //view1.setText(latl[1]);
            //view2.setText(data2);
        }
        else{
            view.setText("Failed");
        }
        //snapToRoads();
    }
    @Override
    public void onDirectionFailure(Throwable t) {
        view.setText("Major Failure");
    }
    public void Reverse(){
        count = count - 3;
        try {
            if(count < Latitudes.length && count >= 0) {
                snapToRoads(Latitudes[count], Longitudes[count]);
                //count = count + 3;
                String[] col = currentString.split(",",5);
                int s = col.length;
                String g = Integer.toString(s);
                //view4.setText(g);
                if(s>3){
                    CurrentStreet = col[1];
                    if(CurrentStreet.equals(" South Kensington") || CurrentStreet.equals(" Knightsbridge")){
                        CurrentStreet = col[0];
                    }
                }
                else{
                    CurrentStreet = col[0];
                }
                String[] del = CurrentStreet.split(" ",5);
                String temp;
                if(del.length > 3){
                    temp = del[2] + " " + del[3];
                }
                else if(del.length > 2){
                    temp = del[1] + " " + del[2];
                }
                else if(del.length > 1){
                    temp = del[0] + " " + del[1];
                }
                else{
                    temp = del[0];
                }
                view4.setText(temp);
                view5.setText(Currentpremises);
            }
            else{
                view3.setText("End of Instructions");
            }
        }
        catch(IOException e){

        }
    }
    public void Forward(){
        //count = count + 3;
        try {
            if(count < Latitudes.length) {
                snapToRoads(Latitudes[count], Longitudes[count]);
                count = count + 3;
                //count = count + 3;
                String[] col = currentString.split(",",5);
                int s = col.length;
                String g = Integer.toString(s);
                //view4.setText(g);
                if(s>3){
                    CurrentStreet = col[1];
                    if(CurrentStreet.equals(" South Kensington") || CurrentStreet.equals(" Knightsbridge")){
                        CurrentStreet = col[0];
                    }
                }
                else{
                    CurrentStreet = col[0];
                }
                String[] del = CurrentStreet.split(" ",5);
                String temp;
                if(del.length > 3){
                    temp = del[2] + " " + del[3];
                }
                else if(del.length > 2){
                    temp = del[1] + " " + del[2];
                }
                else if(del.length > 1){
                    temp = del[0] + " " + del[1];
                }
                else{
                    temp = del[0];
                }
                view4.setText(temp);
                view5.setText(Currentpremises);
            }
            else{
                view3.setText("End of Instructions");
            }
        }
        catch(IOException e){

        }
    }
    @Override
    public void onClick(View v){
        /*try {
            if(count < Latitudes.length) {
                snapToRoads(Latitudes[count], Longitudes[count]);
                count = count + 3;
                String[] col = currentString.split(",",5);
                int s = col.length;
                String g = Integer.toString(s);
                //view4.setText(g);
                if(s>3){
                    CurrentStreet = col[1];
                }
                else{
                    CurrentStreet = col[0];
                }
                String[] del = CurrentStreet.split(" ",5);
                String temp;
                if(del.length > 3){
                    temp = del[2] + " " + del[3];
                }
                else if(del.length > 2){
                    temp = del[1] + " " + del[2];
                }
                else if(del.length > 1){
                    temp = del[0] + " " + del[1];
                }
                else{
                    temp = del[0];
                }
                view4.setText(temp);
                view5.setText(Currentpremises);
            }
            else{
                view3.setText("End of Instructions");
            }
        }
        catch(IOException e){

        }*/

    }
    public void TestVectorDirection(){
        stre = new int[1];
        stre[0] = sif;
        String temp = Double.toString(Latitudes[sif]) + " " +  Double.toString(Longitudes[sif]);
        view2.setText(temp);
        sif++;
        CalculateDirections f = new CalculateDirections(Latitudes,Longitudes,stre);
        String hij = f.CalcVectors(0);
        view1.setText(hij);
    }
    private void snapToRoads(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        //String temp = addresses.get(0).;
        Currentpremises = addresses.get(0).getPremises();
        currentString = address;
        view3.setText(address);
        //view5.setText(temp);
        //view4.setText(knownName);

    }
    public void LookUpAddress() throws IOException {
        String s = Input1.getText().toString();
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(s,1);
        double latitude = 0;
        double longitude = 0;
        if(addresses.size() > 0) {
           latitude= addresses.get(0).getLatitude();
           longitude= addresses.get(0).getLongitude();
        }
        origin = new LatLng(latitude,longitude);
        String out = Double.toString(latitude) + " " + Double.toString(longitude);
        Input1.setText(out);
        String s1 = Input2.getText().toString();
        Geocoder geocoder1 = new Geocoder(this,Locale.getDefault());
        List<Address> addresses1;
        addresses1 = geocoder1.getFromLocationName(s1,1);
        //double latitude = 0;
        //double longitude = 0;
        if(addresses1.size() > 0) {
            latitude= addresses1.get(0).getLatitude();
            longitude= addresses1.get(0).getLongitude();
        }
        destination = new LatLng(latitude,longitude);
        String out2 = Double.toString(latitude) + " " + Double.toString(longitude);
        Input2.setText(out2);
    }

}
