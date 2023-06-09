package com.example.infopharm.API;

import com.example.infopharm.Model.ModelResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIRequestData {
    @GET("retrieve.php")
    Call<ModelResponse> ardRetrieve();

    @FormUrlEncoded
    @POST("create.php")
    Call<ModelResponse> ardCreate(
            @Field("nama") String nama,
            @Field("deskripsi") String deskripsi,
            @Field("efek_samping") String efek_samping,
            @Field("golongan") String golongan,
            @Field("bentuk") String bentuk,
            @Field("foto") String foto
    );

    @Multipart
    @POST("upload.php")
    Call<ResponseBody> ardUpload(
            @Part MultipartBody.Part foto
    );


    @FormUrlEncoded
    @POST("update.php")
    Call<ModelResponse> ardUpdate(
            @Field("id_obat") String id_obat,
            @Field("nama") String nama,
            @Field("deskripsi") String deskripsi,
            @Field("efek_samping") String efek_samping,
            @Field("golongan") String golongan,
            @Field("bentuk") String bentuk,
            @Field("foto") String foto
    );

    @FormUrlEncoded
    @GET("update.php?id_obat={id_obat}")
    Call<ModelResponse> getDetail(@Path("id_obat") String id_obat);


    @FormUrlEncoded
    @POST("delete.php")
    Call<ModelResponse> ardDelete(
            @Field("id_obat") String id_obat
    );

    @GET("golongan/{golongan}.php")
    Call<ModelResponse> getGolongan(@Path("golongan") String golongan);

}
