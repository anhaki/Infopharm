package com.example.infopharm.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.infopharm.API.APIRequestData;
import com.example.infopharm.API.RetroServer;
import com.example.infopharm.Model.ModelResponse;
import com.example.infopharm.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {
    private EditText etNama, etGolongan, etDeskripsi, etBentuk, etEfek, etFoto;
    private Button btnSimpan;
    private String nama, golongan, deskripsi, bentuk, efek_samping, foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNama = findViewById(R.id.et_name);
        etGolongan = findViewById(R.id.et_golongan);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etBentuk = findViewById(R.id.et_bentuk);
        etEfek = findViewById(R.id.et_efek);
        etFoto = findViewById(R.id.et_foto);

        btnSimpan = findViewById(R.id.btn_tambah);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nama =  etNama.getText().toString();
                golongan = etGolongan.getText().toString();
                deskripsi = etDeskripsi.getText().toString();
                bentuk = etBentuk.getText().toString();
                efek_samping = etEfek.getText().toString();
                foto = etFoto.getText().toString();

                if(nama.trim().isEmpty()||golongan.trim().isEmpty()||deskripsi.trim().isEmpty()||bentuk.trim().isEmpty()||efek_samping.trim().isEmpty()||foto.trim().isEmpty()){
                    if(nama.trim().isEmpty()){
                        etNama.setError("Nama Tidak Boleh Kosong");
                    }
                    if(deskripsi.trim().isEmpty()){
                        etDeskripsi.setError("Deskripsi Tidak Boleh Kosong");
                    }
                    if(bentuk.trim().isEmpty()){
                        etBentuk.setError("Bentuk Tidak Boleh Kosong");
                    }
                    if(efek_samping.trim().isEmpty()){
                        etEfek.setError("Efek Samping Tidak Boleh Kosong");
                    }
                    if(foto.trim().isEmpty()){
                        etFoto.setError("Foto Tidak Boleh Kosong");
                    }
                    if(golongan.trim().isEmpty()){
                        etGolongan.setError("Golongan Tidak Boleh Kosong");
                    }
                }
                else{
                    tambahObat();
                }
            }
        });
    }

    private void tambahObat(){
        APIRequestData ARD = RetroServer.koneksiRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ARD.ardCreate(nama, deskripsi, efek_samping, golongan, bentuk, foto);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode : " + kode + "Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });

    }
}