import java.io.*;
import java.util.*;

public class Simulator {
    private int minBuf = 4;
    private int maxBuf = 6;
    private int request = 0;
    private int curQuality = 0;
    private float abw =0;
    private Map<Integer, Integer> encodings = new HashMap<Integer, Integer>();
    Fragment fragment;
    private int curBuf = 0;
    private ArrayList bufferHistory = new ArrayList();
    private ArrayList qualityHistory = new ArrayList();
    private Map<Integer, Integer> requestHistory = new HashMap<Integer, Integer>();
    private int time = 0;

    public void simulate(String file) throws IOException {
        boolean resumePlay = false;
        boolean resumeDownload = true;
        encodings.put(0,250000);
        encodings.put(1,500000);
        encodings.put(2,850000);
        encodings.put(3,1300000);
        //fragment = new Fragment(encodings.get(curQuality));
        //log into throughput, tp
        //ArrayDeque tp = new ArrayDeque();
        int tp;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null){
            qualityHistory.add(curQuality);
            bufferHistory.add(curBuf);
            String[] parts = line.split("\\s+");
            //tp.add(Integer.parseInt(parts[4]));
            tp = Integer.parseInt(parts[4])*8;
            if(curBuf<minBuf) resumeDownload = true;
            if(curBuf>=minBuf) resumePlay=true;
            if(curBuf>=maxBuf){
                resumeDownload = false;
            }
            if(time == 0 || (curBuf<maxBuf && fragment.isDownloaded())){
           //     System.out.println(curQuality);
               // checkRequest();
                //requestHistory.put(time,request);
                fragment = new Fragment(encodings.get(curQuality));
            }
            if(!fragment.isDownloaded() && resumeDownload){
                downloadFragment(tp);
            }


            if(curBuf==0){
                System.out.println("Pause");
                resumePlay = false;
            }
            else if(resumePlay){
                System.out.println("Play");
                curBuf--;
            }
            time++;
        }
    }
    public void downloadFragment(int tp){
        if(fragment.download(tp)) {
            curBuf += 4;
            int newEst = fragment.getSize() / fragment.getTime();
            //α = 1 if option 1, 0.5? if option 2
            float α = 0.5f;
            //available bandwidth
            float option = (1 - α) * abw + α * newEst;
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
            requestHistory.put(time,request);
        }
    }

    public void checkRequest() {
        if (request > curQuality) {
            if(curQuality < 3) curQuality++;
            else request = 3;
        }else if (request < curQuality) {
            if(curQuality>0) {
                if (curQuality>1 && request == curQuality-2) curQuality-=2;
                else curQuality -=1;
            }else request = 0;
        }
    }

    private void write(){
        try {
            //buffersize
            //BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Wille\\IdeaProjects\\TDDD66_2\\src\\input1.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\cygwin64\\TDDD66_2\\src\\input1.txt"));
            //current quality
            //BufferedWriter writer2 = new BufferedWriter(new FileWriter("C:\\Users\\Wille\\IdeaProjects\\TDDD66_2\\src\\input2.txt"));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("C:\\cygwin64\\TDDD66_2\\src\\input2.txt"));
            //requested quality
            //BufferedWriter writer3 = new BufferedWriter(new FileWriter("C:\\Users\\Wille\\IdeaProjects\\TDDD66_2\\src\\input3.txt"));
            BufferedWriter writer3 = new BufferedWriter(new FileWriter("C:\\cygwin64\\TDDD66_2\\src\\input3.txt"));
            for (int i = 0; i < bufferHistory.size(); i++) {
                writer.write(i + " " + bufferHistory.get(i) + "\n");
                writer2.write(i + " " + qualityHistory.get(i) + "\n");
                if(requestHistory.containsKey(i)) writer3.write(i + " " + requestHistory.get(i) + "\n");
            }
            writer.close();
            writer2.close();
            writer3.close();
            System.out.println(time);

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws IOException{
        Simulator sim = new Simulator();
        sim.simulate("C:\\cygwin64\\TDDD66_2\\src\\dl.log");
        //sim.simulate("C:\\Users\\Wille\\IdeaProjects\\TDDD66_2\\src\\dl.log");
        sim.write();

    }
}

