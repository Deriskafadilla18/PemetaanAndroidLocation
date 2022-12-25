package com.deriska.apkmagang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Data> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Data> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nik.setText(arrayList.get(position).getNik());
        holder.nama.setText(arrayList.get(position).getNama());
    }

    @Override
    public int getItemCount() {
        return  arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nik, nama;

        public MyViewHolder(View itemView) {
            super(itemView);
            nik = (TextView) itemView.findViewById(R.id.txt_id);
            nama = (TextView) itemView.findViewById(R.id.txt_nama);
        }
    }
}
