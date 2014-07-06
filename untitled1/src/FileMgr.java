import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by higgipa on 6/2/14.
 */
public class FileMgr {
    BufferedReader readFile;
    File dir;

    int currentFileIndex;

    TreeMap<Date, File> logFiles;
    Iterator<File> fileIter ;
    boolean optEchoLine;

   private  void openFile(File file) {

        try {
            System.out.println("Open File " + file.getName());
            if (readFile != null)
                readFile.close();
            readFile = new BufferedReader(new FileReader(file));
            optEchoLine = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isALogFile(String fileName) {
        String logFilePre = "xps_";
        String logFileDateFormat = "yyyyMMddHHmmssSSS";
        String logFilePost = ".log";
        if (fileName.length() != logFilePre.length() + logFileDateFormat.length() + logFilePost.length() )
            return false;
        if (fileName.substring(0, 3).equals(logFilePre))
            return false;
        if (fileName.substring(fileName.length()-3).equals(logFilePost))
            return false;
        if (getDateFromLogFile(fileName) == null)
            return false;
        return true ;

    }

    Date getDateFromLogFile(String fileName) {
        try {
            String logFileDateFormat = "yyyyMMddHHmmssSSS";
            if (fileName.length() < 4+logFileDateFormat.length() )
                return null;
            String dateString = fileName.substring(4, 4+logFileDateFormat.length());

            SimpleDateFormat sdf = new SimpleDateFormat (logFileDateFormat, Locale.US);
            Date date = sdf.parse(dateString);
            return date;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void openDir(String dirName) {
        dir = new File(dirName);
        if (!dir.isDirectory()) {
            System.out.println("Filename " + dirName + " is not a file");
            return;
        }

        logFiles = new TreeMap<Date, File>();
        System.out.println("Open Directory " + dir.getName());
        File[] fileList = dir.listFiles();
        if(fileList == null) {
            System.out.println("Directory " + dirName + " is empty");
            return;
        }
        for (int i=0; i<fileList.length; ++i) {
            if (isALogFile(fileList[i].getName())) {
                 Date date = getDateFromLogFile(fileList[i].getName());
                System.out.println("File: " + fileList[i].getName() + " == " + date);
                logFiles.put(date, fileList[i]);
            }
        }

        fileIter = logFiles.values().iterator();

        }


    public  String getStr() {
        String s = null;
        try {
            while (s == null) {

                if (readFile == null) {
                    File nextFile = null;
                    if (fileIter.hasNext())
                        nextFile = fileIter.next();
                    if (nextFile == null)
                        return null;
                    System.out.println("Open File: " + nextFile.getName());
                    readFile = new BufferedReader(new FileReader(nextFile));
                     ++currentFileIndex;
                }

                s = readFile.readLine();
                if (s != null && optEchoLine)
                System.out.println(s);
                if (s != null)
                    return s;
                else
                    readFile = null;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void closeFile() {

        if (readFile == null)
            return;
        try {
            readFile.close();
            readFile = null;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
