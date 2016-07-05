package mkt.com.ar.vigilante;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity  {
    TextView tvLatitud;
    TextView tvLongitud;
    TextView tvVelocidadActual;
    EditText etNombreVendedor;
    EditText etIdVendedor;
    Button btnIniciarServicio;
    Button btnPararServicio;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference referenciaVendedores;
    GoogleMap googleMap;
    Marker now;
    Marker vendedor1;
    Marker vendedor2;
    Marker vendedor3;
    Marker vendedor4;

    public static final int MY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLatitud = (TextView) findViewById(R.id.tvLatitud);
        tvLongitud = (TextView) findViewById(R.id.tvLongitud);
        tvVelocidadActual = (TextView) findViewById(R.id.tvVelocidadActial);
        etNombreVendedor = (EditText) findViewById(R.id.etNombreVendedor);
        etIdVendedor = (EditText) findViewById(R.id.etIdVendedor);
        btnIniciarServicio = (Button) findViewById(R.id.btnIniciarServicio);
        btnPararServicio = (Button) findViewById(R.id.btnPararServicio);
        //btnPararServicio.setEnabled(false);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);


        // Getting GoogleMap object from the fragment
        googleMap = mapFragment.getMap();

        LatLng punto = new LatLng(-24.81508669, -65.43448549);
        now = googleMap.addMarker(new MarkerOptions().position(punto));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(punto));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


    }

    @Override
    protected void onStart() {
        super.onStart();

        //Escuchamos por posiciones de vendedores
        database = FirebaseDatabase.getInstance();
        referenciaVendedores = database.getReference(); //Hacemos referencia al nodo que corresponde al vendedor
        DatabaseReference referenciaVendedores = database.getReference("vendedores/" + String.valueOf(globalVariable.getVendedorId()) + "/posicion/");
        // myRef.child("vendedores").child(String.valueOf(globalVariable.getVendedorId()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Coordenada coordenada = dataSnapshot.getValue(Coordenada.class);
                if (coordenada != null) {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btnIniciarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Seteamos el vendedorId y el nombre de vendedor ingresado en la variable global
                final ClaseGlobal globalVariable = (ClaseGlobal) getApplicationContext();
                globalVariable.setVendedorId(Integer.valueOf(etIdVendedor.getText().toString()));
                globalVariable.setNombreVendedor(etNombreVendedor.getText().toString());

                //Una vez que tenemos la referencia al vendedor referenciamos firebase
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference(); //Hacemos referencia al nodo que corresponde al vendedor
                DatabaseReference myRef = database.getReference("vendedores/" + String.valueOf(globalVariable.getVendedorId()) + "/posicion/");
               // myRef.child("vendedores").child(String.valueOf(globalVariable.getVendedorId()));
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    Coordenada coordenada = dataSnapshot.getValue(Coordenada.class);
                        if (coordenada != null) {
                            tvLongitud.setText(String.valueOf(coordenada.getLongitud()));
                            tvLatitud.setText(String.valueOf(coordenada.getLatitud()));
                            tvVelocidadActual.setText("---");
                            LatLng punto = new LatLng(coordenada.getLatitud(), coordenada.getLongitud());
                            // now.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icono_marcador));


                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(punto));
                            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                            animateMarker(now, punto, false);
                        /*now = googleMap.addMarker(new MarkerOptions().position(punto));
                        // Showing the current location in Google Map

                        // Zoom in the Google Map
                       */
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                //Apenas se cree la actividad Iniciamos el servioco

                //chequeamos permisos en runtime (requisito API 23)
                if (ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.INTERNET) ==
                                PackageManager.PERMISSION_GRANTED

                        ){
                    Intent myIntent = new Intent(view.getContext(), AndroidLocationServices.class);
                    startService(myIntent);
                } else {

                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET }, MY_PERMISSIONS_REQUEST);

                    Toast.makeText(view.getContext(), "No tiene permisos de localizacion", Toast.LENGTH_LONG).show();
                }



                //Deshabilitamos controles
                etIdVendedor.setEnabled(false);
                etIdVendedor.setFocusable(false);
                etNombreVendedor.setEnabled(false);
                etNombreVendedor.setFocusable(false);

                //Habilitamos boton de parar
                btnPararServicio.setEnabled(true);
                btnIniciarServicio.setEnabled(false);
            }
        });

        btnPararServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Apenas se cree la actividad Iniciamos el servioco
                Intent myIntent = new Intent(view.getContext(), AndroidLocationServices.class);
                stopService(myIntent);

                //Deshabilitamos controles
                etIdVendedor.setEnabled(true);
                etIdVendedor.setFocusableInTouchMode(true);
                etNombreVendedor.setEnabled(true);
                etNombreVendedor.setFocusableInTouchMode(true);

                btnIniciarServicio.setEnabled(true);
                btnPararServicio.setEnabled(false);
            }
        });
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

    }*/

   /* @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/

    public void animateMarker(final Marker marker, final LatLng toPosition,final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e("PERMI", "le dio permiso");
                    Intent myIntent = new Intent(this, AndroidLocationServices.class);
                    startService(myIntent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
