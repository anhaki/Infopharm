package com.example.infopharm.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infopharm.API.APIRequestData;
import com.example.infopharm.API.RetroServer;
import com.example.infopharm.Activities.MainActivity;
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
        ModelObat MK = listObat.get(position);
        holder.tvId.setText(MK.getId_obat());
        holder.tvNama.setText(MK.getNama());
        holder.tvGolongan.setText(MK.getGolongan());
    }

    @Override
    public int getItemCount() {
        return listObat.size();
    }

    public class VHObat extends RecyclerView.ViewHolder{
        TextView tvId, tvNama, tvGolongan;

        public VHObat(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvGolongan = itemView.findViewById(R.id.tv_golongan);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder pesan = new AlertDialog.Builder(ctx);
                    pesan.setTitle("Perhatian");
                    pesan.setMessage("Operaso apa yang akan dilakukan?");
                    pesan.setCancelable(true);

                    pesan.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hapusObat(tvId.getText().toString());
                            dialog.dismiss();
                        }
                    });
//                    pesan.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent pindah = new Intent(ctx, UbahActivity.class);
//                            pindah.putExtra("xId", tvId.getText().toString());
//                            pindah.putExtra("xNama", tvNama.getText().toString());
//                            pindah.putExtra("xAsal", tvAsal.getText().toString());
//                            pindah.putExtra("xDeskripsi", tvDeskripsiSingkat.getText().toString());
//                            ctx.startActivity(pindah);
//                        }
//                    });
                    pesan.show();
                    return false;
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
