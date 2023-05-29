package com.example.infopharm.Model;

import java.util.List;

public class ModelResponse {
    private String kode, pesan;
    private List<ModelObat> data;

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelObat> getData() {
        return data;
    }

}
