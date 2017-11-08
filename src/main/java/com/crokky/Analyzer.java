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

class Analyzer {
    private String sourceFileName;
    private String destinationFileName;

    Analyzer(String source, String destination) {
        sourceFileName = source;
        destinationFileName = destination;
    }


    void parse() {
        Map<String, Integer> mapDomains = new HashMap<>();
        Map<String, Integer> mapPath = new HashMap<>();
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

        System.out.println(
                printResults(numDomainsFound, mapDomains, mapPath)
        );
        saveResults( printResults(numDomainsFound, mapDomains, mapPath) );

    }


    private void saveResults(String results) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(destinationFileName));
            out.write(results);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String printResults(int numDomainsFound, Map<String,Integer> mDomains,
            Map<String,Integer> mPaths) {
        StringBuilder builder = new StringBuilder();

        builder.append("\nTotal URLs:").append(numDomainsFound);
        builder.append("\nTotal domains:").append(mDomains.size());
        builder.append("\nTotal paths:").append(mPaths.size());

        builder.append("\n\nTop domains:");
        for(Map.Entry<String, Integer>  x : mDomains.entrySet()) {
            builder.append("\n").append(x.getValue()).append(" ").append(x.getKey());
        }

        builder.append("\n\nTop paths:");
        for(Map.Entry<String, Integer>  x : mPaths.entrySet()) {
            builder.append("\n").append(x.getValue()).append(" ").append(x.getKey() );
        }

        return builder.toString();
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
