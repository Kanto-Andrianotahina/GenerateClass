package main;

import mapping.Generate;

import java.io.File;


public class Main {

    public static void main(String[] args) throws IllegalAccessException{
        String path = "A:\\ETUDE\\Mr_NAINA\\Java\\S5\\";
        String language = "java";
        Generate g = new Generate();
        g.generateClass("methodarticle" ,path,language);

    }
}

