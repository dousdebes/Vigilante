package mkt.com.ar.vigilante;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView tvLatitud;
    TextView tvLongitud;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Apenas se cree la actividad Iniciamos el servioco
        Intent myIntent = new Intent(this, AndroidLocationServices.class);
        startService(myIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        tvLatitud = (TextView) findViewById(R.id.tvLatitud);
        tvLongitud = (TextView) findViewById(R.id.tvLongitud);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("coordenadas");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Coordenada coordenada = dataSnapshot.getValue(Coordenada.class);
                tvLongitud.setText(String.valueOf(coordenada.getLongitud()));
                tvLatitud.setText(String.valueOf(coordenada.getLatitud()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
