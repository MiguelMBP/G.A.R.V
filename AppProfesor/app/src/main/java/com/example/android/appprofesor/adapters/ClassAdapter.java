package com.example.android.appprofesor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.models.ClaseApercibimiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> implements Filterable {
    private List<ClaseApercibimiento> clases;
    private List<ClaseApercibimiento> todasClases;
    private int layout;
    private Context context;
    private OnItemClickListener listener;

    public ClassAdapter(List<ClaseApercibimiento> clases, int layout, Context context, OnItemClickListener listener) {
        this.clases = clases;
        this.todasClases = new ArrayList<>(clases);
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

        holder.classView.setText(clase.getUnidad());
        holder.subject.setText(clase.getMateria());

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

    public void addClases(List<ClaseApercibimiento> clases) {
        this.clases = clases;
        this.todasClases = new ArrayList<>(clases);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<ClaseApercibimiento> filteredClases = new ArrayList<>();

            if (constraint == null || constraint.length() < 2) {
                filteredClases.addAll(todasClases);
            } else {
                String filter = constraint.toString().toLowerCase().trim();
                for (int i = 0; i < todasClases.size(); i++) {
                    if (todasClases.get(i).getUnidad().toLowerCase().contains(filter) || todasClases.get(i).getMateria().toLowerCase().contains(filter)) {
                        filteredClases.add(todasClases.get(i));
                    }
                }
            }

            FilterResults r = new FilterResults();
            r.values = filteredClases;
            return r;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clases.clear();
            clases.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {
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

