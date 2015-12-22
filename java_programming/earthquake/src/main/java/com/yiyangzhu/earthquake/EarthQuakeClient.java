package com.yiyangzhu.earthquake;

import java.util.ArrayList;
import java.util.List;

public class EarthQuakeClient
{

    public EarthQuakeClient() {
        // TODO Auto-generated constructor stub
    }

    public ArrayList<QuakeEntry> filterByMagnitude(ArrayList<QuakeEntry> quakeData,
    double magMin) {
        ArrayList<QuakeEntry> answer = new ArrayList<QuakeEntry>();
        for (QuakeEntry entry : quakeData) {
            if (entry.getMagnitude() > magMin) {
                answer.add(entry);
            }
        }
        return answer;
    }

    public ArrayList<QuakeEntry> filterByDistanceFrom(ArrayList<QuakeEntry> quakeData,
            double distMax, Location from) {
        ArrayList<QuakeEntry> answer = new ArrayList<QuakeEntry>();
        for (QuakeEntry entry : quakeData) {
            Location quakeLocation = entry.getLocation();
            if (quakeLocation.distanceTo(from) < distMax) {
                answer.add(entry);
            }
        }
        return answer;
    }

    public ArrayList<QuakeEntry> filterByDepth(ArrayList<QuakeEntry> quakeData, double minDepth, double maxDepth) {
        ArrayList<QuakeEntry> result = new ArrayList<>();
        for (QuakeEntry entry : quakeData) {
            if (entry.getDepth() > minDepth && entry.getDepth() < maxDepth) {
                result.add(entry);
            }
        }
        return result;
    }

    public ArrayList<QuakeEntry> filterByPhrase(ArrayList<QuakeEntry> quakeData, String where, String phrase) {
        ArrayList<QuakeEntry> result = new ArrayList<>();
        for (QuakeEntry entry : quakeData) {
            switch (where) {
                case "start":
                    if (entry.getInfo().startsWith(phrase)) {
                        result.add(entry);
                    }
                    break;
                case "end":
                    if (entry.getInfo().endsWith(phrase)) {
                        result.add(entry);
                    }
                    break;
                case "any":
                    if (entry.getInfo().contains(phrase)) {
                        result.add(entry);
                    }
                    break;
            }
        }
        return result;
    }

    public void dumpCSV(ArrayList<QuakeEntry> list){
        System.out.println("Latitude,Longitude,Magnitude,Info");
        for(QuakeEntry qe : list){
            System.out.printf("%4.2f,%4.2f,%4.2f,%s\n",
                qe.getLocation().getLatitude(),
                qe.getLocation().getLongitude(),
                qe.getMagnitude(),
                qe.getInfo());
        }

    }

    public void bigQuakes() {
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedatasmall.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");
        List<QuakeEntry> result = filterByMagnitude(list, 5);
        for (QuakeEntry entry : result) {
            System.out.println(entry);
        }
        System.out.println(String.format("Found %d quakes that match that criteria", result.size()));
    }

    public void closeToMe(){
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedatasmall.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");

        // This location is Durham, NC
        //Location city = new Location(35.988, -78.907);

        // This location is Bridgeport, CA
        Location city =  new Location(38.17, -118.82);

        List<QuakeEntry> closeQuakes = filterByDistanceFrom(list, 1000000, city);
        for (QuakeEntry entry : closeQuakes) {
            System.out.println(String.format("%f %s", entry.getLocation().distanceTo(city)/1000, entry.getInfo()));
        }
        System.out.println(String.format("Found %d quakes that match that criteria", closeQuakes.size()));
    }

    public void quakesOfDepth() {
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedatasmall.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");

        List<QuakeEntry> closeQuakes = filterByDepth(list, -10000, -5000);
        for (QuakeEntry entry : closeQuakes) {
            System.out.println(entry);
        }
        System.out.println(String.format("Found %d quakes that match that criteria", closeQuakes.size()));
    }

    public void quakesByPhrase() {
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedatasmall.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");

        List<QuakeEntry> closeQuakes = filterByPhrase(list, "end", "California");
        for (QuakeEntry entry : closeQuakes) {
            System.out.println(entry);
        }
        System.out.println(String.format("Found %d quakes that match that criteria", closeQuakes.size()));
    }

    public void createCSV(){
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedata.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        dumpCSV(list);
        System.out.println("# quakes read: "+list.size());
    }

    public static void main(String[] args) {
        EarthQuakeClient client = new EarthQuakeClient();
        client.quakesByPhrase();
    }
}
