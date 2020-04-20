package com.pratamatechnocraft.smarttempatsampah.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.pratamatechnocraft.smarttempatsampah.Database.DBDataSource;
import com.pratamatechnocraft.smarttempatsampah.Model.TimeLine;
import com.pratamatechnocraft.smarttempatsampah.R;
import java.util.ArrayList;

public class AdapterRecycleViewTimeLine extends RecyclerView.Adapter<AdapterRecycleViewTimeLine.ViewHolder>  {

    private ArrayList<TimeLine> timeLines;
    private Context context;
    private DBDataSource dbDataSource;

    public AdapterRecycleViewTimeLine(ArrayList<TimeLine> timeLines, Context context) {
        this.timeLines = timeLines;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_time_line,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TimeLine timeLine= timeLines.get(position);

        holder.namaTempatSampah.setText(timeLine.getNamaTempatSampah());
        if (position==0){
            holder.noUrutTempatSampah.setText("Lokasi Awal");
        }else if(position==timeLines.size()-1){
            holder.noUrutTempatSampah.setText("Lokasi Tujuan");
        }else {
            holder.noUrutTempatSampah.setText("Pemberhentian "+position);
        }
        Log.d("TAG", "onBindViewHolder: "+position);
    }

    @Override
    public int getItemCount() {
        return timeLines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView namaTempatSampah, noUrutTempatSampah;

        public ViewHolder(View itemView) {
            super(itemView);
            namaTempatSampah = (TextView) itemView.findViewById(R.id.namaTempatSampah);
            noUrutTempatSampah = (TextView) itemView.findViewById(R.id.noUrutTempatSampah);
        }
    }
}
