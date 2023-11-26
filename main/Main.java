package main;

import mapping.Generate;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        String path = "A:\\ETUDE\\Mr_NAINA\\Java\\S5\\";
        String language = "C#";
        Generate g = new Generate();
        g.generateClass("amountmvt" ,path,language);
    }
}

