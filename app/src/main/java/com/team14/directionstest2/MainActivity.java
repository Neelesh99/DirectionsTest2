package com.team14.directionstest2;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.RoadsApi;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private String[] stockArr;
    ArrayList<LatLng> directionPosList;
    private GeoApiContext mContext;
    private Button but1;
    private int count;
    private double[] Latitudes;
    private double[] Longitudes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverKey = "AIzaSyAU-jSsThQo2f4Ne0ijd8qScR67JFaeHKY";
        origin = new LatLng(51.512792, -0.177116);;
        destination = new LatLng(51.498857, -0.176238);
        view = (TextView) findViewById(R.id.textview);
        view1 = (TextView) findViewById(R.id.textView);
        view2 = (TextView) findViewById(R.id.textView2);
        view3 = (TextView) findViewById(R.id.textView3);
        view4 = (TextView) findViewById(R.id.textView4);
        but1 = (Button) findViewById(R.id.button2);
        but1.setOnClickListener(this);
        mContext = new GeoApiContext().setApiKey("AIzaSyAU-jSsThQo2f4Ne0ijd8qScR67JFaeHKY");
        requestDirection();
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
            String s = directionPosList.get(3).toString();
            String b = directionPosList.get(6).toString();
            String e = directionPosList.get(8).toString();
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
            view1.setText(b);
            view2.setText(e);
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
    @Override
    public void onClick(View v){
        try {
            if(count < Latitudes.length) {
                snapToRoads(Latitudes[count], Longitudes[count]);
                count = count + 3;
            }
            else{
                view3.setText("End of Instructions");
            }
        }
        catch(IOException e){

        }

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
        view3.setText(address);
        view4.setText(knownName);

    }

}
