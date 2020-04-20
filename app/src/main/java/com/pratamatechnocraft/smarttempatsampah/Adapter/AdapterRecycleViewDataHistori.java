package com.pratamatechnocraft.smarttempatsampah.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.pratamatechnocraft.smarttempatsampah.Database.DBDataSource;
import com.pratamatechnocraft.smarttempatsampah.Fragment.HistoriFragment;
import com.pratamatechnocraft.smarttempatsampah.Fragment.HomeFragment;
import com.pratamatechnocraft.smarttempatsampah.HasilCariActivity;
import com.pratamatechnocraft.smarttempatsampah.Model.Histori;
import com.pratamatechnocraft.smarttempatsampah.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterRecycleViewDataHistori extends RecyclerView.Adapter<AdapterRecycleViewDataHistori.ViewHolder> implements Filterable {

    private List<Histori> listItemHistoris;
    private List<Histori> listItemHistoriFull;
    private Context context;
    Locale locale = new Locale("id", "ID");
    private DBDataSource dbDataSource;
    private AlertDialog alertDialog;
    private LinearLayout noDataHistori;




    public AdapterRecycleViewDataHistori(List<Histori> listItemHistoris, Context context, LinearLayout noDataHistori) {
        this.listItemHistoris = listItemHistoris;
        listItemHistoriFull = new ArrayList<>( listItemHistoris );
        this.context = context;
        this.noDataHistori = noDataHistori;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.list_item_histori,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Histori listItemHistori = listItemHistoris.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd-MM-yyyy HH:mm:ss", locale);
        Date date = null;
        try {
            date = simpleDateFormat.parse(listItemHistori.getTanggal());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtHariHistori.setText(new SimpleDateFormat("EEEE", locale).format(date));
        holder.txtTanggalHistori.setText("("+new SimpleDateFormat("dd-MM-yyyy", locale).format(date)+")");
        holder.txtJamHistori.setText(new SimpleDateFormat("HH:mm:ss", locale).format(date));

        holder.cardViewDataHistori.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HasilCariActivity.class);
                intent.putExtra("tanggal", listItemHistori.getTanggal());
                intent.putExtra("idHistori",  listItemHistori.getIdHistori());
                context.startActivity(intent);
            }
        });

        holder.imageButtonHapusHistori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Yakin Ingin Menghapus Histori Ini ??");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                dbDataSource = new DBDataSource(context);
                                dbDataSource.open();
                                dbDataSource.hapusHistoriSatu(listItemHistori.getIdHistori());
                                listItemHistoris.remove(position);
                                listItemHistoriFull.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                                if (listItemHistoris.size()==0){
                                    noDataHistori.setVisibility(View.VISIBLE);
                                }else{
                                    noDataHistori.setVisibility(View.GONE);
                                }
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemHistoris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtHariHistori, txtTanggalHistori, txtJamHistori;
        public ImageButton imageButtonHapusHistori;
        public CardView cardViewDataHistori;

        public ViewHolder(View itemView) {
            super(itemView);
            txtHariHistori = (TextView) itemView.findViewById(R.id.txtHariHistori);
            txtTanggalHistori = (TextView) itemView.findViewById(R.id.txtTanggalHistori);
            txtJamHistori = (TextView) itemView.findViewById(R.id.txtJamHistori);
            imageButtonHapusHistori = (ImageButton) itemView.findViewById(R.id.imageButtonHapusHistori);
            cardViewDataHistori = (CardView) itemView.findViewById(R.id.cardViewDataHistori);
        }
    }

    @Override
    public Filter getFilter() {
        return listItemFilter;
    }

    private Filter listItemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Histori> filteredList = new ArrayList<>(  );

            if (charSequence == null || charSequence.length()==0){
                filteredList.addAll( listItemHistoriFull );
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Histori itemHistori : listItemHistoriFull){
                    if (itemHistori.getTanggal().toLowerCase().contains( filterPattern )){
                        filteredList.add( itemHistori );
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values=filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listItemHistoris.clear();
            listItemHistoris.addAll((List) filterResults.values );
            notifyDataSetChanged();
        }
    };

}
