package co.edu.utbvirtual.mapaseindicaciones;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView txtLatitude;
    TextView txtLongitude, campoLog;
    Button refresh;
    private static final int  MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    AsyncHttpClient client = new AsyncHttpClient();
    private Location mLastLocation;
    public LocationManager mLocationManager;
    private double latitud;
    private double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitude = (TextView) findViewById(R.id.txtLatitud2);
        txtLongitude = (TextView) findViewById(R.id.txtLongitud2);
        campoLog = (TextView) findViewById(R.id.txtJSON);


        int LOCATION_REFRESH_TIME = 10;
        int LOCATION_REFRESH_DISTANCE = 5;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);
            return;
        }

        refresh = (Button) findViewById(R.id.button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleJSON();

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("latitud", latitud);
                intent.putExtra("longitud", longitud);
                startActivity(intent);

            }
        });
    }
    private void SimpleJSON() {
        /*
         * Se hace el request y se usa como argumento una subclase (anónima? Cual sería el término
         * correcto en este caso?) de JsonHttpResponseHandler, sobreescribiendo sólo el método de
         * request exitoso (onSuccess)
         */
        client.get("http://labsoftware03.unitecnologica.edu.co/archivoNicolas", null, new JsonHttpResponseHandler() {
            /**
             * Handler de evento "request exitoso."
             *
             * @param statusCode Código HTTP de la respuesta del servidor.
             * @param headers Headers HTTP de respuesta del servidor.
             * @param response Objeto JSON recibido.
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // Mostrando la respuesta en LogCat y en campoLog, en la Activity.
                    Log.v("Respuesta JSON:", response.getString("latitud"));
                    latitud = Double.parseDouble(response.getString("latitud"));
                    longitud = Double.parseDouble(response.getString("longitud"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /**
             * Handler de evento "request exitoso.
             *
             * @param statusCode Código HTTP de la respuesta del servidor.
             * @param headers Headers HTTP de respuesta del servidor.
             * @param response Objeto JSON recibido.
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.v("Respuesta JSON:", response.toString());
            }
        });
    }



    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //code
            System.out.println("onLocationChanged");

            mLastLocation = location;

            txtLatitude.setText(String.valueOf(location.getLatitude()));
            txtLongitude.setText(String.valueOf(location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
            //turns off gps services
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
       //     return true;
        //}

        return super.onOptionsItemSelected(item);
    }
    //---- Below here is from what the class implements
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}