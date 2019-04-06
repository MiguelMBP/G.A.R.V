package com.example.android.appprofesor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.ClaseApercibimiento;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private List<ClaseApercibimiento> clases;
    private int layout;
    private Context context;
    private OnItemClickListener listener;

    public ClassAdapter(List<ClaseApercibimiento> clases, int layout, Context context, OnItemClickListener listener) {
        this.clases = clases;
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
        final ClaseApercibimiento clase = clases.get(position);

        holder.classView.setText(clase.getCurso());
        holder.subject.setText(clase.getAsignatura());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(clase, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clases.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView classView;
        TextView subject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classView = itemView.findViewById(R.id.textViewClassName);
            subject = itemView.findViewById(R.id.textViewSubjectName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ClaseApercibimiento clase, int position);
    }
}
