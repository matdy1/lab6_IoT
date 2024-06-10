package com.example.lab6;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab6.databinding.ActivityAgregarEditarIngresosBinding;
import com.example.lab6.dtos.Ingresos;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AgregarEditarEgresosActivity extends AppCompatActivity {
    private Calendar dateTime;
    FirebaseFirestore db;
    ActivityAgregarEditarIngresosBinding binding;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgregarEditarIngresosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        dateTime = Calendar.getInstance();

        binding.textViewDateTime.setOnClickListener(v -> showDateTimePicker());

        binding.buttonSave.setOnClickListener(v -> {
            String titulo = binding.editTextTitle.getText().toString();
            String descripcion = binding.editTextDescription.getText().toString();
            double monto = Double.parseDouble(binding.editTextMonto.getText().toString());
            String fechaStr = binding.textViewDateTime.getText().toString();

            java.sql.Date fecha = null;
            try {
                fecha = new java.sql.Date(sdf.parse(fechaStr).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
                return;
            }

            Ingresos ingresos = new Ingresos();
            ingresos.setTitulo(titulo);
            ingresos.setDescripcion(descripcion);
            ingresos.setMonto(monto);
            ingresos.setDueDate(fecha);

            db.collection("egreso")
                    .add(ingresos)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Egreso guardado", Toast.LENGTH_SHORT).show();
                        // Redirigir a otra actividad
                        Intent intent = new Intent(AgregarEditarEgresosActivity.this, EgresosActivity.class);
                        startActivity(intent);
                        finish(); // Opcional: Llama a finish() si deseas cerrar la actividad actual
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    });
        });

    }

    private void showDateTimePicker() {
        Calendar currentDateTime = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, month);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dateTime.set(Calendar.MINUTE, minute);
                binding.textViewDateTime.setText(sdf.format(dateTime.getTime()));
            }, currentDateTime.get(Calendar.HOUR_OF_DAY), currentDateTime.get(Calendar.MINUTE), true).show();
        }, currentDateTime.get(Calendar.YEAR), currentDateTime.get(Calendar.MONTH), currentDateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean isInputValid() {
        if (binding.editTextTitle.getText().toString().trim().isEmpty() || binding.editTextDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Título y Descripción no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}