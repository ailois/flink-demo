package org.myorg.quickstart.data_set

import org.apache.flink.api.common.accumulators.LongCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.FileSystem.WriteMode

object CounterApp {

  def main(args: Array[String]): Unit = {
    import org.apache.flink.api.scala._
    val env = ExecutionEnvironment.getExecutionEnvironment
    val data = env.fromElements("hadoop", "spark", "flink", "pyspark", "storm")
    //    data.map(new RichMapFunction[String, Long]() {
    //      var counter = 0L
    //      override def map(in: String): Long = {
    //        counter = counter + 1
    //        println("counter: " + counter)
    //        counter
    //      }
    //    }).setParallelism(3).print()

    val info = data.map(new RichMapFunction[String, String]() {
      // step1: 定义计数器
      val counter = new LongCounter()
      override def open(parameters: Configuration): Unit = {
        // step2: 注册计数器
        getRuntimeContext.addAccumulator("ele-counts-scala", counter)
      }
      override def map(in: String): String = {
        counter.add(1)
        in
      }
    })
    info.writeAsText("F:\\code\\flink-demo\\sink", WriteMode.OVERWRITE).setParallelism(2)

    val jobResult = env.execute("counter app")
    val num = jobResult.getAccumulatorResult[Long]("ele-counts-scala")
    println("num: " + num)

  }

}
