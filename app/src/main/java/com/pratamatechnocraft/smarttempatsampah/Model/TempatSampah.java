package com.pratamatechnocraft.smarttempatsampah.Model;

public class TempatSampah {
    Double latitude, longtitude;
    String nama, key;
    Integer status_baterai, status_terisi;

    public TempatSampah(String key, Double latitude, Double longtitude, String nama, Integer status_baterai, Integer status_terisi) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.nama = nama;
        this.status_baterai = status_baterai;
        this.status_terisi = status_terisi;
        this.key=key;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public String getNama() {
        return nama;
    }

    public Integer getStatus_baterai() {
        return status_baterai;
    }

    public Integer getStatus_terisi() {
        return status_terisi;
    }

    public String getKey() {
        return key;
    }
}
