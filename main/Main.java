package main;

import mapping.Generate;

import java.io.File;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IllegalAccessException{
        String path = "A:\\ETUDE\\Mr_NAINA\\Java\\S5\\";
        String language = "java";
        Generate g = new Generate();
//        g.generateClass("methodarticle" ,path,language);
        g.generateAllClass(path,language);
//        List<String> tableName = g.getAllTableName();
//        for (int i = 0; i < tableName.size(); i++) {
//            System.out.println(tableName.get(i));
//        }
    }
}

