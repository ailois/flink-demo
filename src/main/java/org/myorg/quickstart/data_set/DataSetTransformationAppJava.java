package org.myorg.quickstart.data_set;

import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;
import org.myorg.quickstart.utils.DBUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataSetTransformationAppJava {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//        mapFunction(env);
//        filterFunction(env);
//        firstFunction(env);
//        flatMapFunction(env);
//        distinctFunction(env);
//        joinFunction(env);
//        outerJoinFunction(env);
        crossFunction(env);
    }

    private static void crossFunction(ExecutionEnvironment env) throws Exception {
        List<String> info1 = new ArrayList<>();
        info1.add("曼联");
        info1.add("曼城");
        List<String> info2 = IntStream.range(1, 4).boxed().map(String::valueOf).collect(Collectors.toList());
        DataSource<String> data1 = env.fromCollection(info1);
        DataSource<String> data2 = env.fromCollection(info2);
        data1.cross(data2).print();
    }

    private static void joinFunction(ExecutionEnvironment env) throws Exception {
        List<Tuple2<Integer, String>> info1 = new ArrayList<>();
        info1.add(new Tuple2<>(1, "PK哥"));
        info1.add(new Tuple2<>(2, "J哥"));
        info1.add(new Tuple2<>(3, "小队长"));
        info1.add(new Tuple2<>(4, "猪头呼"));

        List<Tuple2<Integer, String>> info2 = new ArrayList<>();
        info2.add(new Tuple2<>(1, "北京"));
        info2.add(new Tuple2<>(2, "上海"));
        info2.add(new Tuple2<>(3, "成都"));
        info2.add(new Tuple2<>(5, "杭州"));

        DataSource<Tuple2<Integer, String>> data1 = env.fromCollection(info1);
        DataSource<Tuple2<Integer, String>> data2 = env.fromCollection(info2);

        data1.join(data2).where(0).equalTo(0).with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer,String,String>>() {
            @Override
            public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
                return new Tuple3<>(first.f0, first.f1, second.f1);
            }
        }).print();

    }

    private static void outerJoinFunction(ExecutionEnvironment env) throws Exception {
        List<Tuple2<Integer, String>> info1 = new ArrayList<>();
        info1.add(new Tuple2<>(1, "PK哥"));
        info1.add(new Tuple2<>(2, "J哥"));
        info1.add(new Tuple2<>(3, "小队长"));
        info1.add(new Tuple2<>(4, "猪头呼"));

        List<Tuple2<Integer, String>> info2 = new ArrayList<>();
        info2.add(new Tuple2<>(1, "北京"));
        info2.add(new Tuple2<>(2, "上海"));
        info2.add(new Tuple2<>(3, "成都"));
        info2.add(new Tuple2<>(5, "杭州"));

        DataSource<Tuple2<Integer, String>> data1 = env.fromCollection(info1);
        DataSource<Tuple2<Integer, String>> data2 = env.fromCollection(info2);

//        data1.leftOuterJoin(data2).where(0).equalTo(0).with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer,String,String>>() {
//            @Override
//            public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
//                if (second == null) {
//                    return new Tuple3<>(first.f0, first.f1, "-");
//                } else {
//                    return new Tuple3<>(first.f0, first.f1, second.f1);
//                }
//            }
//        }).print();

//        data1.rightOuterJoin(data2).where(0).equalTo(0).with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer,String,String>>() {
//            @Override
//            public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
//                if (first == null) {
//                    return new Tuple3<>(second.f0, "-", second.f1);
//                } else {
//                    return new Tuple3<>(first.f0, first.f1, second.f1);
//                }
//            }
//        }).print();

        data1.fullOuterJoin(data2).where(0).equalTo(0).with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer,String,String>>() {
            @Override
            public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
                if (first == null) {
                    return new Tuple3<>(second.f0, "-", second.f1);
                } else if (second == null) {
                    return new Tuple3<>(first.f0, first.f1, "-");
                } else {
                    return new Tuple3<>(first.f0, first.f1, second.f1);
                }
            }
        }).print();

    }

    private static void distinctFunction(ExecutionEnvironment env) throws Exception {
        List<String> info = new ArrayList<>();
        info.add("hadoop,spark");
        info.add("hadoop,flink");
        info.add("flink,flink");
        DataSource<String> data = env.fromCollection(info);
        data.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                Arrays.stream(s.split(",")).forEach(collector::collect);
            }
        }).distinct().print();
    }

    private static void flatMapFunction(ExecutionEnvironment env) throws Exception {
        List<String> info = new ArrayList<>();
        info.add("hadoop,spark");
        info.add("hadoop,flink");
        info.add("flink,flink");
        DataSource<String> data = env.fromCollection(info);
        data.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                Arrays.stream(s.split(",")).forEach(collector::collect);
            }
        }).map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        }).groupBy(0).sum(1).print();
    }

    private static void firstFunction(ExecutionEnvironment env) throws Exception {
        List<Tuple2<Integer, String>> info = new ArrayList<>();
        info.add(new Tuple2<>(1, "Hadoop"));
        info.add(new Tuple2<>(1, "Spark"));
        info.add(new Tuple2<>(1, "Flink"));
        info.add(new Tuple2<>(2, "Java"));
        info.add(new Tuple2<>(2, "Spring Boot"));
        info.add(new Tuple2<>(3, "Linux"));
        info.add(new Tuple2<>(4, "VUE"));
        DataSource<Tuple2<Integer, String>> data = env.fromCollection(info);
        data.first(3).print();
        System.out.println("============");
        data.groupBy(0).first(2).print();
        System.out.println("============");
        data.groupBy(0).sortGroup(1, Order.DESCENDING).first(2).print();
    }

    private static void mapPartitionFunction(ExecutionEnvironment env) throws Exception {
        List<String> list = IntStream.range(1, 101).boxed()
                .map(x -> "student: " + String.valueOf(x))
                .collect(Collectors.toList());
        DataSource<String> data = env.fromCollection(list);
        /*data.map((MapFunction<String, String>) student -> {
            String connection = DBUtils.getCollention();
            System.out.println("connection = [" + connection + "]");
            DBUtils.returnCollection(connection);
            return student;
        }).print();*/
        data.setParallelism(2).mapPartition(new MapPartitionFunction<String, String>() {
            @Override
            public void mapPartition(Iterable<String> iterable, Collector<String> collector) throws Exception {
                String connection = DBUtils.getCollention();
                System.out.println("connection = [" + connection + "]");
                DBUtils.returnCollection(connection);
            }
        }).print();
    }

    private static void filterFunction(ExecutionEnvironment env) throws Exception {
        List<Integer> data = IntStream.range(1, 11).boxed().collect(Collectors.toList());
        env.fromCollection(data).filter((FilterFunction<Integer>) integer -> integer > 5).print();
    }

    private static void mapFunction(ExecutionEnvironment env) throws Exception {
//        List<Integer> data = new ArrayList<>();
//        range(1, 11).forEach(data::add);
        List<Integer> data = IntStream.range(1, 11).boxed().collect(Collectors.toList());
        env.fromCollection(data).map((MapFunction<Integer, Integer>) integer -> integer + 1).print();
    }

}
