package com.example.infopharm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.infopharm.API.APIRequestData;
import com.example.infopharm.API.RetroServer;
import com.example.infopharm.Adapter.AdapterObat;
import com.example.infopharm.Model.ModelObat;
import com.example.infopharm.Model.ModelResponse;
import com.example.infopharm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvObat;
    private FloatingActionButton fabTambah;
    private ProgressBar pbObat;
    private RecyclerView.Adapter adObat;
    private RecyclerView.LayoutManager lmObat;
    private List<ModelObat> listObat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvObat = findViewById(R.id.rv_obat);
//        fabTambah = findViewById(R.id.fab_tambah);
        pbObat = findViewById(R.id.pb_obat);

        lmObat = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvObat.setLayoutManager(lmObat);

//        fabTambah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, TambahActivity.class));
//            }
//        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        retrieveObat();
    }

    public void retrieveObat(){
        pbObat.setVisibility(View.VISIBLE);

        APIRequestData ARD = RetroServer.koneksiRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ARD.ardRetrieve();

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listObat = response.body().getData();

                adObat = new AdapterObat(MainActivity.this, listObat);
                rvObat.setAdapter(adObat);
                adObat.notifyDataSetChanged();

                pbObat.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbObat.setVisibility(View.GONE);
            }
        });
    }}