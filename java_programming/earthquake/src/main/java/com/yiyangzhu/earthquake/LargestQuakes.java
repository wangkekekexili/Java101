package com.yiyangzhu.earthquake;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Find n largest quakes.
 *
 * @author kewang
 */
public class LargestQuakes {

    public ArrayList<QuakeEntry> getLargest(ArrayList<QuakeEntry> quakeData, int howMany){
        ArrayList<QuakeEntry> result = new ArrayList<QuakeEntry>();
        if (howMany >= quakeData.size()) {
            result.addAll(quakeData);
        } else {
            ArrayList<QuakeEntry> copy = new ArrayList<>();
            copy.addAll(quakeData);
            copy.sort(new Comparator<QuakeEntry>() {
                @Override
                public int compare(QuakeEntry first, QuakeEntry second) {
                    if (first.getMagnitude() > second.getMagnitude()) {
                        return -1;
                    } else if (first.getMagnitude() < second.getMagnitude()) {
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

    public void findLargestQuakes(){
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedatasmall.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size());

        ArrayList<QuakeEntry> largest = getLargest(list, 10);
        for (QuakeEntry entry : largest) {
            System.out.println(entry);
        }
        System.out.println("number found: " + largest.size());
    }

    public static void main(String[] args) {
        LargestQuakes test = new LargestQuakes();
        test.findLargestQuakes();
    }
}
