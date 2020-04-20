package com.pratamatechnocraft.smarttempatsampah.Model;

public class InfoWindowMap {
    private String statusTerisi;
    private String statusBaterai;

    public InfoWindowMap(String statusTerisi, String statusBaterai) {
        this.statusTerisi = statusTerisi;
        this.statusBaterai = statusBaterai;
    }

    public String getStatusTerisi() {
        return statusTerisi;
    }

    public String getStatusBaterai() {
        return statusBaterai;
    }
}
