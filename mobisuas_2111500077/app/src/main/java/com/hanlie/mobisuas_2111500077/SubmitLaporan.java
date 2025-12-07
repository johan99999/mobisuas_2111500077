package com.hanlie.mobisuas_2111500077;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitLaporan extends AppCompatActivity {

    Spinner spinnerKategori;
    EditText etJudul, etLokasi, etKronologi;
    ImageView btnUploadFoto, btnBack;
    Button btnLaporkan;

    Uri fotoUri;
    String encodedImage = "";

    private static final int PICK_IMAGE = 100;

    String URL_SUBMIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_laporan);

        ClassGlobal global = (ClassGlobal) getApplicationContext();
        URL_SUBMIT = global.getUrl() + "submit_laporan.php";

        spinnerKategori = findViewById(R.id.spinnerKategori);
        etJudul = findViewById(R.id.etJudul);
        etLokasi = findViewById(R.id.etLokasi);
        etKronologi = findViewById(R.id.etKronologi);
        btnUploadFoto = findViewById(R.id.btnUploadFoto);
        btnBack = findViewById(R.id.btnBack);
        btnLaporkan = findViewById(R.id.btnLaporkan);

        setupSpinnerKategori();

        btnUploadFoto.setOnClickListener(v -> {
            Intent galeri = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galeri, PICK_IMAGE);
        });

        btnBack.setOnClickListener(v -> finish());

        btnLaporkan.setOnClickListener(v -> submitForm());
    }

    private void setupSpinnerKategori() {

        List<String> kategoriList = new ArrayList<>();
        kategoriList.add("Pilih Kategori");
        kategoriList.add("Tambang Ilegal");
        kategoriList.add("Kebakaran");
        kategoriList.add("Bencana");
        kategoriList.add("Gangguan PDAM");
        kategoriList.add("Penumpukan Sampah");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.custom_spinner_item,
                kategoriList
        ) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {

                if (position == 0) {
                    return getLayoutInflater().inflate(R.layout.custom_spinner_hint, parent, false);
                }
                return super.getView(position, convertView, parent);
            }

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };

        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        spinnerKategori.setAdapter(adapter);
        spinnerKategori.setSelection(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {

            fotoUri = data.getData();
            btnUploadFoto.setImageURI(fotoUri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(fotoUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                byte[] imageBytes = baos.toByteArray();

                encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void submitForm() {

        String kategori = spinnerKategori.getSelectedItem().toString();
        String judul = etJudul.getText().toString().trim();
        String lokasi = etLokasi.getText().toString().trim();
        String kronologi = etKronologi.getText().toString().trim();

        SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String idUser = prefs.getString("c_id_user", null);

        if (idUser == null) {
            Toast.makeText(this, "User tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            return;
        }


        if (spinnerKategori.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Silakan pilih kategori!", Toast.LENGTH_SHORT).show();
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

        if (encodedImage.isEmpty()) {
            Toast.makeText(this, "Silakan upload foto!", Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_SUBMIT,
                response -> {
                    System.out.println("🔥 RAW RESPONSE FROM SERVER:");
                    System.out.println(response);
                    try {
                        JSONObject json = new JSONObject(response);

                        if (json.getBoolean("success")) {
                            Toast.makeText(SubmitLaporan.this, "Laporan berhasil dikirim!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(SubmitLaporan.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(SubmitLaporan.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()

        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("id_user", idUser);
                params.put("kategori", kategori);
                params.put("judul", judul);
                params.put("lokasi", lokasi);
                params.put("kronologi", kronologi);
                params.put("foto", encodedImage);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(SubmitLaporan.this);
        queue.add(request);

    }
}
