package com.example.grupo6pm1t3_2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo6pm1t3_2.modelo.Personas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertarPersonaActivity extends AppCompatActivity {

    private EditText editTextNombres, editTextApellidos, editTextCorreo, editTextFechanac, editTextFoto;
    private Button btnInsertar;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_persona);

        // Inicializar vistas
        editTextNombres = findViewById(R.id.editTextNombres);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextFechanac = findViewById(R.id.editTextFechanac);
        editTextFoto = findViewById(R.id.editTextFoto);
        btnInsertar = findViewById(R.id.btnInsertar);

        // Inicializar la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance("https://grupo6firebasecrud-default-rtdb.firebaseio.com/").getReference("personas");

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarPersona();
            }
        });
    }

    private void insertarPersona() {
        String nombres = editTextNombres.getText().toString();
        String apellidos = editTextApellidos.getText().toString();
        String correo = editTextCorreo.getText().toString();
        String fechanac = editTextFechanac.getText().toString();
        String foto = editTextFoto.getText().toString();

        if (!nombres.isEmpty() && !apellidos.isEmpty() && !correo.isEmpty() && !fechanac.isEmpty() && !foto.isEmpty()) {
            String personId = databaseReference.push().getKey();
            Personas nuevaPersona = new Personas(personId, nombres, apellidos, correo, fechanac, foto);

            databaseReference.child(personId).setValue(nuevaPersona)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(InsertarPersonaActivity.this, "Persona insertada exitosamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(InsertarPersonaActivity.this, "Error al insertar persona", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
