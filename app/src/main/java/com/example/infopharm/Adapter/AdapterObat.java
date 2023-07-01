package com.example.infopharm.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.infopharm.API.APIRequestData;
import com.example.infopharm.API.RetroServer;
import com.example.infopharm.Activities.DetailActivity;
import com.example.infopharm.Activities.MainActivity;
import com.example.infopharm.Activities.UbahActivity;
import com.example.infopharm.Model.ModelObat;
import com.example.infopharm.Model.ModelResponse;
import com.example.infopharm.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterObat extends RecyclerView.Adapter<AdapterObat.VHObat>{
    private Context ctx;
    private List<ModelObat> listObat;

    public AdapterObat(Context ctx, List<ModelObat> listObat) {
        this.ctx = ctx;
        this.listObat = listObat;
    }

    @NonNull
    @Override
    public VHObat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View varView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_drug, parent, false);
        return new VHObat(varView);
    }

    @Override
    public void onBindViewHolder(@NonNull VHObat holder, int position) {
        ModelObat MO = listObat.get(position);
        holder.tvId.setText(MO.getId_obat());
        holder.tvNama.setText(MO.getNama());
        holder.tvGolongan.setText(MO.getGolongan());
//        if(MO.getGolongan().equalsIgnoreCase("Obat bebas")){
//            holder.itemView.setBackground(ContextCompat.getDrawable(ctx, R.drawable.corner_radius_bbs));
//        }
//        else if(MO.getGolongan().equalsIgnoreCase("Obat bebas terbatas")){
//            holder.itemView.setBackground(ContextCompat.getDrawable(ctx, R.drawable.corner_radius_tbt));
//        }
//        else if(MO.getGolongan().equalsIgnoreCase("Obat keras")){
//            holder.itemView.setBackground(ContextCompat.getDrawable(ctx, R.drawable.corner_radius_krs));
//        }
//        else if(MO.getGolongan().equalsIgnoreCase("Narkotika")){
//            holder.itemView.setBackground(ContextCompat.getDrawable(ctx, R.drawable.corner_radius_nkt));
//        }
        holder.tvBentuk.setText(MO.getBentuk());
        holder.tvEfek.setText(MO.getEfek_samping());
        holder.tvDeskripsi.setText(MO.getDeskripsi());
        holder.tvFoto.setText(MO.getFoto());

        if(!MO.getFoto().equals("")){
            Glide.with(holder.itemView.getContext())
                    .load(MO.getFoto())
                    .into(holder.ivFoto);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(ctx, DetailActivity.class);
                pindah.putExtra("xId", MO.getId_obat());
                pindah.putExtra("xNama", MO.getNama());
                pindah.putExtra("xBentuk", MO.getBentuk());
                pindah.putExtra("xEfek", MO.getEfek_samping());
                pindah.putExtra("xDeskripsi", MO.getDeskripsi());
                pindah.putExtra("xGolongan", MO.getGolongan());
                pindah.putExtra("xFoto", MO.getFoto());

                ctx.startActivity(pindah);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listObat.size();
    }

    public class VHObat extends RecyclerView.ViewHolder{
        TextView tvId, tvNama, tvGolongan, tvBentuk, tvEfek, tvDeskripsi, tvFoto;
        ImageView ivFoto, ivHapus, ivUbah;

        public VHObat(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvGolongan = itemView.findViewById(R.id.tv_golongan);
            tvBentuk = itemView.findViewById(R.id.tv_Bentuk);
            tvEfek = itemView.findViewById(R.id.tv_Efek);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi);
            tvFoto = itemView.findViewById(R.id.tv_foto);
            ivFoto = itemView.findViewById(R.id.Iv_foto);
            ivHapus = itemView.findViewById(R.id.bt_hapus);
            ivUbah = itemView.findViewById(R.id.bt_edit);

            ivHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder pesan = new AlertDialog.Builder(ctx);
                    pesan.setTitle("Anda ingin menghapus " + tvNama.getText().toString());
                    pesan.setMessage("Apakah anda yakin?");
                    pesan.setCancelable(true);

                    pesan.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hapusObat(tvId.getText().toString());
                            dialog.dismiss();
                        }
                    });
                    pesan.setPositiveButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = pesan.create();
                    alert.show();
                }
            });
            ivUbah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pindah = new Intent(ctx, UbahActivity.class);
                    pindah.putExtra("xId", tvId.getText().toString());
                    pindah.putExtra("xNama", tvNama.getText().toString());
                    pindah.putExtra("xBentuk", tvBentuk.getText().toString());
                    pindah.putExtra("xEfek", tvEfek.getText().toString());
                    pindah.putExtra("xDeskripsi", tvDeskripsi.getText().toString());
                    pindah.putExtra("xGolongan", tvGolongan.getText().toString());
                    pindah.putExtra("xFoto", tvFoto.getText().toString());

                    ctx.startActivity(pindah);
                }
            });
        }

        private void hapusObat(String idObat){
            APIRequestData ARD = RetroServer.koneksiRetrofit().create(APIRequestData.class);
            Call<ModelResponse> proses = ARD.ardDelete(idObat);

            proses.enqueue(new Callback<ModelResponse>() {
                @Override
                public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                    String kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode : " + kode + ", Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                    ((MainActivity) ctx).retrieveObat();
                }

                @Override
                public void onFailure(Call<ModelResponse> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
