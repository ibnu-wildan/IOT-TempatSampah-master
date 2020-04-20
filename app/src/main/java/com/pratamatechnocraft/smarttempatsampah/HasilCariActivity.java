package com.pratamatechnocraft.smarttempatsampah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pratamatechnocraft.smarttempatsampah.Adapter.AdapterRecycleViewTimeLine;
import com.pratamatechnocraft.smarttempatsampah.Database.DBDataSource;
import com.pratamatechnocraft.smarttempatsampah.Model.TimeLine;
import com.pratamatechnocraft.smarttempatsampah.Utils.directionLib.DirectionFinder;
import com.pratamatechnocraft.smarttempatsampah.Utils.directionLib.DirectionFinderListener;
import com.pratamatechnocraft.smarttempatsampah.Utils.directionLib.Route;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HasilCariActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowCloseListener, DirectionFinderListener {

    private GoogleMap mMap;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private MarkerOptions markerAwal;
    private HashMap<String, HashMap<String, String>> hashMapHashMapStatus = new HashMap<>();
    private boolean isInfoWindowShown = false;
    String lastMarkerKlik;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private Integer duration, distance;
    private DBDataSource dbDataSource;
    private Intent intent;
    private Button buttonNavigasikan;
    String wayPoints = "";

    private RecyclerView recyclerViewTimeLine;
    private ArrayList<TimeLine> timeLines;
    private AdapterRecycleViewTimeLine adapterRecycleViewTimeLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_cari);
        intent = getIntent();
        /*TOOLBAR*/
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarHasilCari);
        toolbar.setSubtitleTextColor( ContextCompat.getColor(this, R.color.colorIcons) );
        this.setTitle("Hasil Cari Rute");
        setSupportActionBar(toolbar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle( intent.getStringExtra("tanggal") );
        /*TOOLBAR*/

        /*MAP*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapHasil);
        mapFragment.getMapAsync(HasilCariActivity.this);
        /*MAP*/

        //Inisialisasi LinearLayout sebagai base bottom sheet view
        final View bottomsheet = findViewById(R.id.bs_ll);

        //Assign LinearLayout tersebut ke BottomSheetBehavior
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);

        //Set BottomSheet view supaya bisa disembunyikan semuanya
        bottomSheetBehavior.setHideable(true);

        /*Inisialisasi Button*/
        buttonNavigasikan = findViewById(R.id.buttonNavigasikan);

        buttonNavigasikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbDataSource = new DBDataSource(HasilCariActivity.this);
                dbDataSource.open();
                String latitude = dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).get(0).getLatitude();
                String longtitude = dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).get(0).getLongtitude();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+latitude+","+longtitude+"&destination="+latitude+","+longtitude+"&travelmode=driving&waypoints="+wayPoints));
                startActivity(intent);
            }
        });

        recyclerViewTimeLine = findViewById(R.id.recyclerViewTimeLine);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.clear();
        databaseReference.child("lokasi_utama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                markerAwal = new MarkerOptions().position(new LatLng(dataSnapshot.child("latitude").getValue(Double.class), dataSnapshot.child("longtitude").getValue(Double.class)));
                markerAwal.title(dataSnapshot.child("nama").getValue(String.class));
                markerAwal.icon(bitmapDescriptorFromVector(HasilCariActivity.this, R.drawable.ic_garbage_truck));
                mMap.addMarker(markerAwal);
                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(dataSnapshot.child("latitude").getValue(Double.class), dataSnapshot.child("longtitude").getValue(Double.class)))
                        .zoom(15)
                        .bearing(0)
                        .tilt(45)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("tempat_sampah").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(dataSnapshot1.child("latitude").getValue(Double.class), dataSnapshot1.child("longtitude").getValue(Double.class)));
                    markerOptions.title(dataSnapshot1.child("nama").getValue().toString().trim());
                    String statusTerisi;
                    String statusBaterai;
                    if (dataSnapshot1.child("status_terisi").getValue().toString().equals("0")){
                        statusTerisi="Kosong";
                        markerOptions.icon(bitmapDescriptorFromVector(HasilCariActivity.this,R.drawable.ic_trash_kosong));
                    }else if(dataSnapshot1.child("status_terisi").getValue().toString().equals("1")){
                        statusTerisi="Setengah";
                        markerOptions.icon(bitmapDescriptorFromVector(HasilCariActivity.this,R.drawable.ic_trash_setengah));
                    }else{
                        statusTerisi="Penuh";
                        markerOptions.icon(bitmapDescriptorFromVector(HasilCariActivity.this,R.drawable.ic_trash_penuh));
                    }
                    if (dataSnapshot1.child("status_baterai").getValue().toString().equals("0")){
                        statusBaterai="Kosong";
                    }else if(dataSnapshot1.child("status_baterai").getValue().toString().equals("1")){
                        statusBaterai="Setengah";
                    }else{
                        statusBaterai="Penuh";
                    }
                    Marker marker = mMap.addMarker(markerOptions);
                    mMap.addMarker(markerOptions);
                    HashMap<String, String> hashMapStatusValue = new HashMap<>();
                    hashMapStatusValue.put("statusBaterai",statusBaterai);
                    hashMapStatusValue.put("statusTerisi",statusTerisi);
                    hashMapHashMapStatus.put(markerOptions.getTitle(),hashMapStatusValue);
                    mMap.setInfoWindowAdapter(new HasilCariActivity.GoogleMapsInfoWindow(hashMapHashMapStatus));
                    if (isInfoWindowShown && marker.getTitle().equals(lastMarkerKlik)){
                        marker.showInfoWindow();
                        isInfoWindowShown=true;
                        lastMarkerKlik=marker.getTitle();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);

        loadHistori();
    }

    private void loadHistori(){
        timeLines = new ArrayList<>();
        dbDataSource = new DBDataSource(this);
        dbDataSource.open();
        String pemisah;
        TimeLine awal = new TimeLine(
                dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).get(0).getIdTempatSampah()
        );
        timeLines.add(awal);
        for (int i=1;i<dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).size();i++){
            if (i==dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).size()-1){pemisah="";}else{pemisah="%7C";}
            String longtitude=dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).get(i).getLongtitude().toString().trim();
            String latitude=dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).get(i).getLatitude().toString().trim();
            Log.d("TAG", "waypoint: "+wayPoints);
            wayPoints=wayPoints+latitude+","+longtitude+pemisah;
            TimeLine timeLine = new TimeLine(
                    dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).get(i).getIdTempatSampah()
            );
            timeLines.add(timeLine);
        }
        TimeLine akhir = new TimeLine(
                dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).get(0).getIdTempatSampah()
        );
        timeLines.add(akhir);
        String latitude = dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).get(0).getLatitude();
        String longtitude = dbDataSource.getDetailHistori(intent.getLongExtra("idHistori",0)).get(0).getLongtitude();
        try {
            new DirectionFinder(HasilCariActivity.this, latitude+", "+longtitude, latitude+", "+longtitude, wayPoints).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recyclerViewTimeLine.setHasFixedSize(true);
        recyclerViewTimeLine.setLayoutManager(new LinearLayoutManager(this));
        adapterRecycleViewTimeLine = new AdapterRecycleViewTimeLine( timeLines, this);
        recyclerViewTimeLine.setAdapter( adapterRecycleViewTimeLine );
        adapterRecycleViewTimeLine.notifyDataSetChanged();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!isInfoWindowShown) {
            marker.showInfoWindow();
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(marker.getPosition())
                    .zoom(15)
                    .bearing(0)
                    .tilt(45)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
            isInfoWindowShown = true;
            lastMarkerKlik=marker.getTitle();
        }
        return true;
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        isInfoWindowShown = false;
        lastMarkerKlik="";
    }

    class GoogleMapsInfoWindow implements GoogleMap.InfoWindowAdapter {
        private final View viewGoogleMapsInfoWindow;
        private HashMap<String, HashMap<String, String>> stringArrayListStatus;
        public GoogleMapsInfoWindow(HashMap<String, HashMap<String, String>> stringArrayListStatus) {
            viewGoogleMapsInfoWindow = getLayoutInflater().inflate(R.layout.fragment_info_window_google_maps, null);
            this.stringArrayListStatus= stringArrayListStatus;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(final Marker marker) {
            final ImageView imageViewMarkerInfoWindow;
            final TextView txtTitle, txtStatusTerisi, txtStatusBaterai;
            TableLayout tableStatusInfoWindow;
            imageViewMarkerInfoWindow = viewGoogleMapsInfoWindow.findViewById(R.id.imageViewMarkerInfoWindow);
            txtTitle = viewGoogleMapsInfoWindow.findViewById(R.id.txtTitle);
            txtStatusTerisi = viewGoogleMapsInfoWindow.findViewById(R.id.txtStatusTerisi);
            txtStatusBaterai = viewGoogleMapsInfoWindow.findViewById(R.id.txtStatusBaterai);
            tableStatusInfoWindow = viewGoogleMapsInfoWindow.findViewById(R.id.tableStatusInfoWindow);
            txtTitle.setText(marker.getTitle());
            if (stringArrayListStatus.get(marker.getTitle()) == null){
                tableStatusInfoWindow.setVisibility(View.GONE);
                marker.setIcon(bitmapDescriptorFromVector(HasilCariActivity.this,R.drawable.ic_garbage_truck));
                imageViewMarkerInfoWindow.setBackgroundResource(R.drawable.truck);
            }else{
                tableStatusInfoWindow.setVisibility(View.VISIBLE);
                txtStatusTerisi.setText(stringArrayListStatus.get(marker.getTitle()).get("statusTerisi"));
                txtStatusBaterai.setText(stringArrayListStatus.get(marker.getTitle()).get("statusBaterai"));

                if (txtStatusTerisi.getText().equals("Kosong")){
                    marker.setIcon(bitmapDescriptorFromVector(HasilCariActivity.this,R.drawable.ic_trash_kosong));
                    imageViewMarkerInfoWindow.setBackgroundResource(R.drawable.kosong);
                }else if(txtStatusTerisi.getText().equals("Setengah")){
                    marker.setIcon(bitmapDescriptorFromVector(HasilCariActivity.this,R.drawable.ic_trash_setengah));
                    imageViewMarkerInfoWindow.setBackgroundResource(R.drawable.setengah);
                }else{
                    marker.setIcon(bitmapDescriptorFromVector(HasilCariActivity.this,R.drawable.ic_trash_penuh));
                    imageViewMarkerInfoWindow.setBackgroundResource(R.drawable.penuh);
                }
            }

            return viewGoogleMapsInfoWindow;
        }
    }

    @Override
    public void onDirectionFinderStart() {
        duration=0;
        distance=0;
        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        for (Route route : routes) {

            distance=distance+route.distance.value;
            duration=duration+route.duration.value;
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(R.color.colorAccent).
                    width(15);
            for (int i = 0; i < route.points.size(); i++){
                polylineOptions.add(route.points.get(i));
            }
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
