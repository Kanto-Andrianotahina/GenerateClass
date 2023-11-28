# GenerateClass
I- Info 
    Database: Postgres
    JDK Version: 17 

II-  How to use the function generateClass
 1-  Change the database name in class Connect
 2- Specify the path where you want to put the class file
 3- Specify the table name in your database that you want to make class
 4- Precise the type class you want to create (JAVA or C#)
    for a class Java change the String language by => java
    for C# change by => C#

III- Example:
    1-  Generate class JAVA 
    public static void main(String[] args) throws IllegalAccessException{
        String path = "A:\\ETUDE\\Mr_NAINA\\Java\\S5\\";
        String language = "java";
        Generate g = new Generate();
        g.generateClass("methodarticle" ,path,language);
    }

    2- Generate class C# 
    public static void main(String[] args) throws IllegalAccessException{
        String path = "A:\\ETUDE\\Mr_NAINA\\Java\\S5\\";
        String language = "C#";
        Generate g = new Generate();
        g.generateClass("methodarticle" ,path,language);
    }


    
