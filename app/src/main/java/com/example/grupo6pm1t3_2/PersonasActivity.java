package com.example.grupo6pm1t3_2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo6pm1t3_2.adapter.PersonasAdapter;
import com.example.grupo6pm1t3_2.modelo.Personas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.HashMap;
import java.util.Map;

public class PersonasActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private PersonasAdapter adapter;
    private EditText editTextNombres, editTextApellidos, editTextCorreo, editTextFechanac, editTextFoto, editTextPersonId;
    private Button btnCreate, btnRead, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas);

        // Inicializar la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance("https://grupo6firebasecrud-default-rtdb.firebaseio.com/").getReference("personas");

        // Configurar campos de entrada y botones
        editTextNombres = findViewById(R.id.editTextNombres);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextFechanac = findViewById(R.id.editTextFechanac);
        editTextFoto = findViewById(R.id.editTextFoto);
        editTextPersonId = findViewById(R.id.editTextPersonId);
        btnCreate = findViewById(R.id.btnCreate);
        btnRead = findViewById(R.id.btnRead);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Personas> options = new FirebaseRecyclerOptions.Builder<Personas>()
                .setQuery(databaseReference, Personas.class)
                .build();

        adapter = new PersonasAdapter(options, this::onPersonClick);
        recyclerView.setAdapter(adapter);

        // Configurar eventos de clic en los botones
        btnCreate.setOnClickListener(v -> createPerson());
        btnRead.setOnClickListener(v -> readPerson());
        btnUpdate.setOnClickListener(v -> updatePerson());
        btnDelete.setOnClickListener(v -> deletePerson());
    }

    private void onPersonClick(Personas persona) {
        // Mostrar los detalles de la persona seleccionada en los campos de entrada
        editTextNombres.setText(persona.getNombres());
        editTextApellidos.setText(persona.getApellidos());
        editTextCorreo.setText(persona.getCorreo());
        editTextFechanac.setText(persona.getFechanac());
        editTextFoto.setText(persona.getFoto());
        editTextPersonId.setText(persona.getId());
    }

    private void createPerson() {
        // Generar un ID único
        String personId = databaseReference.push().getKey();

        // Verificar si el ID se generó correctamente
        if (personId == null) {
            Toast.makeText(this, "Error al generar ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear una nueva persona con los datos ingresados por el usuario
        String nombres = editTextNombres.getText().toString();
        String apellidos = editTextApellidos.getText().toString();
        String correo = editTextCorreo.getText().toString();
        String fechanac = editTextFechanac.getText().toString();
        String foto = editTextFoto.getText().toString();

        Personas nuevaPersona = new Personas(personId, nombres, apellidos, correo, fechanac, foto);

        // Insertar la nueva persona en la base de datos
        databaseReference.child(personId).setValue(nuevaPersona)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PersonasActivity.this, "Persona creada", Toast.LENGTH_SHORT).show();
                        // Limpiar los campos después de crear la persona
                        clearFields();
                    } else {
                        Toast.makeText(PersonasActivity.this, "Error al crear persona", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void readPerson() {
        // La lectura de datos ya se maneja automáticamente por el adaptador FirebaseRecyclerAdapter
    }

    private void updatePerson() {
        String personId = editTextPersonId.getText().toString();
        if (personId.isEmpty()) {
            Toast.makeText(this, "ID de persona no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference personReference = databaseReference.child(personId);

        // Obtener datos actualizados
        String nombres = editTextNombres.getText().toString();
        String apellidos = editTextApellidos.getText().toString();
        String correo = editTextCorreo.getText().toString();
        String fechanac = editTextFechanac.getText().toString();
        String foto = editTextFoto.getText().toString();

        // Crear un mapa de actualizaciones
        Map<String, Object> actualizaciones = new HashMap<>();
        actualizaciones.put("nombres", nombres);
        actualizaciones.put("apellidos", apellidos);
        actualizaciones.put("correo", correo);
        actualizaciones.put("fechanac", fechanac);
        actualizaciones.put("foto", foto);

        personReference.updateChildren(actualizaciones).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PersonasActivity.this, "Persona actualizada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PersonasActivity.this, "Error al actualizar persona", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePerson() {
        String personId = editTextPersonId.getText().toString();
        if (personId.isEmpty()) {
            Toast.makeText(this, "ID de persona no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference personReference = databaseReference.child(personId);

        // Eliminar la persona
        personReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PersonasActivity.this, "Persona eliminada", Toast.LENGTH_SHORT).show();
                // Limpiar los campos después de eliminar la persona
                clearFields();
            } else {
                Toast.makeText(PersonasActivity.this, "Error al eliminar persona", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        editTextNombres.setText("");
        editTextApellidos.setText("");
        editTextCorreo.setText("");
        editTextFechanac.setText("");
        editTextFoto.setText("");
        editTextPersonId.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
