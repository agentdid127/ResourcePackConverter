package com.agentdid127.resourcepack.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**

 Retrieved from http://www.javapractices.com/topic/TopicAction.do?Id=257.


 Detect and remove Byte Order Marks (BOMs) from text files saved with a
 Unicode encoding.

 <P>Dev tool only. If you use this tool to remove BOMs, please ensure 
 you have made a backup.

 <P>This class assumes the UTF-8 encoding for the BOM, but 
 is easily changed to handle any encoding. 
 See http://en.wikipedia.org/wiki/Byte_order_mark for more info.
 JDK 5+.
 */
public final class BomDetector {

/*    /** Run the tool against a root directory.
    public static void main(String... args) throws IOException{
        BomDetector bom = new BomDetector(
                "C:\\Temp3\\test\\",
                ".txt", ".jsp", ".jspf", ".tag", ".html",
                ".css", ".xml", ".js", ".sql", ".tld"
        );

        int count = 0;
        for(String file : bom.findBOMs()){
            log(file);
            ++count;
        }
        log("Number of files with BOM:" + count);

    /*
    for(String file : bom.removeBOMs()){
      log("Removed BOM from: " + file);
    }

    }
    */

    public BomDetector(String rootDirectory, String... fileExtensions){
        this.rootDir = new File(rootDirectory);
        this.extensions = Arrays.asList(fileExtensions);
        if(!rootDir.exists() || rootDir.isFile() ){
            throw new RuntimeException("Root directory not valid.");
        }
    }

    /** Find files with BOMs under the given root directory. Return their names. */
    public List<String> findBOMs() throws IOException {
        List<String> result = new ArrayList<String>();
        for(File textFile : findTextFilesBeneath(rootDir)){
            if(startsWithBOM(textFile)){
                result.add(textFile.getCanonicalPath());
            }
        }
        return result;
    }

    /**
     Find and remove BOMs from files under the given root directory.
     Overwrites files.
     Return the names of the affected files.
     */
    public List<String> removeBOMs() throws IOException{
        List<String> result = new ArrayList<String>();
        for(String bomFile : findBOMs()){
            stripBomFrom(bomFile);
            result.add(bomFile);
        }
        return result;
    }

    // PRIVATE
    private File rootDir;
    private List<String> extensions;

    /** Different encodings will have different BOMs. This is for UTF-8. */
    private final int[] BYTE_ORDER_MARK = {239, 187, 191};

    private static void log(Object thing){
        System.out.println(String.valueOf(thing));
    }

    private List<File> findTextFilesBeneath(File startingDir) throws IOException {
        List<File> result = new ArrayList<File>();
        File[] filesAndDirs = startingDir.listFiles();
        List<File> filesDirs = Arrays.asList(filesAndDirs);
        for(File file : filesDirs){
            if (isTextFile(file)){
                result.add(file);
            }
            if( file.isDirectory() ) {
                //recursive call!!
                List<File> deeperList = findTextFilesBeneath(file);
                result.addAll(deeperList);
            }
        }
        return result;
    }

    private boolean isTextFile(File file) throws IOException{
        boolean result = false;
        String fileName = file.getCanonicalPath();
        int finalDot = fileName.lastIndexOf(".");
        if (finalDot > -1){
            String extension = fileName.substring(finalDot);
            result = extensions.contains(extension);
        }
        return result;
    }

    private boolean startsWithBOM(File textFile) throws IOException {
        boolean result = false;
        if(textFile.length() < BYTE_ORDER_MARK.length) return false;
        //open as bytes here, not characters
        int[] firstFewBytes = new int[BYTE_ORDER_MARK.length];
        InputStream input = null;
        try {
            input = new FileInputStream(textFile);
            for(int index = 0; index < BYTE_ORDER_MARK.length; ++index){
                firstFewBytes[index] = input.read(); //read a single byte
            }
            result = Arrays.equals(firstFewBytes, BYTE_ORDER_MARK);
        }
        finally {
            input.close();
        }
        return result;
    }

    private void stripBomFrom(String textFile) throws IOException{
        File bomFile = new File(textFile);
        long initialSize = bomFile.length();
        long truncatedSize = initialSize - BYTE_ORDER_MARK.length;
        byte[] memory = new byte[(int)(truncatedSize)];
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(bomFile));
            input.skip(BYTE_ORDER_MARK.length);
            int totalBytesReadIntoMemory = 0;
            while(totalBytesReadIntoMemory < truncatedSize){
                int bytesRemaining = (int)truncatedSize - totalBytesReadIntoMemory;
                int bytesRead = input.read(memory, totalBytesReadIntoMemory, bytesRemaining);
                if(bytesRead > 0){
                    totalBytesReadIntoMemory = totalBytesReadIntoMemory + bytesRead;
                }
            }
            overwriteWithoutBOM(memory, bomFile);
        }
        finally {
            input.close();
        }
        File after = new File(textFile);
        long finalSize = after.length();
        long changeInSize = initialSize - finalSize;
        if(changeInSize != BYTE_ORDER_MARK.length){
            throw new RuntimeException(
                    "Change in file size: " + changeInSize +
                            " Expected change: " + BYTE_ORDER_MARK.length
            );
        }
    }

    private void overwriteWithoutBOM(
            byte[] bytesWithoutBOM, File textFile
    ) throws IOException{
        OutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(textFile));
            output.write(bytesWithoutBOM);
        }
        finally {
            output.close();
        }
    }
} 