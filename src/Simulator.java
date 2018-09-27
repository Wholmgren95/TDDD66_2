import java.io.*;
import java.util.*;

public class Simulator {
    public static void main(String args[]) throws IOException {
        //log into throughput, tp
        ArrayDeque tp = new ArrayDeque();
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Wille\\IdeaProjects\\TDDD66_2\\src\\dl.log"));
        String line;
        while ((line = reader.readLine()) != null){
            String[] parts = line.split("\\s+");
            tp.add(Integer.parseInt(parts[4]));
        }


    }
}

