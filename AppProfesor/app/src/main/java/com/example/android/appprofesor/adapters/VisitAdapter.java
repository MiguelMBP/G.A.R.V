package com.example.android.appprofesor.adapters;

import android.content.Context;
import android.service.autofill.FieldClassification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.Alumno;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder> {
    private List<Alumno> alumnos;
    private int layout;
    private Context context;
    private OnItemClickListener listener;

    public VisitAdapter(List<Alumno> alumnos, int layout, Context context, OnItemClickListener listener) {
        this.alumnos = alumnos;
        this.layout = layout;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Alumno alumno = alumnos.get(position);

        holder.student.setText(alumno.getNombre() + " " + alumno.getApellidos());
        holder.company.setText(alumno.getEmpresa().getNombre());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(alumno, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView student;
        TextView company;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            student = itemView.findViewById(R.id.textViewStudentCompanyName);
            company = itemView.findViewById(R.id.textViewCompanyListName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Alumno alumno, int position);
    }
}
