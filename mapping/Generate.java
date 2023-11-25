package  mapping;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.PrintWriter;

public class Generate{
    String tableName;

    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public boolean checkTable(String table){
        boolean response = false;
        try {
            Connect c = new Connect();
            Connection con = c.connectPostgres();
            String sql = "select count (*) from information_schema.tables where table_name = ?";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1,table);
            ResultSet result = state.executeQuery();

            while (result.next()){
                int count = result.getInt("count");
                if (count == 0) {
                    response = false;
                }else {
                    response = true;
                }
            }
        con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
    public  String generateAttributes(ResultSet result, StringBuilder stringBuilder){
        StringBuilder attributes = new StringBuilder();
        try {
            while (result.next()){
                String columnName = result.getString("column_name");
                String dataType = result.getString("data_type");

                // Convert PostgreSQL data type to Java data type
                String javaType;
                if (dataType.startsWith("character varying")) {
                    javaType = "String";
                }
                else if(dataType.equals("integer") || dataType.equals("int")){
                    javaType = "int";
                }
                else if (dataType.equals("double precision")){
                    javaType = "double";
                }
                else if (dataType.equals("date")){
                    javaType = "Date";
                }
                else if (dataType.equals("timestamp without time zone")){
                    javaType = "Timestamp";
                }
                else {
                    javaType = "Object";
                }

                attributes.append("\t"+javaType).append(" ").append(columnName).append(";\n");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return attributes.toString();
    }
    public String generateGetttersSetters(ResultSet result,StringBuilder stringBuilder){
        StringBuilder gettersSetters = new StringBuilder();
        try {
            result.beforeFirst();
            while (result.next()){
                String columnName = result.getString("column_name");
                String dataType = result.getString("data_type");

                // Convert PostgreSQL data type to Java data type
                String javaType;
                if (dataType.startsWith("character varying")) {
                    javaType = "String";
                }
                else if(dataType.equals("serial") || dataType.equals("int")){
                    javaType = "int";
                }
                else if (dataType.equals("double precision")){
                    javaType = "double";
                }
                else if (dataType.equals("date")){
                    javaType = "Date";
                }
                else if (dataType.equals("timestamp")){
                    javaType = "Timestamp";
                }
                else {
                    javaType = "Object";
                }

                gettersSetters.append("\tpublic ").append("get"+capitalize(columnName)).append("()").append("{return ").append(columnName).append(";}\n");
                gettersSetters.append("\tpublic void ").append("set"+capitalize(columnName)).append("(").append(javaType).append(" ").append(columnName+")").append("{").append("this.").append(columnName).append("="+columnName+";}\n");
                gettersSetters.append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return gettersSetters.toString();
    }
    public void generateFile(String path,StringBuilder stringBuilder, String tableName,String typeClass){
        try {
            if (typeClass.equals("java")) {
                PrintWriter writer = new PrintWriter(path + capitalize(tableName) + ".java", "UTF-8");
                writer.println(stringBuilder.toString());
                writer.close();
            } else if (typeClass.equals("C#")) {
                PrintWriter writer = new PrintWriter(path + capitalize(tableName) + ".cs", "UTF-8");
                writer.println(stringBuilder.toString());
                writer.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void  generateClass(String tableName, String typeClass, String path, String classType) throws IllegalAccessException {
        boolean checking = this.checkTable(tableName);
        String template = null;
        if (checking == true) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("public "+typeClass).append(" ").append(capitalize(tableName)).append("{\n");
            try {
                Connect c = new Connect();
                Connection con = c.connectPostgres();
                String sql = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = ?";
                PreparedStatement state = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                state.setString(1,tableName);
                ResultSet result = state.executeQuery();

                /// field declarations
               String fieldDeclarations = this.generateAttributes(result,stringBuilder);

                stringBuilder.append("\n");
                stringBuilder.append("// Getters & Setters\n");

                // getters and setters
                String gettersSetters = this.generateGetttersSetters(result,stringBuilder);

                // Read Template
                if (classType.equals("java")) {
                    template = new String(Files.readAllBytes(Paths.get("A:\\ETUDE\\Mr_NAINA\\Java\\S5\\GenerateClass\\templateJAVA.txt")));
                } else if (classType.equals("C#")) {
                    template = new String(Files.readAllBytes(Paths.get("A:\\ETUDE\\Mr_NAINA\\Java\\S5\\GenerateClass\\templateC#.txt")));
                }

                // Replace Placeholders
                String classContent = template.replace("CLASS_NAME", capitalize(tableName))
                        .replace("GETTERS_SETTERS", gettersSetters)
                        .replace("FIELD_DECLARATIONS", fieldDeclarations);


                stringBuilder.append("}\n"); /// fermeture class

                // create file
                this.generateFile(path,new StringBuilder(classContent),tableName,classType);

                System.out.println(stringBuilder.toString());
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            throw new IllegalAccessException("Table n'existe pas !");
        }

    }


    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Generate() {
    }
}
