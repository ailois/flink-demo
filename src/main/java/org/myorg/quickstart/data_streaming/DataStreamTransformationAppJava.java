package org.myorg.quickstart.data_streaming;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class DataStreamTransformationAppJava {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        filterFunction(env);
//        unionFunction(env);
        splitSelectFunction(env);
        env.execute("DataStreamTransformationAppJava");
    }

    private static void splitSelectFunction(StreamExecutionEnvironment env) {
        DataStreamSource<Long> data = env.addSource(new JavaCustomNonParallelSourceFunction());
        SplitStream<Long> splits = data.split(new OutputSelector<Long>() {
            @Override
            public Iterable<String> select(Long aLong) {
                List<String> list = new ArrayList<>();
                if (aLong % 2 == 0) {
                    list.add("even");
                } else {
                    list.add("odd");
                }
                return list;
            }
        });
//        splits.select("odd").print().setParallelism(1);
        splits.select("even").print().setParallelism(1);
    }

    private static void unionFunction(StreamExecutionEnvironment env) {
        DataStreamSource<Long> data1 = env.addSource(new JavaCustomNonParallelSourceFunction());
        DataStreamSource<Long> data2 = env.addSource(new JavaCustomNonParallelSourceFunction());
        data1.union(data2).print().setParallelism(1);
    }

    private static void filterFunction(StreamExecutionEnvironment env) {
        DataStreamSource<Long> data = env.addSource(new JavaCustomNonParallelSourceFunction());
        data.map((MapFunction<Long, Long>) value -> {
            System.out.println("reviced: " + value);
            return value;
        }).filter((FilterFunction<Long>) value -> value % 2 == 0).print().setParallelism(1);
    }
}
