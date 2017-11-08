package com.crokky;

public class WebLogAnalyzer
{
    public static void main(String[] args) {

        if(args.length < 2) {
            System.out.println("Usage: WebLogAnalyzer source destination");
        } else {
            Analyzer mAnalyzer = new Analyzer(args[0],args[1] );
            mAnalyzer.parse();
        }

    }
}
