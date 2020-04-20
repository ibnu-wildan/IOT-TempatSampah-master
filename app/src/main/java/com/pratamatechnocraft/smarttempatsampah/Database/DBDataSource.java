package com.pratamatechnocraft.smarttempatsampah.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.pratamatechnocraft.smarttempatsampah.Model.DetailHistori;
import com.pratamatechnocraft.smarttempatsampah.Model.Histori;
import java.util.ArrayList;

public class DBDataSource {
    private SQLiteDatabase database;
    private DBHelperSqlLite dbHelperSqlLite;
    private String[] allColumnsHistori = {
            dbHelperSqlLite.ID_HISTORI,
            dbHelperSqlLite.TANGGAL
    };
    private String[] allColumnsDetailHistori = {
            dbHelperSqlLite.ID_DETAIL,
            dbHelperSqlLite.ID_HISTORI_DETAIL,
            dbHelperSqlLite.ID_TEMPAT_SAMPAH,
            dbHelperSqlLite.LATITUDE,
            dbHelperSqlLite.LONGTITUDE
    };

    public DBDataSource(Context context){ dbHelperSqlLite = new DBHelperSqlLite(context); }

    public void open() throws SQLException { database = dbHelperSqlLite.getWritableDatabase(); }

    public void close() {
        dbHelperSqlLite.close();
    }

    public Histori createHistori(String tanggal){

        ContentValues values = new ContentValues();
        values.put(dbHelperSqlLite.TANGGAL, tanggal);

        long insertId = database.insert(dbHelperSqlLite.TABLE_NAME_HISTORI, null,
                values);

        Cursor cursor = database.query(dbHelperSqlLite.TABLE_NAME_HISTORI,
                allColumnsHistori, dbHelperSqlLite.ID_HISTORI + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        Histori histori= cursorToHistori(cursor);

        cursor.close();

        return histori;
    }

    private Histori cursorToHistori(Cursor cursor) {
        Histori histori = new Histori();
        histori.setIdHistori(cursor.getLong(0));
        histori.setTanggal(cursor.getString(1));

        return histori;
    }

    public ArrayList<Histori> getSemuaHistori() {
        ArrayList<Histori> historis = new ArrayList<Histori>();

        Cursor cursor = database.query(DBHelperSqlLite.TABLE_NAME_HISTORI,
                allColumnsHistori, null, null, null, null, DBHelperSqlLite.ID_HISTORI + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Histori histori= cursorToHistori(cursor);
            historis.add(histori);
            cursor.moveToNext();
        }
        cursor.close();
        return historis;
    }

    public ArrayList<Histori> getLastHistori() {
        ArrayList<Histori> historis = new ArrayList<Histori>();

        Cursor cursor = database.query(DBHelperSqlLite.TABLE_NAME_HISTORI,
                allColumnsHistori, null, null, null, null, DBHelperSqlLite.TANGGAL + " DESC", "1");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Histori histori= cursorToHistori(cursor);
            historis.add(histori);
            cursor.moveToNext();
        }
        cursor.close();
        return historis;
    }

    public void hapusHistoriSatu(long idHistori){
        database.delete(DBHelperSqlLite.TABLE_NAME_HISTORI, "id_histori = "+idHistori, null);
        database.delete(DBHelperSqlLite.TABLE_NAME_DETAIL_HISTORI, "id_histori_detail = "+idHistori, null);
    }

    public boolean totalHistori()
    {
        //select query
        Cursor cursor = database.rawQuery("select * from data_histori", null);
        //ambil data yang pertama
        cursor.moveToFirst();
        int count= cursor.getCount();
        //tutup sambungan
        cursor.close();
        if (count>0){
            return true;
        }else{
            return false;
        }
    }

    public DetailHistori createDetailHistori(long idHistoriDetail, String idTempatSampah, String latitude,String longtitude){

        ContentValues values = new ContentValues();
        values.put(dbHelperSqlLite.ID_HISTORI_DETAIL, idHistoriDetail);
        values.put(dbHelperSqlLite.ID_TEMPAT_SAMPAH, idTempatSampah);
        values.put(dbHelperSqlLite.LATITUDE, latitude);
        values.put(dbHelperSqlLite.LONGTITUDE, longtitude);

        long insertId = database.insert(dbHelperSqlLite.TABLE_NAME_DETAIL_HISTORI, null,
                values);

        Cursor cursor = database.query(dbHelperSqlLite.TABLE_NAME_DETAIL_HISTORI,
                allColumnsDetailHistori, dbHelperSqlLite.ID_DETAIL + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        DetailHistori detailHistori= cursorToDetailHistori(cursor);

        cursor.close();

        return detailHistori;
    }

    private DetailHistori cursorToDetailHistori(Cursor cursor) {
        DetailHistori detailHistori = new DetailHistori();
        detailHistori.setIdDetail(cursor.getLong(0));
        detailHistori.setIdHistoriDetail(cursor.getLong(1));
        detailHistori.setIdTempatSampah(cursor.getString(2));
        detailHistori.setLatitude(cursor.getString(3));
        detailHistori.setLongtitude(cursor.getString(4));
        return detailHistori;
    }


    public ArrayList<DetailHistori> getDetailHistori(long idHistori) {
        ArrayList<DetailHistori> detailHistoris = new ArrayList<>();

        Cursor cursor = database.query(DBHelperSqlLite.TABLE_NAME_DETAIL_HISTORI,
                allColumnsDetailHistori, "id_histori_detail = "+idHistori, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DetailHistori detailHistori= cursorToDetailHistori(cursor);
            detailHistoris.add(detailHistori);
            cursor.moveToNext();
        }
        cursor.close();
        return detailHistoris;
    }

    public void hapusSemua(){
        database.delete(DBHelperSqlLite.TABLE_NAME_HISTORI, null, null);
        database.delete(DBHelperSqlLite.TABLE_NAME_DETAIL_HISTORI, null, null);
    }

}
