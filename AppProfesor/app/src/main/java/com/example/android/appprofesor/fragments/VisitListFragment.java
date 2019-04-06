package com.example.android.appprofesor.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.adapters.VisitAdapter;
import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.utils.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VisitListFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    VisitAdapter adapter;
    List<Alumno> alumnos;

    OnStudentSelected callback;

    public VisitListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_list, container, false);

        recyclerView = view.findViewById(R.id.visit_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        alumnos = Utils.getDummyVisit();

        adapter = new VisitAdapter(alumnos, R.layout.visit_row, getContext(), new VisitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Alumno alumno, int position) {
                callback.onChange(alumno);
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            callback = (OnStudentSelected) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public interface OnStudentSelected{
        public void onChange(Alumno alumno);
    }
}
