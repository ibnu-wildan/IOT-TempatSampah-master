package com.pratamatechnocraft.smarttempatsampah.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperSqlLite extends SQLiteOpenHelper {
    /*Tabel Histori Initialisasi*/
    public static final String TABLE_NAME_HISTORI = "data_histori";
    public static final String ID_HISTORI = "id_histori";
    public static final String TANGGAL = "tanggal";

    /*Tabel Detail Histori Initialisasi*/
    public static final String TABLE_NAME_DETAIL_HISTORI = "data_detail_histori";
    public static final String ID_DETAIL = "id_detail";
    public static final String ID_HISTORI_DETAIL = "id_histori_detail";
    public static final String ID_TEMPAT_SAMPAH = "id_tempat_sampah";
    public static final String LATITUDE = "latitude";
    public static final String LONGTITUDE = "longtitude";
    private static final String db_name ="iot_tempat_sampah.db";
    private static final int db_version=11;

    // Perintah SQL untuk membuat tabel database baru
    private static final String db_create_histori = "create table "
            + TABLE_NAME_HISTORI + "("
            + ID_HISTORI +" integer primary key autoincrement, "
            + TANGGAL+ " varchar(100) not null);";

    private static final String db_create_detail_histori = "create table "
            + TABLE_NAME_DETAIL_HISTORI + "("
            + ID_DETAIL +" integer primary key autoincrement, "
            + ID_HISTORI_DETAIL +" integer (10) not null, "
            + ID_TEMPAT_SAMPAH + " varchar(100) not null,"
            + LATITUDE + " varchar (100) not null, "
            + LONGTITUDE + " varchar (100) not null);";

    public DBHelperSqlLite(Context context) {
        super(context, db_name, null, db_version);
        // Auto generated
    }

    //mengeksekusi perintah SQL di atas untuk membuat tabel database baru
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(db_create_histori);
        db.execSQL(db_create_detail_histori);
    }

    // dijalankan apabila ingin mengupgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelperSqlLite.class.getName(),"Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DELETE FROM "+ TABLE_NAME_DETAIL_HISTORI);
        db.execSQL("DELETE FROM "+ TABLE_NAME_HISTORI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HISTORI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DETAIL_HISTORI);
        onCreate(db);

    }
}
