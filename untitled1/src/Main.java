
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

class Main {

    static class SplitLine {
        String timestampText;
        String linetext;
        String message_type_tag;
        Date timestamp;
        }

    static SplitLine split(String sLine) {
        SplitLine splitLine;
        splitLine = new SplitLine();
        try {


            String dateFormat = "yyyy-MM-dd HH:mm:ss,SSS";
            String dateString = sLine.substring(0, dateFormat.length());
            splitLine.timestampText = dateString;
            SimpleDateFormat sdf = new SimpleDateFormat (dateFormat, Locale.US);
            splitLine.timestamp = sdf.parse(dateString);
            splitLine.linetext = sLine.substring(dateFormat.length());

            } catch (Exception e) {
                e.printStackTrace();
            }
        return splitLine;
    }



    public static void main(String[] args) {
        int nmLines = 0;
        long maxTimeDiff = 0;
        System.out.println("running...");
        FileMgr f = new FileMgr();
        f.openDir("/pjh/");
        String s;
        Date lastDate = null;
        do {
            s = f.getStr();
            ++nmLines;
            if (s == null)
                break;
            SplitLine split;
            split = split(s);
            System.out.println("(" + split.timestamp + ")" + split.linetext);
            if (lastDate != null && lastDate.compareTo(split.timestamp) > 0)
                System.out.println(">>>>>>>> BAD SORT <<<<<<<<<<");
            if (lastDate == null)
                lastDate = split.timestamp;
            long timeDiff =  split.timestamp.getTime() - lastDate.getTime();
            System.out.println("diff" + timeDiff + " ms");
            maxTimeDiff = Math.max(maxTimeDiff, timeDiff);
            if (maxTimeDiff > 1000)
                System.out.println("Big timediff = " + timeDiff);
            lastDate = split.timestamp;

        }
        while (!s.isEmpty()) ;

        System.out.println("Number of lines = " + nmLines);
        System.out.println("max time diff = " + maxTimeDiff + " ms");
    f.closeFile();

    }
}
