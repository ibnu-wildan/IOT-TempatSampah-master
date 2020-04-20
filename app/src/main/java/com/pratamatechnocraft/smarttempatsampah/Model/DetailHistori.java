package com.pratamatechnocraft.smarttempatsampah.Model;

public class DetailHistori {
    long idDetail;
    long idHistoriDetail;
    String idTempatSampah;
    String latitude;
    String longtitude;

    public long getIdHistoriDetail() {
        return idHistoriDetail;
    }

    public void setIdHistoriDetail(long idHistoriDetail) {
        this.idHistoriDetail = idHistoriDetail;
    }

    public String getIdTempatSampah() {
        return idTempatSampah;
    }

    public void setIdTempatSampah(String idTempatSampah) {
        this.idTempatSampah = idTempatSampah;
    }

    public long getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(long idDetail) {
        this.idDetail = idDetail;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}
