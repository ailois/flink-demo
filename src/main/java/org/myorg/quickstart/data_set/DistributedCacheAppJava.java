package org.myorg.quickstart.data_set;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;

import java.io.File;
import java.util.List;

public class DistributedCacheAppJava {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        String filePath = "F:\\code\\flink-demo\\test.txt";

        // step1 : 注册一个本地/HDFS文件
        env.registerCachedFile(filePath, "pk-scala-dc");
        DataSource<String> data = env.fromElements("hadoop", "spark", "flink", "pyspark", "storm");

        data.map(new RichMapFunction<String, String>() {
            @Override
            public void open(Configuration parameters) throws Exception {
                File file = getRuntimeContext().getDistributedCache().getFile("pk-scala-dc");
                List<String> lines = FileUtils.readLines(file);
                lines.forEach(System.out::println);
            }

            @Override
            public String map(String value) {
                return value;
            }
        }).print();


    }

}
