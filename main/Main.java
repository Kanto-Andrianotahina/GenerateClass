package main;

import mapping.Generate;

import java.io.File;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IllegalAccessException{
        String path = "A:\\ETUDE\\Prog\\S5\\PrevisionCoupure\\src\\java\\mapping\\";
        String language = "java";
        Generate g = new Generate();
//        g.generateClass("presencesalle" ,path,language);
        g.generateAllClass(path,language);
//        List<String> tableName = g.getAllTableName();
//        for (int i = 0; i < tableName.size(); i++) {
//            System.out.println(tableName.get(i));
//        }
    }
}

