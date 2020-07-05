package org.myorg.quickstart.wordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class WordCountJava {

    public static void main(String[] args) throws Exception {

        String input = "F:\\code\\flink-demo\\test.txt";

        // step1: 获取运行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // step2: read data
        DataSource<String> text = env.readTextFile(input);

        // step3: transform
        text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) {
                String[] tokens = value.split("\t");
                Arrays.stream(tokens).filter(token -> token.length() > 0)
                        .map(token -> new Tuple2<>(token, 1))
                        .forEach(collector::collect);
            }
        }).groupBy(0).sum(1).print();
    }

}
