package org.myorg.quickstart.data_set

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

object DataSetDataSourceApp {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment
//    fromCollection(env)
//    textFile(env)
    csvFile(env)

  }

  case class MyCaseClass(name:String, age:Int, job:String)

  def csvFile(env: ExecutionEnvironment) : Unit= {
    val filePath = "F:\\code\\flink-demo\\people.csv"
//    env.readCsvFile[(String, Int, String)](filePath, ignoreFirstLine = true).print()
//    env.readCsvFile[MyCaseClass](filePath, ignoreFirstLine = true).print()
    env.readCsvFile[Person](filePath, ignoreFirstLine = true, pojoFields = Array("name", "age", "job")).print()
  }


  def textFile(env: ExecutionEnvironment) : Unit={
    val filePath = "F:\\code\\flink-demo\\test.txt"
    env.readTextFile(filePath).print()
  }

  def fromCollection(env: ExecutionEnvironment): Unit ={
    val data = 1 to 10
    env.fromCollection(data).print()

  }

}
