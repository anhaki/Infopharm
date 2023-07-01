package com.example.infopharm.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.infopharm.API.APIRequestData;
import com.example.infopharm.API.RetroServer;
import com.example.infopharm.Adapter.AdapterObat;
import com.example.infopharm.Model.ModelResponse;
import com.example.infopharm.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private TextView nama, golongan, deskripsi, efek, bentuk;
    private String id_obat, yNama, yGolongan, yDeskripsi, yBentuk, yEfek, yFoto;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().hide();

        nama = findViewById(R.id.nama_obat);
        golongan = findViewById(R.id.golongan_obat);
        foto = findViewById(R.id.iv_foto);
        deskripsi = findViewById(R.id.deskripsi);
        efek = findViewById(R.id.efek);
        bentuk = findViewById(R.id.bentuks);

        Intent intent = getIntent();
        String idObat = intent.getStringExtra("varId");

        getSupportActionBar().hide();

        Intent ambil = getIntent();
        id_obat = ambil.getStringExtra("xId");
        yNama = ambil.getStringExtra("xNama");
        yGolongan = ambil.getStringExtra("xGolongan");
        yDeskripsi = ambil.getStringExtra("xDeskripsi");
        yBentuk = ambil.getStringExtra("xBentuk");
        yEfek = ambil.getStringExtra("xEfek");
        yFoto = ambil.getStringExtra("xFoto");

        nama.setText(yNama);
        golongan.setText(yGolongan);
        deskripsi.setText(yDeskripsi);
        efek.setText(yEfek);
        bentuk.setText(yBentuk);

        if(!yFoto.equals("")){
            foto.setPadding(0,0,0,0);
            Glide.with(DetailActivity.this)
                    .load(yFoto)
                    .into(foto);
        }

    }
}

