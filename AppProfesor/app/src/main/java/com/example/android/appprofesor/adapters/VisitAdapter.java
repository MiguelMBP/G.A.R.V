package com.example.android.appprofesor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.Alumno;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        holder.alumno.setText(alumno.getNombre() + " " + alumno.getApellidos());
        holder.empresa.setText(alumno.getEmpresa().getNombre());
        holder.fecha.setText(sdf.format(alumno.getFecha()));

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

    public void addAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView alumno;
        TextView empresa;
        TextView fecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alumno = itemView.findViewById(R.id.textViewStudentCompanyName);
            empresa = itemView.findViewById(R.id.textViewCompanyListName);
            fecha = itemView.findViewById(R.id.textViewCompanyDate);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Alumno alumno, int position);
    }
}
