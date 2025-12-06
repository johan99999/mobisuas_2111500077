package com.hanlie.mobisuas_2111500077;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanlie.mobisuas_2111500077.R;

import java.util.ArrayList;
import java.util.List;

public class SubmitLaporan extends AppCompatActivity {

    Spinner spinnerKategori;
    EditText etJudul, etLokasi, etKronologi;
    ImageView btnUploadFoto, btnBack;
    Button btnLaporkan;

    Uri fotoUri;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_laporan);

        // === INISIALISASI UI ===
        spinnerKategori = findViewById(R.id.spinnerKategori);
        etJudul = findViewById(R.id.etJudul);
        etLokasi = findViewById(R.id.etLokasi);
        etKronologi = findViewById(R.id.etKronologi);
        btnUploadFoto = findViewById(R.id.btnUploadFoto);
        btnBack = findViewById(R.id.btnBack);
        btnLaporkan = findViewById(R.id.btnLaporkan);

        // === SETUP SPINNER DENGAN HINT ===
        setupSpinnerKategori();

        // === EVENT UPLOAD FOTO ===
        btnUploadFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeri = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeri, PICK_IMAGE);
            }
        });

        // === BUTTON BACK ===
        btnBack.setOnClickListener(v -> finish());

        // === TOMBOL SUBMIT ===
        btnLaporkan.setOnClickListener(v -> submitForm());
    }

    // ====================================================================
    // CUSTOM SPINNER DENGAN HINT
    // ====================================================================
    private void setupSpinnerKategori() {

        List<String> kategoriList = new ArrayList<>();
        kategoriList.add("Pilih Kategori"); // HINT INTERNAL
        kategoriList.add("Kebakaran");
        kategoriList.add("Banjir");
        kategoriList.add("Kecelakaan");
        kategoriList.add("Pencurian");
        kategoriList.add("Lainnya");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.custom_spinner_item,
                kategoriList
        ) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                if (position == 0) {
                    // tampilan hint custom
                    return getLayoutInflater().inflate(R.layout.custom_spinner_hint, parent, false);
                }
                return super.getView(position, convertView, parent);
            }

            @Override
            public boolean isEnabled(int position) {
                return position != 0; // hint tidak bisa dipilih
            }
        };

        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        spinnerKategori.setAdapter(adapter);
        spinnerKategori.setSelection(0);
    }

    // ====================================================================
    // TERIMA FOTO DARI GALERI
    // ====================================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            fotoUri = data.getData();
            btnUploadFoto.setImageURI(fotoUri);
        }
    }

    // ====================================================================
    // PROSES SUBMIT
    // ====================================================================
    private void submitForm() {
        String kategori = spinnerKategori.getSelectedItem().toString();
        String judul = etJudul.getText().toString();
        String lokasi = etLokasi.getText().toString();
        String kronologi = etKronologi.getText().toString();

        if (spinnerKategori.getSelectedItemPosition() == 0) {
            etJudul.setError("Pilih kategori!");
            return;
        }

        if (judul.isEmpty()) {
            etJudul.setError("Judul wajib diisi");
            return;
        }

        if (lokasi.isEmpty()) {
            etLokasi.setError("Lokasi wajib diisi");
            return;
        }

        if (kronologi.isEmpty()) {
            etKronologi.setError("Kronologi wajib diisi");
            return;
        }

        // → Di sini kamu bisa tambahkan pengiriman ke API server
        // (POST via Volley/Retrofit)

        // Untuk sekarang tampilkan log:
        android.widget.Toast.makeText(this,
                "Laporan berhasil disiapkan!",
                android.widget.Toast.LENGTH_LONG).show();

        finish();
    }
}
