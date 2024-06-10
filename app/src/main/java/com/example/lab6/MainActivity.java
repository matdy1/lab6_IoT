package com.example.lab6;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.databinding.ActivityMainBinding;
import com.example.lab6.dtos.Ingresos;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    private IngresosAdapter ingresosAdapter;
    private static final int TOAST_DURATION = Toast.LENGTH_LONG;
    ActivityMainBinding binding;
    FirebaseUser currentUser;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.recyclerView);  // Asegurarte de que el ID es correcto
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadIngresosData();

        ImageView profileButton = findViewById(R.id.profileButtonPopup);

        FloatingActionButton fabAddTask = findViewById(R.id.agregarIngreso);
        fabAddTask.setOnClickListener(v -> {
            Intent addTaskIntent = new Intent(MainActivity.this, AgregarEditarIngresosActivity.class);
            startActivityForResult(addTaskIntent, ADD_TASK_REQUEST);
        });

        Button ingresos = findViewById(R.id.ingresos);
        ingresos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgregarEditarIngresosActivity.class);
            startActivity(intent);
        });

        Button egresos = findViewById(R.id.egresos);
        egresos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EgresosActivity.class);
            startActivity(intent);
        });

        Button graficas = findViewById(R.id.graficas);
        graficas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResumenActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_profile, null);

            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.setBackgroundDrawable(new ColorDrawable());
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            dimBehind(popupWindow);

            TextView textViewName = popupView.findViewById(R.id.textViewName);
            TextView textViewEmail = popupView.findViewById(R.id.textViewEmail);

            if (currentUser != null) {
                textViewName.setText(currentUser.getDisplayName());
                textViewEmail.setText(currentUser.getEmail());
            } else {
                Toast.makeText(MainActivity.this, "No está logueado", TOAST_DURATION).show();
            }

            Button buttonLogout = popupView.findViewById(R.id.buttonLogout);
            buttonLogout.setOnClickListener(v1 -> {
                AuthUI.getInstance().signOut(MainActivity.this)
                        .addOnCompleteListener(task -> {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
            });
        });
    }

    private void loadIngresosData() {
        db.collection("ingreso")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Ingresos> ingresosList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ingresos ingresos = document.toObject(Ingresos.class);
                            ingresos.setId(document.getId()); // Almacenar el ID del documento
                            ingresosList.add(ingresos);
                        }
                        ingresosAdapter = new IngresosAdapter(ingresosList, this);
                        recyclerView.setAdapter(ingresosAdapter);
                    } else {
                        Toast.makeText(MainActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dimBehind(PopupWindow popupWindow) {
        View container = popupWindow.getContentView().getRootView();
        WindowManager wm = (WindowManager) container.getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);
    }
}
