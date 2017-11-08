package com.crokky;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyzer {
    private String sourceFileName;
    private String destinationFileName;

    Analyzer(String source, String destination) {
        sourceFileName = source;
        destinationFileName = destination;
    }


    public void parse() {
        Map<String, Integer> mapDomains = new HashMap<String, Integer>();
        Map<String, Integer> mapPath = new HashMap<String, Integer>();
        int numDomainsFound = 0;
        try {
            File file = new File(sourceFileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<URL> lUrls = extractUrls(line);
                numDomainsFound += lUrls.size();
                for (URL u : lUrls) {
                    setHitForItem(mapDomains, u.getHost() );
                    setHitForItem(mapPath, u.getPath() );
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        printResults(numDomainsFound, mapDomains, mapPath);

    }
/*
    private void saveResults(int numDomainsFound, Map<String,Integer> mDomains,
                              Map<String,Integer> mPaths) {
        try (FileOutputStream fop = new FileOutputStream(destinationFileName)) {
            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace(); //cannot open dest file
        }




        System.out.println("Total URLs:" + numDomainsFound);
        System.out.println("Total domains:" + mDomains.size());
        System.out.println("Total paths:" + mPaths.size());


        System.out.println("\nTop domains:");
        for(Map.Entry<String, Integer>  x : mDomains.entrySet()) {
            System.out.println(x.getValue() + " " + x.getKey());
        }

        System.out.println("\nTop paths:");
        for(Map.Entry<String, Integer>  x : mPaths.entrySet()) {
            System.out.println(x.getValue() + " " + x.getKey() );
        }

        System.out.println("");
    }
*/

    private void printResults(int numDomainsFound, Map<String,Integer> mDomains,
            Map<String,Integer> mPaths) {
        StringBuilder builder = new StringBuilder();

        System.out.println("Total URLs:" + numDomainsFound);
        System.out.println("Total domains:" + mDomains.size());
        System.out.println("Total paths:" + mPaths.size());


        System.out.println("\nTop domains:");
        for(Map.Entry<String, Integer>  x : mDomains.entrySet()) {
            System.out.println(x.getValue() + " " + x.getKey());
        }

        System.out.println("\nTop paths:");
        for(Map.Entry<String, Integer>  x : mPaths.entrySet()) {
            System.out.println(x.getValue() + " " + x.getKey() );
        }

        System.out.println("");
    }

    private void setHitForItem(Map<String, Integer> map, String item) {
        if(map.get(item) == null){
            map.put(item,1);
        }else{
            map.put(item, map.get(item) + 1);
        }
    }

    /**
     * Returns a list with all links contained in the input
     */
    private static List<URL> extractUrls(String text)
    {
        List<URL> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);
        while (urlMatcher.find())
        {
            try {

                containedUrls.add(new URL(
                                text.substring(urlMatcher.start(0),
                                        urlMatcher.end(0))
                        )
                );
            } catch (MalformedURLException e) {
                System.out.print("no_URL:"); // not a URL
            }
        }
        return containedUrls;
    }


}
