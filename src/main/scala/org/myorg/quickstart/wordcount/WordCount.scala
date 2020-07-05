package org.myorg.quickstart.wordcount

import org.apache.flink.api.scala.ExecutionEnvironment

object WordCount {

  def main(args: Array[String]): Unit = {

    val input = "F:\\code\\flink-demo\\test.txt"
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.readTextFile(input)
    import org.apache.flink.api.scala._
    text.flatMap(_.toLowerCase.split("\t"))
      .filter(_.nonEmpty)
      .map((_,1))
      .groupBy(0)
      .sum(1)
      .print()

  }

}
