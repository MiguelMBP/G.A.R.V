package com.example.android.appprofesor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.appprofesor.R;
import com.example.android.appprofesor.adapters.ClassAdapter;
import com.example.android.appprofesor.models.ClaseApercibimiento;
import com.example.android.appprofesor.models.Settings;
import com.example.android.appprofesor.viewmodels.ClaseViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment para mostrar la vista de lista de los objetos ClaseApercibimiento
 */
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
        setHasOptionsMenu(true);

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
        try {
            SharedPreferences prefs =
                    getContext().getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
            String address = prefs.getString("address", null);
            int port = prefs.getInt("port", -1);
            if (address != null && port != -1) {
                Settings settings = new Settings(address, port);
                model.getClases(settings).observe(this, new Observer<List<ClaseApercibimiento>>() {
                    @Override
                    public void onChanged(List<ClaseApercibimiento> clases) {
                        adapter.addClases(clases);
                    }
                });
            } else {
                throw new NullPointerException();
            }

        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Error, ajustes no establecidos", Toast.LENGTH_SHORT)
                    .show();
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (OnClassSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public interface OnClassSelected {
        public void onChange(ClaseApercibimiento clase);
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.buscador, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);

                return false;
            }
        });
    }
}
