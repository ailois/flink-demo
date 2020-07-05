package org.myorg.quickstart.data_set;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.core.fs.FileSystem;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataSetSinkAppJava {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        List<Integer> info = IntStream.range(1, 11).boxed().collect(Collectors.toList());
        DataSource<Integer> data = env.fromCollection(info);
        String filePath = "F:\\code\\flink-demo\\sink";
        data.writeAsText(filePath, FileSystem.WriteMode.OVERWRITE).setParallelism(2);
        env.execute("sink app java");
    }

}
