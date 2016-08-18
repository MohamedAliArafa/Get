package com.zeowls.get;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zeowls.get.BackEnd.Core;
import com.zeowls.get.DataModel.ShopDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static ArrayList<ShopDataModel> shops = new ArrayList<>();
    private static View view;
    private BottomSheetBehavior mBottomSheetBehavior;
    private GoogleMap mMap;
    public Context c;
    RelativeLayout Map_Info;
    SupportMapFragment mapFragment;
    getAllShops getAllShops;
    protected GoogleApiClient mGoogleApiClient;
    protected static final String TAG = "Maps Activity";
    protected Location mLastLocation;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    Boolean enableGPS = false;


    public HomeMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        buildGoogleApiClient();
        mapFragment.getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_home_map, container, false);
        } catch (InflateException e) {
                                                                                                                                                                    /* map is already there, just return view as it is */
        }
        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        getAllShops = new getAllShops();
        if (getAllShops.getStatus() != AsyncTask.Status.RUNNING) {
            getAllShops.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request missing location permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // Location permission has been granted, continue as usual.

            mMap.setMyLocationEnabled(true);


        }

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {


                if (!((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE))
                        .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Intent gpsOptionsIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);

                    //prompt user to enable gps
                } else {
                    //gps is enabled
                }


                //               Location myLocation =  mMap.getMyLocation();
                //               if (myLocation!=null) {
                //                   LatLng loc = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                //                   Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc));
                //                   if (mMap != null) {
                //                       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                //                   }
                //               }

                return false;
            }

        });


        if (!((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.Location_Not_Enabled)
                    .setMessage(R.string.Please_turn_on_gps_first)
                    .setPositiveButton(R.string.Turn_On, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            enableGPS = true;
                            Intent gpsOptionsIntent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(gpsOptionsIntent);
                        }
                    })
                    .setNegativeButton(R.string.No_Map, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();


        }


    }


    private class getAllShops extends AsyncTask {

        @Override
        protected void onPreExecute() {
            shops.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (shops.size() != 0) {
                Log.d("Shop Array ", shops.get(0).getName());
                for (int i = 0; i < shops.size(); i++) {
                    if (!shops.get(i).getLatitude().equals("null") && !shops.get(i).getLongitude().equals("null")) {

                        LatLng sydney = new LatLng(Double.valueOf(shops.get(i).getLatitude()),Double.valueOf(shops.get(i).getLongitude()));
                        mMap.addMarker(new MarkerOptions().position(sydney).title(shops.get(i).getName()));
                        Log.d("Markers", String.valueOf(i));

                    }
                }
            } else {
                Log.d("Shop Array ", " Empty");

            }
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getActivity());
                JSONArray itemsarray = core.getAllShops();
                //Log.d("ShopsJsonArray", itemsarray.toString());
                if (itemsarray.length() != 0) {
                    for (int i = 0; i < itemsarray.length(); i++) {
                        JSONObject item = itemsarray.getJSONObject(i);
                        //Log.d("ShopsJsonObject", item.toString());
                        ShopDataModel shop = new ShopDataModel();
                        shop.setId(item.getInt("id"));
                        shop.setName(item.getString("name"));
                        if (item.getString("description").equals("null") || item.getString("description").isEmpty())
                            shop.setDescription("No Description Available");
                        else
                            shop.setDescription(item.getString("description"));
                        //                        shop.setOwner(item.getString("owner"));
                        if (!item.getString("profile_pic").equals("null")) {
                            shop.setPictureUrl(item.getString("profile_pic"));
                        }
                        //shop.setOwner(item.getString("owner"));
                        if (!item.isNull("profile_pic")) {
                            shop.setPictureUrl(item.getString("profile_pic"));
                        } else {
                            shop.setPictureUrl("null");
                        }

                        if (!item.isNull("latitude")) {
                            shop.setLatitude(item.getString("latitude"));
                            Log.d("Latitude : ", item.getString("latitude"));
                        } else {
                            shop.setLatitude("null");
                        }

                        if (!item.isNull("longitude")) {
                            shop.setLongitude(item.getString("longitude"));
                            Log.d("longitude : ", item.getString("longitude"));
                        } else {
                            shop.setLongitude("null");
                        }


                        if (!item.isNull("description")) {
                            shop.setDescription(item.getString("description"));
                        } else {
                            shop.setDescription("");
                        }
                        shops.add(shop);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        //        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //            // TODO: Consider calling
        //            //    ActivityCompat#requestPermissions
        //            // here to request the missing permissions, and then overriding
        //            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //            //                                          int[] grantResults)
        //            // to handle the case where the user grants the permission. See the documentation
        //            // for ActivityCompat#requestPermissions for more details.
        //            return;
        //        }


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request missing location permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            );
        } else {
            // Location permission has been granted, continue as usual.


            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mMap.setMyLocationEnabled(true);

            if (mLastLocation != null) {
                //            mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel,
                //                    mLastLocation.getLatitude()));
                //            mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel,
                //                    mLastLocation.getLongitude()));
                Log.d("User Lat ", String.valueOf(mLastLocation.getLatitude()));
                Log.d("User Lat ", String.valueOf(mLastLocation.getLongitude()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom
                        (new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 12.25f));


            } else {
                Toast.makeText(getActivity(), R.string.No_location_Deteccetd, Toast.LENGTH_LONG).show();
            }


        }

        //  mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    public void onStart() {

        mGoogleApiClient.connect();
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


}
