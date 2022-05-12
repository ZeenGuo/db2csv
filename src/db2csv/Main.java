package db2csv;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        DB2CsvExporter exporter = new DB2CsvExporter();
//        exporter.export("Orders");
//        exporter.export("Products");
//        for (String s :args){
//            exporter.export(s);
//        }
        ArrayList<String> tables = exporter.getAllTables();
        for (String table : tables) {
            if (!(table.equals("test"))
                    && !(table.contains("_"))) {
                exporter.export(table);
            }
        }
    }
}
