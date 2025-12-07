package com.hanlie.mobisuas_2111500077;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText etPhone, etPassword;
    Button btnLogin;

    String URL_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ClassGlobal global = (ClassGlobal) getApplicationContext();
        URL_LOGIN = global.getUrl() + "login.php";

        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Nomor HP dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            loginProcess(phone, pass);
        });
    }

    private void loginProcess(String phone, String password) {

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Sedang memproses...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    progressDialog.dismiss();

                    try {
                        JSONObject json = new JSONObject(response);

                        boolean success = json.getBoolean("success");
                        String message = json.getString("message");

                        if (success) {
                            String idUser = json.getString("c_id_user");
                            SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("c_id_user", idUser);
                            editor.putString("phone", phone);
                            editor.apply();
                            ClassGlobal global = (ClassGlobal) getApplicationContext();
                            global.setIdUser(idUser);
                            Toast.makeText(Login.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Login.this, "Error parsing data!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Gagal terhubung ke server!", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("aksi", "login");
                params.put("phone", phone);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(request);
    }
}
