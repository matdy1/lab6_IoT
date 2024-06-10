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

import com.example.lab6.dtos.Egresos;

import java.util.List;

public class EgresosAdapter extends RecyclerView.Adapter<EgresosAdapter.EgresosViewHolder> {
    private List<Egresos> egresosList;
    private Context context;

    public EgresosAdapter(List<Egresos> egresosList, Context context) {
        this.egresosList = egresosList;
        this.context = context;
    }

    @NonNull
    @Override
    public EgresosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_egresos, parent, false);
        return new EgresosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EgresosViewHolder holder, int position) {
        Egresos egresos = egresosList.get(position);
        holder.textViewTitle.setText(egresos.getTitulo());
        holder.textViewDescription.setText(egresos.getDescripcion());
        holder.textViewDueDate.setText(egresos.getDueDate().toString());
        holder.monto.setText(String.valueOf(egresos.getMonto()));

        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AgregarEditarIngresosActivity.class);
            intent.putExtra("egresos", egresos);
            intent.putExtra("egresosIndex", position);
            ((MainActivity) context).startActivityForResult(intent, MainActivity.EDIT_TASK_REQUEST);
        });

        // Configurar el botón de eliminación
        holder.buttonDelete.setOnClickListener(v -> {
            //
        });



    }

    @Override
    public int getItemCount() {
        return egresosList.size();
    }

    static class EgresosViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription, textViewDueDate,monto;
        Button buttonEdit, buttonDelete;
        CardView cardView;

        EgresosViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            monto = itemView.findViewById(R.id.monto);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

