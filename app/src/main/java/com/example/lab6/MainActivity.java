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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    private IngresosAdapter ingresosAdapter;
    private static final int TOAST_DURATION = Toast.LENGTH_LONG;
    ActivityMainBinding binding;
    FirebaseUser currentUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ingresosAdapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ImageView profileButton = findViewById(R.id.profileButtonPopup);
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

    private void dimBehind(PopupWindow popupWindow) {
        View container = popupWindow.getContentView().getRootView();
        WindowManager wm = (WindowManager) container.getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);
    }
}
