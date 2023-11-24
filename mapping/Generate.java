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



    public void  generateClass(String tableName, String typeClass){
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

                stringBuilder.append("\t"+javaType).append(" ").append(columnName).append(";\n");
            }


            result.beforeFirst();
            stringBuilder.append("\n");
            stringBuilder.append("// Getters & Setters\n");
            // Getters & Setters
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


            // Constructors
            result.beforeFirst();
            stringBuilder.append("\n");
            stringBuilder.append("// Constructors\n");

            stringBuilder.append("\tpublic ").append(capitalize(tableName)).append("(){}\n");

            stringBuilder.append("}\n"); /// fermeture class

            PrintWriter writer = new PrintWriter("A:\\ETUDE\\Mr_NAINA\\Java\\S5\\" + capitalize(tableName) + ".java", "UTF-8");
            writer.println(stringBuilder.toString());
            writer.close();

            System.out.println(stringBuilder.toString());
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    public Generate() {
    }
}
