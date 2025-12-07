package com.hanlie.mobisuas_2111500077;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RiwayatActivity extends AppCompatActivity {
    RecyclerView rv;
    LaporanAdapter adapter;
    ArrayList<Laporan> list = new ArrayList<>();
    String URL;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_riwayat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Riwayat Laporan");

        rv = findViewById(R.id.recyclerRiwayat);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LaporanAdapter(this, list);
        rv.setAdapter(adapter);

        loadData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadData() {
        ClassGlobal global = (ClassGlobal) getApplicationContext();

        String idUser = global.getIdUser();


        URL = global.getUrl() + "riwayat_laporan.php?id_user=" + idUser;

        Log.d("CEK_URL", "URL: " + URL); // Debug

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                res -> {
                    try {
                        JSONArray arr = new JSONArray(res);
                        list.clear();

                        for (int i=0; i<arr.length(); i++){
                            JSONObject o = arr.getJSONObject(i);
                            Laporan x = new Laporan();
                            x.id = o.getString("C_ID_LAPORAN");
                            x.judul = o.getString("C_JUDUL");
                            x.kategori = o.getString("C_KATEGORI");
                            x.status = o.getString("C_STATUS");
                            x.terlapor = o.getString("C_TERLAPOR");
                            x.ditanggapi = o.getString("C_DITANGGAPI");
                            x.tanggapan = o.getString("C_TANGGAPAN");

                            list.add(x);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> {
                    err.printStackTrace();
                }
        );

        Volley.newRequestQueue(this).add(req);
    }


}
