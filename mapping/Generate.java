package  mapping;

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
    public  void generateAttributes(ResultSet result, StringBuilder stringBuilder){
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

                stringBuilder.append("\t"+javaType).append(" ").append(columnName).append(";\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void generateGetttersSetters(ResultSet result,StringBuilder stringBuilder){
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

                stringBuilder.append("\tpublic ").append("get"+capitalize(columnName)).append("()").append("{return ").append(columnName).append(";}\n");
                stringBuilder.append("\tpublic void ").append("set"+capitalize(columnName)).append("(").append(javaType).append(" ").append(columnName+")").append("{").append("this.").append(columnName).append("="+columnName+";}\n");
                stringBuilder.append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void generateFile(String path,StringBuilder stringBuilder, String tableName){
        try {
            PrintWriter writer = new PrintWriter(path + capitalize(tableName) + ".java", "UTF-8");
            writer.println(stringBuilder.toString());
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void  generateClass(String tableName, String typeClass, String path) throws IllegalAccessException {
        boolean checking = this.checkTable(tableName);
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

                /// attributes
               this.generateAttributes(result,stringBuilder);

                stringBuilder.append("\n");
                stringBuilder.append("// Getters & Setters\n");

                // Getters & Setters
                this.generateGetttersSetters(result,stringBuilder);


                // Constructors
                stringBuilder.append("\n");
                stringBuilder.append("// Constructors\n");
                stringBuilder.append("\tpublic ").append(capitalize(tableName)).append("(){}\n");

                stringBuilder.append("}\n"); /// fermeture class

                // creation file
                this.generateFile(path,stringBuilder,tableName);

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
