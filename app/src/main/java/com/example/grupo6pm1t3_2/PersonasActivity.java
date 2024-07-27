package com.example.grupo6pm1t3_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PersonasActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas);

        // Inicializar la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance("https://grupo6firebasecrud-default-rtdb.firebaseio.com/").getReference("personas");


        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnRead = findViewById(R.id.btnRead);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnDelete = findViewById(R.id.btnDelete);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPerson();
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readPerson();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePerson();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePerson();
            }
        });
    }

    private void createPerson() {
        // Crear una nueva persona
        String personId = databaseReference.push().getKey();
        Personas nuevaPersona = new Personas(personId, "Juan", "Perez", "juan.perez@example.com", "01-01-1990", "url_de_la_foto");
        databaseReference.child(personId).setValue(nuevaPersona)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Éxito
                        } else {
                            // Error
                        }
                    }
                });
    }

    private void readPerson() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Personas persona = snapshot.getValue(Personas.class);
                    // Procesar la persona
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores
            }
        });
    }

    private void updatePerson() {
        String personId = "ID_DE_LA_PERSONA"; // Reemplaza con el ID de la persona a actualizar
        DatabaseReference personReference = databaseReference.child(personId);

        // Actualizar los datos de la persona
        Map<String, Object> actualizaciones = new HashMap<>();
        actualizaciones.put("nombres", "Juan Carlos");
        personReference.updateChildren(actualizaciones)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Éxito
                        } else {
                            // Error
                        }
                    }
                });
    }

    private void deletePerson() {
        String personId = "ID_DE_LA_PERSONA"; // Reemplaza con el ID de la persona a eliminar
        DatabaseReference personReference = databaseReference.child(personId);

        // Eliminar la persona
        personReference.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Éxito
                        } else {
                            // Error
                        }
                    }
                });
    }
}