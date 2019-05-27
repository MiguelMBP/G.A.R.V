package com.example.android.appprofesor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.AlumnoApercibimiento;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Realiza la función de puente entre el RecyclerView y los datos a mostrar
 */
public class ClassStudentAdapter extends RecyclerView.Adapter<ClassStudentAdapter.ViewHolder> {
    private List<AlumnoApercibimiento> alumnos;
    private int layout;
    private Context context;

    public ClassStudentAdapter(List<AlumnoApercibimiento> alumnos, int layout, Context context) {
        this.alumnos = alumnos;
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
        final AlumnoApercibimiento alumno = alumnos.get(position);

        holder.alumno.setText(alumno.getNombre());
        holder.apercibimientos.setText(alumno.getMeses().size() + " Apercibimientos");
        holder.meses.setText(alumno.getMesesString());
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView alumno;
        TextView apercibimientos;
        TextView meses;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alumno = itemView.findViewById(R.id.textViewListStudentName);
            apercibimientos = itemView.findViewById(R.id.textViewWarningCount);
            meses = itemView.findViewById(R.id.textViewMonths);
        }
    }

}
