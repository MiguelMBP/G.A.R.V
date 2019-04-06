package com.example.android.appprofesor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.AlumnoApercibimiento;
import com.example.android.appprofesor.models.TutorAsignatura;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.ViewHolder> {
    private List<TutorAsignatura> asignaturas;
    private int layout;
    private Context context;

    public WarningAdapter(List<TutorAsignatura> asignaturas, int layout, Context context) {
        this.asignaturas = asignaturas;
        this.layout = layout;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TutorAsignatura asignatura = asignaturas.get(position);

        holder.asignatura.setText(asignatura.getNombre());
        holder.apercibimientos.setText(asignatura.getMeses().size() + "");
        holder.meses.setText(asignatura.getMesesString());
    }

    @Override
    public int getItemCount() {
        return asignaturas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView asignatura;
        TextView apercibimientos;
        TextView meses;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            asignatura = itemView.findViewById(R.id.textViewSubjectTutorName);
            apercibimientos = itemView.findViewById(R.id.textViewWarningTutorCount);
            meses = itemView.findViewById(R.id.textViewWarningTutorMonth);
        }
    }

}
