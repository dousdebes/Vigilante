package mkt.com.ar.vigilante;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.SyncStateContract;
import android.util.Log;
import android.net.NetworkInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alberto on 08/06/2016.
 */
public class AndroidLocationServices extends Service {

    PowerManager.WakeLock wakeLock;

    private LocationManager locationManager;

    public AndroidLocationServices() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");

        // Toast.makeText(getApplicationContext(), "Service Created",
        // Toast.LENGTH_SHORT).show();
        // this acquires the wake lock
        wakeLock.acquire();
        Log.e("Google", "Service Created");

    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

        Toast.makeText(this, "Servicio Iniciado", Toast.LENGTH_SHORT).show();
        //Log.e("Google", "Service Started");

        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, listener); //Update location cada 500ms o 0 mts
        } catch (SecurityException  e) {

            Log.e("Permisos", "No tiene permisos");
        }

    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub


            Log.e("Google", "Location Changed");

            if (location == null)
                return;

            if (isConnectingToInternet(getApplicationContext())) {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                try {
                    Log.e("latitude", location.getLatitude() + "");
                    Log.e("longitude", location.getLongitude() + "");

                    jsonObject.put("latitude", location.getLatitude());
                    jsonObject.put("longitude", location.getLongitude());

                    jsonArray.put(jsonObject);
                    final ClaseGlobal globalVariable = (ClaseGlobal) getApplicationContext();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //Nodo principal
                    DatabaseReference myRef = database.getReference("vendedores/" + String.valueOf(globalVariable.getVendedorId()));
                    String key = myRef.push().getKey();
                    Coordenada  coordenada = new Coordenada();
                    //Seteamos los valores
                    coordenada.setVendedorId(globalVariable.getVendedorId());
                    coordenada.setDisplayName(globalVariable.getNombreVendedor());
                    coordenada.setLatitud(location.getLatitude());
                    coordenada.setLongitud(location.getLongitude());
                    coordenada.setVelocidadActual(location.getSpeed());
                    coordenada.setTimestamp(new Date().getTime());


                    Map<String, Object> coordenadaValues = coordenada.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/posicion/", coordenadaValues);
                    childUpdates.put("/historial/" + key, coordenadaValues);
                    myRef.updateChildren(childUpdates);
                    Log.e("request", jsonArray.toString());

                } catch (Exception e) {
                    // TODO Auto-generated catch block

                    e.printStackTrace();
                }

            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        Toast.makeText(this, "Servicio Detenido", Toast.LENGTH_SHORT).show();
        super.onDestroy();

        wakeLock.release();

    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

}