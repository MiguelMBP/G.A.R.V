package com.example.android.appprofesor.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.adapters.ClassAdapter;
import com.example.android.appprofesor.models.ClaseApercibimiento;
import com.example.android.appprofesor.viewmodels.ClaseViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ClassListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ClassAdapter adapter;
    private List<ClaseApercibimiento> clases = new ArrayList<>();
    private OnClassSelected callback;
    private ClaseViewModel model;

    public ClassListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_list, container, false);

        recyclerView = view.findViewById(R.id.class_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //clases = Utils.getDummyClass();
        //clases = WarningConnector.getMaterias();

        adapter = new ClassAdapter(clases, R.layout.class_row, getContext(), new ClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ClaseApercibimiento clase, int position) {
                callback.onChange(clase);
            }
        });
        recyclerView.setAdapter(adapter);

        model = ViewModelProviders.of(this).get(ClaseViewModel.class);
        model.getClases().observe(this, new Observer<List<ClaseApercibimiento>>() {
            @Override
            public void onChanged(List<ClaseApercibimiento> clases) {
                adapter.addClases(clases);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            callback = (OnClassSelected) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public interface OnClassSelected{
        public void onChange(ClaseApercibimiento clase);
    }

}
