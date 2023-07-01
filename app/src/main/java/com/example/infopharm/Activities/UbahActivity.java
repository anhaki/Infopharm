package com.example.infopharm.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.infopharm.API.APIRequestData;
import com.example.infopharm.API.RetroServer;
import com.example.infopharm.Model.ModelResponse;
import com.example.infopharm.R;
import com.example.infopharm.databinding.ActivityUbahBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {
    private String id_obat, yNama, yGolongan, yDeskripsi, yBentuk, yEfek, yFoto, tmp1, tmp2;
    private int idx;
    private EditText etNama, etDeskripsi, etBentuk, etEfek;
    private Button btnUbah;
    private String nama, golongan, deskripsi, bentuk, efek_samping, foto;
    private CardView cvFoto;
    private ImageView ivFoto;
    private ActivityUbahBinding binding;
    private ActivityResultLauncher<String> cropGambar;
    private Uri uriFoto;
    private File fileFoto;
    private MultipartBody.Part fotoReal;
    private FloatingActionButton fabEdit;
    Spinner spGolongan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        getSupportActionBar().hide();

        binding = ActivityUbahBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spGolongan = (Spinner) findViewById(R.id.sp_Golongan);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.golongan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGolongan.setAdapter(adapter);

        Intent ambil = getIntent();
        id_obat = ambil.getStringExtra("xId");
        yNama = ambil.getStringExtra("xNama");
        yGolongan = ambil.getStringExtra("xGolongan");
        yDeskripsi = ambil.getStringExtra("xDeskripsi");
        yBentuk = ambil.getStringExtra("xBentuk");
        yEfek = ambil.getStringExtra("xEfek");
        yFoto = ambil.getStringExtra("xFoto");
        etNama = findViewById(R.id.et_name);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etBentuk = findViewById(R.id.et_bentuk);
        etEfek = findViewById(R.id.et_efek);
        ivFoto = findViewById(R.id.iv_foto);
        fabEdit = findViewById(R.id.fab_edit);

        btnUbah = findViewById(R.id.btn_ubah);

        cropGambar = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(UbahActivity.this.getApplicationContext(), CropActivity.class);
            intent.putExtra("DataGambar", result.toString());
            startActivityForResult(intent, 100);
        });

        if(!yFoto.equals("")){
            fabEdit.setVisibility(View.VISIBLE);
            binding.fabEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePermission();
                }
            });
        }
        else{
            fabEdit.setVisibility(View.GONE);
            binding.ivFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePermission();
                }
            });

        }

        if(yGolongan.equalsIgnoreCase("Obat bebas")){
            idx = 0;
        }
        else if(yGolongan.equalsIgnoreCase("Obat bebas terbatas")){
            idx = 1;
        }
        else if(yGolongan.equalsIgnoreCase("Obat keras")){
            idx = 2;
        }
        else if(yGolongan.equalsIgnoreCase("Narkotika")){
            idx = 3;
        }


        etNama.setText(yNama);
        spGolongan.setSelection(idx);
        etDeskripsi.setText(yDeskripsi);
        etBentuk.setText(yBentuk);
        etEfek.setText(yEfek);

        if(!yFoto.equals("")){
            ivFoto.setPadding(0,0,0,0);
            Glide.with(UbahActivity.this)
                    .load(yFoto)
                    .into(ivFoto);
        }

        tmp1 = String.valueOf(ivFoto.getTag());
        tmp2 = String.valueOf(ivFoto.getTag());

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nama =  etNama.getText().toString();
                golongan = spGolongan.getSelectedItem().toString();
                deskripsi = etDeskripsi.getText().toString();
                bentuk = etBentuk.getText().toString();
                efek_samping = etEfek.getText().toString();

                    if(tmp1.equals(tmp2)){
                        foto = yFoto;
                    }
                    else{
                        fileFoto = new File(uriFoto.getPath());

                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileFoto);
                        fotoReal = MultipartBody.Part.createFormData("foto", fileFoto.getName(), requestBody);

                        foto = "https://diseaseinformationhq.000webhostapp.com/img/" + fileFoto.getName();
                    }


                if(nama.trim().isEmpty()||deskripsi.trim().isEmpty()||bentuk.trim().isEmpty()||efek_samping.trim().isEmpty()){
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
                }
                else{
                    UbahObat();
                }
            }
        });
    }

    private void ImagePermission() {
        Dexter.withContext(UbahActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        cropGambar.launch("image/*");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(UbahActivity.this, "Akses ditolak", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == 101){
            String result = data.getStringExtra("CROP");
            uriFoto = data.getData();
            if(result!=null){
                uriFoto = Uri.parse(result);
            }
            ivFoto.setPadding(0, 0, 0, 0);
            ivFoto.setImageURI(uriFoto);

            tmp1 = "";
            fabEdit.setVisibility(View.VISIBLE);
        }
    }

    private void UbahObat(){
        APIRequestData ARD = RetroServer.koneksiRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ARD.ardUpdate(id_obat, nama, deskripsi, efek_samping, golongan, bentuk, foto);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });

        if(!foto.equals(yFoto)){
            Call<ResponseBody> fileNya = ARD.ardUpload(fotoReal);
            fileNya.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(UbahActivity.this, "Foto berhasil diunggah", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(UbahActivity.this, "Foto gagal diunggah", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}