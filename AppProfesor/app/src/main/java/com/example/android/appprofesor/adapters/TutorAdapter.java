package com.example.android.appprofesor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.TutorAlumno;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.ViewHolder> {
    private List<TutorAlumno> alumnos;
    private int layout;
    private Context context;
    private OnItemClickListener listener;

    public TutorAdapter(List<TutorAlumno> alumnos, int layout, Context context, OnItemClickListener listener) {
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
        final TutorAlumno alumno = alumnos.get(position);

        holder.alumno.setText(alumno.getNombre());

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
        private TextView alumno;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alumno = itemView.findViewById(R.id.textViewStudentTutorListName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TutorAlumno alumno, int position);
    }
}
