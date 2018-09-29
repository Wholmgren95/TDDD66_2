import java.io.*;
import java.util.*;

public class Simulator {
    private int minBuf = 4;
    private int maxBuf = 6;
    private int request = 0;
    private int curQuality = 0;
    private int abw =0;
    Map<Integer, Integer> encodings = new HashMap<Integer, Integer>();
    Fragment fragment;
    private int curBuf = 0;

    public void simulate(String file) throws IOException {
        //encodings, 0=250, 1=500, 2=850, 3 = 1300 Kbit/s

        int request = 0;

        encodings.put(0,250);
        encodings.put(1,500);
        encodings.put(2,850);
        encodings.put(3,1300);
        fragment = new Fragment(encodings.get(curQuality));
        //log into throughput, tp
        //ArrayDeque tp = new ArrayDeque();
        int tp;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null){

            String[] parts = line.split("\\s+");
            //tp.add(Integer.parseInt(parts[4]));
            tp = Integer.parseInt(parts[4])*8;
            if(curBuf<maxBuf && fragment.isDownloaded()){
                fragment = new Fragment(encodings.get(curQuality));
            }
            if(!fragment.isDownloaded()){
                downloadFragment(tp);
            }
            if(curBuf<minBuf) System.out.println("Pause");
            else{
                System.out.println("Play");
                curBuf--;
            }

        }
    }
    public void downloadFragment(int tp){
        if(fragment.download(tp)) {
            curBuf += 4;
            int newEst = fragment.getSize() / fragment.getTime();
            //α = 1 if option 1, 0.5? if option 2
            int α = 1;
            //available bandwidth
            int option = (1 - α) * abw + α * newEst;
            abw = option;
            //om vi ska höja
            if (curQuality<3 && abw >= encodings.get(curQuality + 1)) {
                request++;
                //om vi ska sänka
            } else if (abw < encodings.get(curQuality)) {
                if (curQuality>0 && abw < encodings.get(curQuality - 1)) request--;
                request--;
            }
            checkRequest();
        }
    }

    public void checkRequest() {
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
        sim.simulate("C:\\Users\\Wille\\IdeaProjects\\TDDD66_2\\src\\dl.log");
    }
}

