import java.io.*;
import java.util.*;

public class Simulator {
    private int request = 0;
    private int curQuality = 0;

    public void simulate(int minBuf, int maxBuf, String file) throws IOException {
        //encodings, 0=250, 1=500, 2=850, 3 = 1300 Kbit/s
        int curBuf = 0;
        int request = 0;
        Map<Integer, Integer> encodings = new HashMap<Integer, Integer>();
        encodings.put(0,250);
        encodings.put(1,500);
        encodings.put(2,850);
        encodings.put(3,1300);
        //log into throughput, tp
        //ArrayDeque tp = new ArrayDeque();
        int tp;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null){
            checkRequest();
            String[] parts = line.split("\\s+");
            //tp.add(Integer.parseInt(parts[4]));
            tp = Integer.parseInt(parts[4])*8;

            //om vi ska höja
            if(tp >= encodings.get(curQuality+1)){
                request++;
            //om vi ska sänka
            }else if(tp < encodings.get(curQuality)){
                if(tp < encodings.get(curQuality-1)) request--;
                request--;
            }

        }

    }
    private void checkRequest() {
        if (request > curQuality) {
            if(curQuality < 3) curQuality++;
            else request = 3;
        }else if (request < curQuality) {
            if(curQuality>0) {
                if (request == (curQuality - 1)) curQuality--;
                else curQuality = curQuality-2;
            }else request = 0;
        }
    }

    public static void main(String[] args) throws IOException{
        Simulator sim = new Simulator();
        sim.simulate(4,6, "C:\\Users\\Wille\\IdeaProjects\\TDDD66_2\\src\\dl.log");
    }
}

