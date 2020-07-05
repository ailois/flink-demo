package org.myorg.quickstart.data_set;

import org.apache.flink.api.java.ExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class DataSetDataSourceAppJava {

    public static void main(String[] args) throws Exception {

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//        fromCollection(env);
//        textFile(env);
        readCsv(env);
    }

    private static void textFile(ExecutionEnvironment env) throws Exception {
        String filePath = "F:\\code\\flink-demo\\test.txt";
        env.readTextFile(filePath).print();
    }

    private static void fromCollection(ExecutionEnvironment env) throws Exception {
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            data.add(i);
        }
        env.fromCollection(data).print();
    }

    private static void readCsv(ExecutionEnvironment env) throws Exception {
        String filePath = "F:\\code\\flink-demo\\people.csv";
//        env.readCsvFile(filePath)
//                .ignoreFirstLine()
//                .pojoType(Person.class, "name", "age", "job")
//                .print();
//        Tuple3 tuple3 = new Tuple3("name");
        env.readCsvFile(filePath)
                .ignoreFirstLine()
                .types(String.class, Integer.class, String.class).print();
    }

}
