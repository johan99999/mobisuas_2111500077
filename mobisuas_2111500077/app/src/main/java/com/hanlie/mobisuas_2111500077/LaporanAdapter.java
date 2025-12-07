package com.hanlie.mobisuas_2111500077;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.ViewHolder> {

    Context ctx;
    ArrayList<Laporan> data;


    public LaporanAdapter(Context ctx, ArrayList<Laporan> data) {
        this.ctx = ctx;
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_laporan, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Laporan x = data.get(pos);

        h.txtJudul.setText(x.judul);
        h.txtStatus.setText(x.status);
        h.txtKategori.setText(x.kategori);
        h.txtTglLapor.setText("Waktu Terlapor:\t\t"+x.terlapor);
        h.txtTglTanggap.setText("Waktu Ditanggapi:\t\t"+x.ditanggapi);
        h.txtTanggapan.setText(x.tanggapan);


        switch (x.kategori) {
            case "Tambang Ilegal":
                h.icon.setImageResource(R.drawable.tambang_ilegal);
                break;
            case "Kebakaran":
                h.icon.setImageResource(R.drawable.kebakaran);
                break;
            case "Bencana":
                h.icon.setImageResource(R.drawable.bencana);
                break;
            case "Gangguan PDAM":
                h.icon.setImageResource(R.drawable.masalah_pdam);
                break;
            case "Penumpukan Sampah":
                h.icon.setImageResource(R.drawable.penumpukan_sampah);
                break;
            default:
                h.icon.setImageResource(R.drawable.ic_default);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView txtJudul, txtStatus, txtKategori, txtTglLapor, txtTglTanggap, txtTanggapan;

        public ViewHolder(@NonNull View v) {
            super(v);
            icon = v.findViewById(R.id.iconKategori);
            txtJudul = v.findViewById(R.id.txtJudul);
            txtStatus = v.findViewById(R.id.txtStatus);
            txtKategori = v.findViewById(R.id.txtKategori);
            txtTglLapor = v.findViewById(R.id.txtDilapor);
            txtTglTanggap = v.findViewById(R.id.txtDitanggapi);
            txtTanggapan = v.findViewById(R.id.txtTanggapanAdmin);
        }
    }
}
