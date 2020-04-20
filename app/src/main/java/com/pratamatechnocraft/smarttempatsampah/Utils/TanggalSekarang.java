package com.pratamatechnocraft.smarttempatsampah.Utils;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TanggalSekarang {
    public String getTanggal() {
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getWaktu() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
