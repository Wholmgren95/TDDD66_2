public class Fragment {
    private int size;
    private int downloaded;
    private boolean isDownloaded;
    private int downloadTime;

    public Fragment(int quality){
        size = quality*4;
        downloaded = 0;
        isDownloaded = false;
        downloadTime = 0;
    }

    public boolean download(int bits){
        //FEL PÅ DOM TVÅ UNDER
        downloaded = bits+downloaded;
        if(downloaded >=size) isDownloaded = true;
        else isDownloaded = false;
        downloadTime++;
        return isDownloaded;
    }
    public int getSize(){
        return size;
    }

    public int getTime(){
        return downloadTime;
    }

    public boolean isDownloaded(){
        return isDownloaded;
    }
}
