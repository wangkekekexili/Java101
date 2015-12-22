package com.yiyangzhu.earthquake;
/**
 * Find N-closest quakes
 * 
 * @author Duke Software/Learn to Program
 * @version 1.0, November 2015
 */

import java.util.*;

public class ClosestQuakes
{
    public ArrayList<QuakeEntry> getClosest(ArrayList<QuakeEntry> quakeData, Location current, int howMany){
        ArrayList<QuakeEntry> result = new ArrayList<QuakeEntry>();
        if (howMany >= quakeData.size()) {
            result.addAll(quakeData);
        } else {
            ArrayList<QuakeEntry> copy = new ArrayList<>();
            copy.addAll(quakeData);
            copy.sort(new Comparator<QuakeEntry>() {
                @Override
                public int compare(QuakeEntry first, QuakeEntry second) {
                    double firstToCurrent = first.getLocation().distanceTo(current);
                    double secondToCurrent = second.getLocation().distanceTo(current);
                    if (firstToCurrent < secondToCurrent) {
                        return -1;
                    } else if (firstToCurrent > secondToCurrent) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            for (int index = 0; index < howMany; index++) {
                result.add(copy.get(index));
            }
        }
        return result;
    }

    public void findClosestQuakes(){
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedatasmall.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size());

        Location jakarta  = new Location(-6.211,106.845);

        ArrayList<QuakeEntry> close = getClosest(list,jakarta,10);
        for(int k=0; k < close.size(); k++){
            QuakeEntry entry = close.get(k);
            double distanceInMeters = jakarta.distanceTo(entry.getLocation());
            System.out.printf("%4.2f\t %s\n", distanceInMeters/1000,entry);
        }
        System.out.println("number found: "+close.size());
    }

    public static void main(String[] args) {
        ClosestQuakes test = new ClosestQuakes();
        test.findClosestQuakes();
    }
}
