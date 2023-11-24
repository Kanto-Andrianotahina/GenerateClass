package main;

import mapping.Generate;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        String path = "A:\\ETUDE\\Mr_NAINA\\Java\\S5\\";
        Generate g = new Generate();
        g.generateClass("amountmvt" ,"class",path);
    }
}

