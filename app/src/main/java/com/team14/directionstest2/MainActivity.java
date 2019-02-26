package com.team14.directionstest2;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.RoadsApi;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
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
import com.team14.directionstest2.FormatForCommunication;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
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
    private Button but6;
    private Button but7;
    private int count;
    private double[] Latitudes;
    private double[] Longitudes;
    private int[] stre;
    private String currentString;
    private String CurrentStreet;
    private String Currentpremises;
    private int sif;
    private FusedLocationProviderClient client;
    double CurrentLat;
    double CurrentLong;
    boolean CurrentAsOrigin = false;
    boolean End_Of_Instructions = true;
    public FormatForCommunication Formatter = new FormatForCommunication();
    public Vector<Double> Turn_Lat;
    public Vector<Double> Turn_Long;
    public Vector<Double> Distances;
    public Vector<String> NextStreet;
    public int[] Turn_Index;
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
        but6 = (Button) findViewById(R.id.toStart);
        but7 = (Button) findViewById(R.id.Go_button);
        Input1 = (EditText) findViewById(R.id.editText);
        Input2 = (EditText) findViewById(R.id.editText2);
        sif = 5;
        but7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate_Turns();
                Calulate_Distance();
                Clock_Screen();
                try {
                    Navigation_Cycle();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
        but6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useCurrentAsStartPoint();
            }
        });
        count = 0;
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        Button button = findViewById(R.id.getLocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLoc();
                //TextView f = findViewById(R.id.Kale);
                //f.setText("Hello No");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        getLoc();
                        //TextView t = findViewById(R.id.Kale);
                        //t.setText("Hello Yes");
                    }
                }, 20000);

               /* if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                    return;
                }
                //while(on) {
                    client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                TextView textView = findViewById(R.id.location);
                                TextView ted = findViewById(R.id.location2);
                                double temp = location.getLatitude();
                                double temp2 = location.getLongitude();
                                textView.setText(Double.toString(temp));
                                ted.setText(Double.toString(temp2));
                            }

                        }
                    });*/
                //}

            }
        });

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
            End_Of_Instructions = false;
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
        count = count - 1;
        try {
            if(count < Latitudes.length && count >= 0) {
                End_Of_Instructions = false;
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
                End_Of_Instructions = true;
            }
        }
        catch(IOException e){

        }
    }
    public void Forward(){
        //count = count + 3;
        try {
            if(count < Latitudes.length) {
                End_Of_Instructions = false;
                snapToRoads(Latitudes[count], Longitudes[count]);
                count = count + 1;
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
                End_Of_Instructions = true;
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
        double latitude = 0;
        double longitude = 0;
        if(!CurrentAsOrigin) {
            String s = Input1.getText().toString();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            addresses = geocoder.getFromLocationName(s, 1);

            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            }
            origin = new LatLng(latitude, longitude);
            String out = Double.toString(latitude) + " " + Double.toString(longitude);
            Input1.setText(out);
        }
        else{
            CurrentAsOrigin = false;
        }
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
    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }
    private void getLoc(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    TextView textView = findViewById(R.id.toLat);
                    TextView ted = findViewById(R.id.toLong);
                    double temp = location.getLatitude();
                    CurrentLat = temp;
                    double temp2 = location.getLongitude();
                    CurrentLong = temp2;
                    textView.setText(Double.toString(temp));
                    ted.setText(Double.toString(temp2));
                }

            }
        });
    }
    public void useCurrentAsStartPoint(){
        origin = new LatLng(CurrentLat,CurrentLong);
        String out = Double.toString(CurrentLat) + " " + Double.toString(CurrentLong);
        Input1.setText(out);
        CurrentAsOrigin = true;
    }
    public void Clock_Screen(){
        char[] For_Transmit = Formatter.GoToClock();
        /// Transmit For_Transmit///
    }
    public void Navigation_Cycle() throws InterruptedException {
        int instruction = 0;
        char[] For_Transmit = Formatter.RecieveBluetooth();
        ///Transmit For_Transmit ///
        wait(500);
        String street = CurrentStreet;
        String Distance = Distances.elementAt(instruction).toString();
        String Next_Street = NextStreet.elementAt(instruction);
        CalculateDirections Cal = new CalculateDirections(Latitudes,Longitudes,Turn_Index);
        int Turn_No = 0;
        String Direction = Cal.CalcVectors(Turn_No);
        Turn_No++;
        For_Transmit = Formatter.StartNav(street,Distance,Next_Street,Direction);
        ///Transmit For Transmit ///
        boolean arrived = false;
        while(!arrived){
            if(Calculate_Instant_Distance(Turn_No) < 10 && Turn_No != (Next_Street.length()-2)){
                boolean turned = false;
                For_Transmit = Formatter.SwitchToTurn(Next_Street,Direction);
                ///Transmit For_Transmit ///
                while(!turned){
                    double dist = Calculate_Instant_Distance(Turn_No);
                    if(dist > 10){
                        turned = true;
                    }
                }
                Turn_No++;
                CurrentStreet = Next_Street;
                street = CurrentStreet;
                Distance = Distances.elementAt(Turn_No).toString();
                Next_Street = NextStreet.elementAt(Turn_No);
                Direction = Cal.CalcVectors(Turn_No);
                For_Transmit = Formatter.ReturnToGeneral(street,Distance,Next_Street,Direction);
                ///Transmit For_Transmit///
            }
            else if(Turn_No != (Next_Street.length()-2)){
                wait(1000);
                Distance = Calculate_Instant_Distance(Turn_No++).toString();
                For_Transmit = Formatter.ReturnToGeneral(street,Distance,Next_Street,Direction);
                ///Transmit For_Transmit///
            }
            else{
                For_Transmit = Formatter.ArrivalScreen(street);
                ///Transmit For_Transmit///
            }
        }
    }
    public void Calculate_Turns(){
        Forward();
        int star = 0;
        String hold = CurrentStreet;
        for(int i = 0; !End_Of_Instructions ;i++){
            Forward();
            if(CurrentStreet.equals(hold)){

            }
            else{
                Turn_Lat.addElement(Latitudes[i]);
                Turn_Long.addElement(Longitudes[i]);
                NextStreet.addElement(CurrentStreet);
                hold = CurrentStreet;
                Turn_Index[star] = i;
                star++;
            }
        }
        count = 0;
    }
    public void Calulate_Distance(){
        double R = 6371e3;
        for(int i = 0; i < Turn_Lat.size();i++){
            getLoc();
            double phi1 = CurrentLat;
            double lambda1 = CurrentLong;
            double phi2 = Turn_Lat.elementAt(i);
            double lambda2 = Turn_Long.elementAt(i);
            double delta_phi = ToRadians(phi1) - ToRadians(phi2);
            double delta_lambda = ToRadians(lambda1) - ToRadians(lambda2);
            double a = (Math.sin(delta_phi/2)*Math.sin(delta_phi/2)) + Math.cos(ToRadians(phi1))*Math.cos(ToRadians(phi2))*(Math.sin(delta_lambda/2)*Math.sin(delta_lambda/2));
            double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
            double d = R*c;
            Distances.addElement(d);
        }
    }
    public Double Calculate_Instant_Distance(int i){
        double R = 6371e3;
        getLoc();
        double phi1 = CurrentLat;
        double lambda1 = CurrentLong;
        double phi2 = Turn_Lat.elementAt(i);
        double lambda2 = Turn_Long.elementAt(i);
        double delta_phi = ToRadians(phi1) - ToRadians(phi2);
        double delta_lambda = ToRadians(lambda1) - ToRadians(lambda2);
        double a = (Math.sin(delta_phi/2)*Math.sin(delta_phi/2)) + Math.cos(ToRadians(phi1))*Math.cos(ToRadians(phi2))*(Math.sin(delta_lambda/2)*Math.sin(delta_lambda/2));
        double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        return R*c;

    }
    public double ToRadians(double in){
        return in*(Math.PI/180);
    }

}
