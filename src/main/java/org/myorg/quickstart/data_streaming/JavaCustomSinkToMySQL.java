package org.myorg.quickstart.data_streaming;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JavaCustomSinkToMySQL {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.socketTextStream("localhost", 7777);
        SingleOutputStreamOperator<Student> studentStream = source.map(new MapFunction<String, Student>() {
            @Override
            public Student map(String value) throws Exception {
                String[] splits = value.split(",");
                Student student = new Student();
                student.setId(Integer.parseInt(splits[0]));
                student.setName(splits[1]);
                student.setAge(Integer.parseInt(splits[2]));
                return student;
            }
        });
        studentStream.addSink(new SinkToMySQl());
        env.execute("JavaCustomSinkToMySQL");
    }



}
