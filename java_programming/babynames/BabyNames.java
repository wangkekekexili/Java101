
import java.io.File;

import edu.duke.*;
import org.apache.commons.csv.*;

/**
 * Write a description of BabyNames here.
 * 
 * @author kewang 
 */
public class BabyNames {

    public static final int NAME_COLUMN_INDEX = 0;
    public static final int GENDER_COLUMN_INDEX = 1;
    public static final int NUMBER_COLUMN_INDEX = 2;
    
    public static final String GENDER_FEMALE = "F";
    public static final String GENDER_MALE = "M";
    
    public static void totalBirths() {
        
        int numberOfUniqueNames = 0;
        int numberOfUniqueBoyNames = 0;
        int numberOfUniqueGirlNames = 0;
        
        FileResource fr = new FileResource();
        for (CSVRecord record : fr.getCSVParser(false)) {
            numberOfUniqueNames++;
            if (GENDER_FEMALE.equals(record.get(GENDER_COLUMN_INDEX))) {
                numberOfUniqueGirlNames++;
            } else {
                numberOfUniqueBoyNames++;
            }
        }
        
        String result = String.format("Total number of names: %d\n" +
                "Total number of gril names: %d\n" + 
                "Total number of boy names: %d\n",
                numberOfUniqueNames, numberOfUniqueGirlNames, numberOfUniqueBoyNames);
        System.out.print(result);
        
    }
    
    public static int getRank(int year, String name, String gender) {
        if (year < 1880 || year > 2014) {
            return -1;
        }
        if ("".equals(name)) {
            return -1;
        }
        if (!GENDER_FEMALE.equals(gender) && !GENDER_MALE.equals(gender)) {
            return -1;
        }
        
        String fileName = getFileNameByYear(year);
        FileResource fr = new FileResource(fileName);
        
        int rank = 0;
        for (CSVRecord record : fr.getCSVParser(false)) {
            if (gender.equals(record.get(GENDER_COLUMN_INDEX))) {
                rank++;
                if (name.equals(record.get(NAME_COLUMN_INDEX))) {
                    return rank;
                }
            }
        }
        return -1;
        
    }
    
    public static String getName(int year, int rank, String gender) {
        if (year < 1880 || year > 2014) {
            return "NO NAME";
        }
        if (rank < 1) {
            return "NO NAME";
        }
        if (!GENDER_FEMALE.equals(gender) && !GENDER_MALE.equals(gender)) {
            return "NO NAME";
        }
        
        String fileName = getFileNameByYear(year);
        FileResource fr = new FileResource(fileName);
        
        int currentRank = 0;
        for (CSVRecord record : fr.getCSVParser(false)) {
            if (gender.equals(record.get(GENDER_COLUMN_INDEX))) {
                currentRank++;
                if (currentRank == rank) {
                    return record.get(NAME_COLUMN_INDEX);
                }
            }
        }
        return "NO NAME";
        
    }
    
    public static void whatIsNameInYear(String name, int yearBorn, int newYear, String gender) {
        int rankInYearBorn = getRank(yearBorn, name, gender);
        if (rankInYearBorn == -1) {
            System.out.println("The name " + name + " is not on the list.");
            return;
        } 
        String newName = getName(newYear, rankInYearBorn, gender);
        if ("NO NAME".equals(newName)) {
            System.out.println("Sorry, but no corresponding name is found.");
            return;
        }
        System.out.println(String.format("%s born in %d would be %s if she was born in %d.", name, yearBorn, newName, newYear));
    }
    
    /**
     * This method selects a range of files to process and returns an integer, the year with the highest rank for the name and gender.
     * 
     * @param name the given name to search
     * @param gender the given gender to search
     * 
     * @return the year with the highest rank for the name and gender; -1 if not found
     */
    public static int yearOfHighestRank(String name, String gender) {
        
        int result = -1;
        String fileName = "";
        
        DirectoryResource dr = new DirectoryResource();
        for (File file : dr.selectedFiles()) {
            FileResource fr = new FileResource(file);
            int currentRank = 0;
            for (CSVRecord  record : fr.getCSVParser(false)) {
                if (gender.equals(record.get(GENDER_COLUMN_INDEX))) {
                    currentRank++;
                    if (name.equals(record.get(NAME_COLUMN_INDEX))) {
                        if (result == -1 || currentRank < result) {
                            result = currentRank;
                            fileName = file.getName();
                        }
                    }
                }
            }
        }
    
        if (result == -1) {
            return -1;
        } else {
            return Integer.parseInt(fileName.substring(3, 7));
        }
    }
    
    public static double getAverageRank(String name, String gender) {
        int numberOfFiles = 0;
        int rankSum = 0;
        
        DirectoryResource dr = new DirectoryResource();
        for (File file : dr.selectedFiles()) {
            FileResource fr = new FileResource(file);
            numberOfFiles++;
            int currentRank = 0;
            boolean ifFound = false;
            for (CSVRecord record : fr.getCSVParser(false)) {
                if (gender.equals(record.get(GENDER_COLUMN_INDEX))) {
                    currentRank++;
                    if (name.equals(record.get(NAME_COLUMN_INDEX))) {
                        rankSum += currentRank;
                        ifFound = true;
                        break;
                    }
                }
            }
            if (!ifFound) {
                return -1;
            }
        }
        
        return (double)rankSum / numberOfFiles;
    }
    
    public static int getTotalBirthsRankedHigher(int year, String name, String gender) {
        int result = 0;
        for (CSVRecord record : new FileResource(getFileNameByYear(year)).getCSVParser(false)) {
            if (gender.equals(record.get(GENDER_COLUMN_INDEX))) {
                if (name.equals(record.get(NAME_COLUMN_INDEX))) {
                    return result;
                } else {
                    result += Integer.parseInt(record.get(NUMBER_COLUMN_INDEX));
                }
            }
        }
        return result;
    }
    
    private static final String getFileNameByYear(int year) {
        return String.format("us_babynames%sus_babynames_by_year%syob%s.csv",
                File.separator, File.separator, Integer.toString(year));
    }

}
