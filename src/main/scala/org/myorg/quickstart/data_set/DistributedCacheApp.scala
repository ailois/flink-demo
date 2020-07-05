package org.myorg.quickstart.data_set

import org.apache.commons.io.FileUtils
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

object DistributedCacheApp {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment
    val filePath = "F:\\code\\flink-demo\\test.txt"

    // step1 : 注册一个本地/HDFS文件
    env.registerCachedFile(filePath, "pk-scala-dc")

    import org.apache.flink.api.scala._
    val data = env.fromElements("hadoop","spark","flink","pyspark","storm")

    data.map(new RichMapFunction[String,String]() {

      // step2: 在open方法中获取分布式的缓存
      override def open(parameters: Configuration): Unit = {
        val dcFile = getRuntimeContext.getDistributedCache.getFile("pk-scala-dc")
        val lines = FileUtils.readLines(dcFile)

        /**
          * 此时会出现一个异常: Java集合和scala集合不兼容的问题
          */
        import scala.collection.JavaConverters._
        for(ele <- lines.asScala) {
          println(ele)
        }
      }
      override def map(in: String): String = {
        in
      }
    }).print()

  }

}
