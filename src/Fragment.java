public class Fragment {
    private int size;
    private int downloaded;
    private boolean isDownloaded;

    public Fragment(int quality){
        size = quality*4;
        downloaded = 0;
        isDownloaded = false;
    }

    public void download(int bits){
        downloaded = size-bits;
        if(downloaded <=0) isDownloaded = true;
        isDownloaded = false;
    }
    public boolean isDownloaded(){
        return isDownloaded;
    }
}
