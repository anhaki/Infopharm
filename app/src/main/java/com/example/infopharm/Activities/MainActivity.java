package com.example.infopharm.Activities;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
    private FloatingActionButton fabTambah, fabBebas, fabBebasTerbatas, fabKeras, fabNarkotika, fabSemua, fabMenu;
    private LottieAnimationView pbObat;
    private RecyclerView.Adapter adObat;
    private RecyclerView.LayoutManager lmObat;
    private List<ModelObat> listObat;
    private Boolean buka = false;
    private Animation hadeuh, hadeuh2, tomAnim, tomAnim2;
    private LinearLayout llTombols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Info Pharm - All Drugs");
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.custom_bar));
        getSupportActionBar().setElevation(0);

        rvObat = findViewById(R.id.rv_obat);
        fabTambah = findViewById(R.id.fab_tambah);
        fabBebas = findViewById(R.id.fab_bebas);
        fabBebasTerbatas = findViewById(R.id.fab_bebasTerbatas);
        fabKeras = findViewById(R.id.fab_keras);
        fabNarkotika = findViewById(R.id.fab_narkotika);
        fabSemua = findViewById(R.id.fab_semua);
        fabMenu = findViewById(R.id.fab_menu);

        llTombols = findViewById(R.id.ll_tombols);

        hadeuh = AnimationUtils.loadAnimation(this, R.anim.rotate_open);
        hadeuh2 = AnimationUtils.loadAnimation(this, R.anim.rotate_close);

        pbObat = findViewById(R.id.pb_obat);

        lmObat = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvObat.setLayoutManager(lmObat);

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buka == false){
                    fabMenu.setRotation(180);
                    llTombols.setTranslationX(-740);
                    buka = true;
                }
                else if(buka == true){
                    fabMenu.setRotation(0);
                    llTombols.setTranslationX(0);

                    buka = false;
                }

            }
        });

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TambahActivity.class));
            }
        });

        fabSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { retrieveObat();
                getSupportActionBar().setTitle("Info Pharm - Semua Obat");
            }

        });

        fabBebas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveGolongan("bebas");
                getSupportActionBar().setTitle("Info Pharm - Obat Bebas");
            }
        });

        fabBebasTerbatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveGolongan("bebasTerbatas");
                getSupportActionBar().setTitle("Info Pharm - Obat Bebas Terbatas");

            }
        });

        fabKeras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveGolongan("keras");
                getSupportActionBar().setTitle("Info Pharm - Obat Keras");
            }
        });
        fabNarkotika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveGolongan("narkotika");
                getSupportActionBar().setTitle("Info Pharm - Narkotika");
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        retrieveObat();
    }

    public void retrieveObat(){
        rvObat.setVisibility(View.INVISIBLE);
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
                rvObat.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbObat.setVisibility(View.GONE);
            }
        });
    }

    private void retrieveGolongan(String golongan){
        rvObat.setVisibility(View.INVISIBLE);
        pbObat.setVisibility(View.VISIBLE);

        APIRequestData ardData = RetroServer.koneksiRetrofit().create(APIRequestData.class);
        Call<ModelResponse> prosess = ardData.getGolongan(golongan);

        prosess.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listObat = response.body().getData();

                adObat = new AdapterObat(MainActivity.this, listObat);
                rvObat.setAdapter(adObat);
                adObat.notifyDataSetChanged();

                pbObat.setVisibility(View.GONE);
                rvObat.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbObat.setVisibility(View.GONE);
            }
        });
    }

}