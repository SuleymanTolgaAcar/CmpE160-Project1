// Süleyman Tolga Acar
// 2021400237
// 17.03.2023
// Given two input stations this program finds the path between those two stations using recursion.
// It prints the path to the console, also it shows the path on the canvas using StdDraw library.

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


public class suleyman_tolga_acar {
    public static void main(String[] args) {
        // Defining main arrays I use
        String[][] metroLines = new String[10][2];
        String[][] stations = new String[10][];
        String[][] breakPoints = new String[7][];

        readFile(metroLines, stations, breakPoints);
        // Removing the asterix from station names but this doesn't modify the original array. It returns a new one.
        String[][] fixedStations = fixStationNames(stations);

        // Getting console input from the user
        Scanner sc = new Scanner(System.in);
        String start = sc.nextLine();
        String end = sc.nextLine();
        // Check if the input is valid
        if(!(checkValid(start, fixedStations) && checkValid(end, fixedStations))){
            System.out.println("The station names provided are not present in this map.");
            return;
        }
        // Find the path between the two stations
        String solution = findPath(start, end, fixedStations, breakPoints, "", "");
        if(solution == ""){
            System.out.println("These two stations are not connected");
            return;
        }
        // I sliced the solution string just because how I returned it. It has nothing to do with the solution itself.
        solution = solution.substring(1);
        for(String station: solution.split(" ")){
            System.out.println(station);
        }
        // Setting up the canvas properties
        StdDraw.setCanvasSize(1024, 482);
        StdDraw.setXscale(0, 1024);
        StdDraw.setYscale(0, 482);
        StdDraw.setFont(new Font("Helvetica", Font.BOLD, 8));
        StdDraw.enableDoubleBuffering();

        // Drawing everything initially and then showing the path between points
        draw(metroLines, stations);
        showPath(solution.split(" "), fixedStations, metroLines, stations);
    }

    // Check if the station name is valid or not
    public static boolean checkValid(String value, String[][] stations){
        for(String[] line : stations){
            for(String station : line){
                if(station.equals(value))
                    return true;
            }
        }
        return false;
    }

    // Draws the background, lines and station names.
    public static void draw(String[][] metroLines, String[][] stations){
        StdDraw.picture(1024 / 2, 482 / 2, "background.jpg");
        drawLines(metroLines, stations);
        writeTexts(stations);
        StdDraw.show();
    }


    // Read the coordinates.txt file and make the data usable
    public static void readFile (String[][] metroLines, String[][] stations, String[][] breakPoints){
        File coorFile = new File("coordinates.txt");
        try {
            Scanner sc = new Scanner(coorFile);

            int i = 0;
            while(sc.hasNextLine())
            {
                String lineTxt = sc.nextLine();
                String[] lineSplit = lineTxt.split(" ");
                if (i < 20){
                    if (i % 2 == 0){
                        metroLines[i / 2] = lineSplit;
                    }
                    else {
                        stations[i / 2] = lineSplit;
                    }
                }
                else {
                    breakPoints[i - 20] = lineSplit;
                }
                i++;
            }
            sc.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File not found.");
        }
    }

    // Write the station names on the canvas
    public static void writeTexts(String[][] stations){
        StdDraw.setPenColor(StdDraw.BLACK);
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < stations[i].length; j += 2){
                String stationName = stations[i][j];
                String[] coordiantesStr = stations[i][j + 1].split(",");
                int x_coor = Integer.parseInt(coordiantesStr[0]);
                int y_coor = Integer.parseInt(coordiantesStr[1]);
                if(stationName.startsWith("*"))
                    StdDraw.text(x_coor, y_coor + 5, stationName.substring(1));
            }
        }
    }

    // Draw the lines between stations
    public static void drawLines(String[][] metroLines, String[][] stations){
        for (int i = 0; i < 10 ; i++){
            String line = metroLines[i][0];
            String[] colorStr = metroLines[i][1].split(",");
            Integer[] colorInt = new Integer[3];
            for (int j = 0; j < 3; j++){
                colorInt[j] = Integer.parseInt(colorStr[j]);
            }
            StdDraw.setPenColor(colorInt[0], colorInt[1], colorInt[2]);

            for(int j = 1; j < stations[i].length - 2; j += 2){
                String[] coordiantes1Str = stations[i][j].split(",");
                int x_coor1 = Integer.parseInt(coordiantes1Str[0]);
                int y_coor1 = Integer.parseInt(coordiantes1Str[1]);
                String[] coordiantes2Str = stations[i][j + 2].split(",");
                int x_coor2 = Integer.parseInt(coordiantes2Str[0]);
                int y_coor2 = Integer.parseInt(coordiantes2Str[1]);

                StdDraw.setPenRadius(0.012);
                StdDraw.line(x_coor1, y_coor1, x_coor2, y_coor2);
            }

            StdDraw.setPenColor(Color.WHITE);
            for(int j = 1; j < stations[i].length; j += 2){
                String[] coordiantesStr = stations[i][j].split(",");
                int x_coor = Integer.parseInt(coordiantesStr[0]);
                int y_coor = Integer.parseInt(coordiantesStr[1]);

                StdDraw.setPenRadius(0.01);
                StdDraw.point(x_coor, y_coor);
            }
        }
    }

    // Find the path from starting station to end station. It uses recursion to do so. Returns the solution path as a string.
    // If the stations given are not connected in any way, returns an empty string.
    public static String findPath(String current, String end, String[][] stations, String[][] breakPoints, String last, String solution){
        String[] ways = possibleWays(current, stations, breakPoints);
        for(String way : ways){
            if(way.equals(last))
                continue;
            if(way.equals(end))
                return solution + " " + current + " " + way;
            String sol = findPath(way, end, stations, breakPoints, current, solution + " " + current);
            if(sol != "")
                return sol;
        }
        return "";
    }

    // Finds all possible stations which we can travel to in one move from the current station.
    // Returns the possible stations as an array of strings.
    public static String[] possibleWays(String station, String[][] stations, String[][] breakPoints){
        String ways = "";
        for(String[] line : breakPoints){
            if(station.equals(line[0])){
                for(int i = 1; i < line.length; i++){
                    switch (line[i]){
                        case "B1":
                            ways = waysHelper(ways, station, stations[0]);
                            break;
                        case "M1A":
                            ways = waysHelper(ways, station, stations[1]);
                            break;
                        case "M1B":
                            ways = waysHelper(ways, station, stations[2]);
                            break;
                        case "M2":
                            ways = waysHelper(ways, station, stations[3]);
                            break;
                        case "M4":
                            ways = waysHelper(ways, station, stations[4]);
                            break;
                        case "M5":
                            ways = waysHelper(ways, station, stations[5]);
                            break;
                        case "M6":
                            ways = waysHelper(ways, station, stations[6]);
                            break;
                        case "M7":
                            ways = waysHelper(ways, station, stations[7]);
                            break;
                        case "M9":
                            ways = waysHelper(ways, station, stations[8]);
                            break;
                        case "M11":
                            ways = waysHelper(ways, station, stations[9]);
                            break;
                    }
                }
                return ways.split(",");
            }
        }
        for(String[] line : stations){
            ways = waysHelper(ways, station, line);
        }
        return ways.split(",");
    }

    // Just a helper function. According to the index of the current station, returns the possible stations to travel in the same metro line.
    public static String waysHelper(String ways, String station, String[] line){
        int index = search(station, line);
        if(index != -1){
            if(index != 0)
                ways += line[index - 2] + ",";
            if(index != line.length - 2)
                ways += line[index + 2] + ",";
        }
        return ways;
    }

    // Normal search function. I made my own because I didn't know if I can use the built-in ones.
    public static Integer search(String value, String[] array){
        for(int i = 0; i < array.length; i++){
            if(value.equals(array[i]))
                return i;
        }
        return -1;
    }

    // Removes the asterix from the name of the station. Because this way it's easier to check them if they're equal.
    // This does NOT modify the original array. First it deep copies it then apply the process.
    public static String[][] fixStationNames(String[][] stations){
        String[][] fixed = new String[10][];
        for(int i = 0; i < 10; i++){
            fixed[i] = stations[i].clone();
        }
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < fixed[i].length; j++){
                fixed[i][j] = fixed[i][j].replace("*", "");
            }
        }
        return fixed;
    }

    // Takes the solution which findPath function found as a parameter. Then, animates the path by clearing the canvas and
    // redrawing everything just the same except the current station every 300 ms.
    public static void showPath(String[] solution, String[][] stations, String[][] metroLines, String[][] stationsNotFixed){
        String passedStations = "";
        for(String station : solution){
            StdDraw.clear();
            draw(metroLines, stationsNotFixed);
            if(passedStations != ""){
                String[] passedStationsArray = passedStations.substring(1).split(" ");
                for(int i = 0; i < passedStationsArray.length; i += 2){
                    StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                    StdDraw.setPenRadius(0.01);
                    StdDraw.point(Integer.parseInt(passedStationsArray[i]), Integer.parseInt(passedStationsArray[i + 1]));
                }
            }
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < stations[i].length; j += 2){
                    if(station.equals(stations[i][j])){
                        String[] coordiantesStr = stations[i][j + 1].split(",");
                        int x_coor = Integer.parseInt(coordiantesStr[0]);
                        int y_coor = Integer.parseInt(coordiantesStr[1]);
                        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                        StdDraw.setPenRadius(0.02);
                        StdDraw.point(x_coor, y_coor);
                        passedStations += " " + x_coor + " " + y_coor;
                    }
                }
            }
            StdDraw.show();
            StdDraw.pause(300);
        }
    }
}