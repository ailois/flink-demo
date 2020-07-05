package org.myorg.quickstart.streaming

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

object StreamingScalaWc {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val text = env.socketTextStream("localhost",9999)

    import org.apache.flink.api.scala._
    text.flatMap(_.toLowerCase.split(","))
      .filter(_.nonEmpty)
      .map((_, 1))
      .keyBy(0)
      .timeWindow(Time.seconds(5))
      .sum(1)
      .setParallelism(1)
      .print()

    env.execute()

  }

}
