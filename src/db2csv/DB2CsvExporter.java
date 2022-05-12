package db2csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DB2CsvExporter {
    public BufferedWriter fileWriter;

    public String getFileName(String baseName){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateTimeInfo=dateFormat.format(new Date());
        return baseName.concat(String.format("_%s.csv",dateTimeInfo));
    }

    public int writeHeaderLine(ResultSet result) throws SQLException, IOException {
        // write header line containing column names
        ResultSetMetaData metaData = result.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
        String headerLine = "";

        // exclude the first column which is the ID field
        for (int i = 2; i <= numberOfColumns; i++) {
            String columnName = metaData.getColumnName(i);
            headerLine = headerLine.concat(columnName).concat(",");
        }

        fileWriter.write(headerLine.substring(0, headerLine.length() - 1));

        return numberOfColumns;
    }

    public String escapeDoubleQuotes(String value) {
        return value.replaceAll("\"", "\"\"");
    }

    public ArrayList<String> getAllTables(){
        ArrayList<String> tables= new ArrayList<>();
        String jdbcURL="jdbc:mysql://localhost:3306/tus";
        String username="mabel";
        String password="fff301155";
        try (Connection conn=DriverManager.getConnection(jdbcURL,username,password)){
            String sql="SHOW TABLES;";
            Statement stat=conn.createStatement();
            ResultSet result=stat.executeQuery(sql);
//            System.out.println(result);

            while (result.next()){
                System.out.println(result.getString(1));
                tables.add(result.getString(1));
            }

//            do {
//                System.out.println(result.getString(1));
//            }while (result.next());

        }catch (SQLException e){
            System.out.println("Database error:");
            e.printStackTrace();
        }
//        System.out.println(tables);
        return tables;
    }


    public void export(String table){
        String jdbcURL="jdbc:mysql://localhost:3306/tus";
        String username="mabel";
        String password="fff301155";

        String csvFileName=getFileName(table.concat("_Export"));

        try (Connection connection= DriverManager.getConnection(jdbcURL,username,password)){
            String sql="SELECT * FROM ".concat(table);
            Statement statement=connection.createStatement();
            ResultSet result=statement.executeQuery(sql);

            fileWriter=new BufferedWriter(new FileWriter(csvFileName));

            int columnCount=writeHeaderLine(result);

            while (result.next()){
                String line="";
                for (int i=2;i<=columnCount;i++){
                    Object valueObject=result.getObject(i);
                    String valueString = "";

                    if (valueObject != null)
                        valueString = valueObject.toString();

                    if(valueObject instanceof String){
                        valueString="\""+escapeDoubleQuotes(valueString)+"\"";
                    }

                    line=line.concat(valueString);

                    if (i != columnCount) {
                        line = line.concat(",");
                    }
                }
                fileWriter.newLine();
                fileWriter.write(line);
            }
            result.close();
            statement.close();
            connection.close();
            fileWriter.close();
        }catch (SQLException e){
            System.out.println("Database error:");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("File IO error:");
            e.printStackTrace();
        }

    }

}
