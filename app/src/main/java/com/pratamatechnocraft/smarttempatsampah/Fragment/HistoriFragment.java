package com.pratamatechnocraft.smarttempatsampah.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ViewUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.pratamatechnocraft.smarttempatsampah.Adapter.AdapterRecycleViewDataHistori;
import com.pratamatechnocraft.smarttempatsampah.Database.DBDataSource;
import com.pratamatechnocraft.smarttempatsampah.Model.Histori;
import com.pratamatechnocraft.smarttempatsampah.R;
import com.pratamatechnocraft.smarttempatsampah.Service.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoriFragment extends Fragment {
    View view;
    NavigationView navigationView;
    private RecyclerView recyclerViewDataHistori;
    private AdapterRecycleViewDataHistori adapterDataHistori;
    LinearLayout noDataHistori;
    SwipeRefreshLayout refreshDataHistori;
    ProgressBar progressBarDataHistori;
    SessionManager sessionManager;
    private Boolean statusFragment = false;
    private List<Histori> listItemHistoris;
    private DBDataSource dbDataSource;
    private HashMap<String, String> user;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_histori, container, false);
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_history);
        noDataHistori = view.findViewById( R.id.noDataHistori );
        refreshDataHistori = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataHistori);
        progressBarDataHistori = view.findViewById( R.id.progressBarDataHistori );
        recyclerViewDataHistori = (RecyclerView) view.findViewById(R.id.recycleViewDataHistori);

        sessionManager = new SessionManager( getContext() );
        user = sessionManager.getDataLogin();

        refreshDataHistori.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapterDataHistori = new AdapterRecycleViewDataHistori( listItemHistoris, getContext(), noDataHistori);
                recyclerViewDataHistori.setAdapter( adapterDataHistori );
                listItemHistoris.clear();
                adapterDataHistori.notifyDataSetChanged();
                loadHistori();
            }
        } );


        dbDataSource = new DBDataSource(getContext());
        dbDataSource.open();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Histori");
        setHasOptionsMenu( true );
        loadHistori();
        statusFragment=false;
    }

    @Override
    public void onPause() {
        super.onPause();
        statusFragment = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (statusFragment==true) {
            adapterDataHistori = new AdapterRecycleViewDataHistori( listItemHistoris, getContext(), noDataHistori);
            recyclerViewDataHistori.setAdapter( adapterDataHistori );
            listItemHistoris.clear();
            adapterDataHistori.notifyDataSetChanged();
            loadHistori();

        }
    }


    private void loadHistori(){
        progressBarDataHistori.setVisibility(View.VISIBLE);
        listItemHistoris = new ArrayList<>();
        listItemHistoris = dbDataSource.getSemuaHistori();
        setUpRecycleView();
        if (adapterDataHistori.getItemCount()==0){
            noDataHistori.setVisibility( View.VISIBLE );
        }else{
            noDataHistori.setVisibility( View.GONE );
        }
        progressBarDataHistori.setVisibility(View.GONE);
        refreshDataHistori.setRefreshing(false);
    }

    private void setUpRecycleView() {
        recyclerViewDataHistori.setHasFixedSize(true);
        recyclerViewDataHistori.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDataHistori = new AdapterRecycleViewDataHistori( listItemHistoris, getContext(), noDataHistori);
        recyclerViewDataHistori.setAdapter( adapterDataHistori );
        adapterDataHistori.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.ic_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterDataHistori.getFilter().filter(s);
                return false;
            }
        } );
        searchView.setQueryHint("Cari: Histori");
        menu.findItem(R.id.ic_hapus_semua).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Yakin Ingin Menghapus Semua Histori ??");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                dbDataSource = new DBDataSource(getContext());
                                dbDataSource.open();
                                dbDataSource.hapusSemua();
                                loadHistori();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }
}
