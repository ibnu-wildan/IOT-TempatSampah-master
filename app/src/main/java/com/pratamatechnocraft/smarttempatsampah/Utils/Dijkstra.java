package com.pratamatechnocraft.smarttempatsampah.Utils;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dijkstra{
    private List<HashMap<String, HashMap<String, Double>>> locations = new ArrayList<>();
    private HashMap<String, Double> longitudes = new HashMap<>();
    private HashMap<String, Double> latitudes = new HashMap<>();
    private List<String> shortest_route = new ArrayList<>();
    private List<HashMap<String, HashMap<String, Double>>> hasil= new ArrayList<>();
    private List<List<String>> shortest_routes = new ArrayList<>();
    private Double shortest_distance = 0.0;
    private List<List<String>> all_routes = new ArrayList<>();

    public void add(String name, Double longitude, Double latitude) {
        HashMap<String, Double> lokasi = new HashMap<>();
        lokasi.put("longitude", longitude);
        lokasi.put("latitude", latitude);
        HashMap<String, HashMap<String, Double>> value = new HashMap<>();
        value.put(name,lokasi);
        this.locations.add(value);
    }

    public void compute() {
        List<HashMap<String, HashMap<String, Double>>> locations = this.locations;
        for (int i=0;i<locations.size();i++) {
            for (Map.Entry<String, HashMap<String, Double>> location : locations.get(i).entrySet()) {
                this.longitudes.put(location.getKey(), location.getValue().get("longitude"));
                this.latitudes.put(location.getKey(), location.getValue().get("latitude"));
            }
        }

        List<String> location = new ArrayList<>();
        for (int i=0;i<this.locations.size();i++) {
            String key1 = null;
            for (Map.Entry<String, HashMap<String, Double>> key : this.locations.get(i).entrySet()){
                key1=key.getKey();
            }
            location.add(key1);
        }

        //this.all_routes = this.array_permutations(location,new ArrayList<String>());
        this.all_routes = this.permute(location);
        for (int i=0;i<this.all_routes.size();i++) {
            double total=0.0;
            for (int j=0;j<this.all_routes.get(i).size();j++){
                if (j<location.size()-1){
                    Double latitude1=this.latitudes.get(all_routes.get(i).get(j));
                    Double longtitude1=this.longitudes.get(all_routes.get(i).get(j));
                    Double latitude2=this.latitudes.get(all_routes.get(i).get(j+1));
                    Double longtitude2=this.longitudes.get(all_routes.get(i).get(j+1));
                    total += this.getDistanceBetween(new LatLng(latitude1, longtitude1), new LatLng(latitude2, longtitude2));
                }
            }
            if (total<this.shortest_distance || this.shortest_distance==0.0){
                this.shortest_distance = total;
                this.shortest_route = all_routes.get(i);
                this.shortest_routes = new ArrayList<>();
            }
            if (total == this.shortest_distance){
                this.shortest_routes.add(all_routes.get(i));
            }
        }
    }

    private List<List<String>> permute(List<String> arr) {
        List<List<String>> list = new ArrayList<>();
        permuteHelper(list, new ArrayList<String>(), arr);
        return list;
    }

    private void permuteHelper(List<List<String>> list, List<String> resultList, List<String> arr){
        // Base case
        if(resultList.size() == arr.size()){
            list.add(new ArrayList<>(resultList));
        }
        else{
            for(int i = 0; i < arr.size(); i++){

                if(resultList.contains(arr.get(i)))
                {
                    // If element already exists in the list then skip
                    continue;
                }
                // Choose element
                resultList.add(arr.get(i));
                // Explore
                permuteHelper(list, resultList, arr);
                // Unchoose element
                resultList.remove(resultList.size() - 1);
            }
        }
    }

    private static Double getDistanceBetween(LatLng latLon1, LatLng latLon2) {
        if (latLon1 == null || latLon2 == null)
            return null;
        float[] result = new float[1];
        Location.distanceBetween(latLon1.latitude, latLon1.longitude,
                latLon2.latitude, latLon2.longitude, result);
        return (double) result[0];
    }

    public List<String> shortest_route(){
        return this.shortest_route;
    }
    // returns an array of any routes that are exactly the same distance as the shortest (ie the shortest backwards normally)
    public List<List<String>> matching_shortest_routes(){
        return this.shortest_routes;
    }
    // the shortest possible distance to travel
    public Double shortest_distance(){
        return this.shortest_distance;
    }
    // returns an array of all the possible routes
    public List<List<String>> routes(){
        return this.all_routes;
    }

    public void setHasil(){
        List<HashMap<String, HashMap<String, Double>>> hasil= new ArrayList<>();
        for (int i=0;i<this.shortest_route().size();i++){
            HashMap<String, Double> lokasi = new HashMap<>();
            HashMap<String, HashMap<String, Double>> maker = new HashMap<>();
            lokasi.put("latitude", this.latitudes.get(this.shortest_route().get(i)));
            lokasi.put("longtitude", this.longitudes.get(this.shortest_route().get(i)));
            maker.put(this.shortest_route().get(i),lokasi);
            hasil.add(maker);
        }
        this.hasil=hasil;
    }

    public  List<HashMap<String, HashMap<String, Double>>> hasil(){
        return this.hasil;
    }
}