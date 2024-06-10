package com.example.lab6;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.dtos.Ingresos;


import java.util.List;

public class IngresosAdapter extends RecyclerView.Adapter<IngresosAdapter.IngresosViewHolder> {
    private List<Ingresos> ingresosList;
    private Context context;

    public IngresosAdapter(List<Ingresos> ingresosList, Context context) {
        this.ingresosList = ingresosList;
        this.context = context;
    }

    @NonNull
    @Override
    public IngresosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingresos, parent, false);
        return new IngresosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngresosViewHolder holder, int position) {
        Ingresos ingresos = ingresosList.get(position);
        holder.textViewTitle.setText(ingresos.getTitulo());
        holder.textViewDescription.setText(ingresos.getDescripcion());
        holder.textViewDueDate.setText(ingresos.getDueDate().toString());
        holder.monto.setText(String.valueOf(ingresos.getMonto()));

        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AgregarEditarIngresosActivity.class);
            intent.putExtra("ingresos", ingresos);
            intent.putExtra("ingresosIndex", position);
            ((MainActivity) context).startActivityForResult(intent, MainActivity.EDIT_TASK_REQUEST);
        });

        // Configurar el botón de eliminación
        holder.buttonDelete.setOnClickListener(v -> {
            //
        });


    }

    @Override
    public int getItemCount() {
        return ingresosList.size();
    }

    static class IngresosViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription, textViewDueDate,monto;
        Button buttonEdit, buttonDelete;
        CardView cardView;

        IngresosViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            monto = itemView.findViewById(R.id.monto);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

