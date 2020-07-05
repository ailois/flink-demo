package org.myorg.quickstart.streaming;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class StreamingJavaWc {

    public static void main(String[] args) throws Exception {
        // step1 : 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // step2 : 读取数据
        DataStreamSource<String> text = env.socketTextStream("localhost", 9999);

        // step3 : transform
        text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) {
                Arrays.stream(value.toLowerCase().split(","))
                        .map(token -> new Tuple2<>(token, 1))
                        .forEach(collector::collect);
            }
        }).keyBy(0).timeWindow(Time.seconds(5)).sum(1).print().setParallelism(1);

        env.execute("streaming job");

    }

}
