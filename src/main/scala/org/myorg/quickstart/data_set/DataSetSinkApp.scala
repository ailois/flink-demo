package org.myorg.quickstart.data_set

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.core.fs.FileSystem.WriteMode

object DataSetSinkApp {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val data = 1 to 10
    val text = env.fromCollection(data)
    val filePath = "F:\\code\\flink-demo\\sink"
    text.writeAsText(filePath, WriteMode.OVERWRITE).setParallelism(2)
    env.execute("sink app scala")
  }

}
