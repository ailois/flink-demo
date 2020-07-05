package org.myorg.quickstart.data_set

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.ExecutionEnvironment
import org.myorg.quickstart.utils.DBUtils

import scala.collection.mutable.ListBuffer

object DataSetTransformationApp {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment
//    mapFunction(env)
//    filterFunction(env)
//    mapPartitionFunction(env)
//    firstFunction(env)
//    flatMapFunction(env)
//    distinctFunction(env)
//    joinFunction(env)
//    outerJoinFunction(env)
    crossFunction(env)
  }

  def crossFunction(env: ExecutionEnvironment) : Unit = {
    import org.apache.flink.api.scala._
    val info1 = List("曼联", "曼城")
    val info2 = List(3, 1, 0)

    val data1 = env.fromCollection(info1)
    val data2 = env.fromCollection(info2)
    data1.cross(data2).print()
  }

  def outerJoinFunction(env: ExecutionEnvironment) : Unit = {
    import org.apache.flink.api.scala._
    val info1 = ListBuffer[(Int, String)]() // 编号 名字
    info1.append((1, "PK哥"))
    info1.append((2, "J哥"))
    info1.append((3, "小队长"))
    info1.append((4, "猪头呼"))

    val info2 = ListBuffer[(Int, String)]() // 编号 城市
    info2.append((1, "北京"))
    info2.append((2, "上海"))
    info2.append((3, "成都"))
    info2.append((5, "杭州"))

    val data1 = env.fromCollection(info1)
    val data2 = env.fromCollection(info2)

//    data1.leftOuterJoin(data2).where(0).equalTo(0).apply((first, second) => {
//      if (second == null) {
//        (first._1, first._2, "-")
//      } else {
//        (first._1, first._2, second._2)
//      }
//    }).print()

//    data1.rightOuterJoin(data2).where(0).equalTo(0).apply((first, second) => {
//      if (first == null) {
//        (second._1, "-", second._2)
//      } else {
//        (first._1, first._2, second._2)
//      }
//    }).print()

    data1.fullOuterJoin(data2).where(0).equalTo(0).apply((first, second) => {
      if (first == null) {
        (second._1, "-", second._2)
      } else if (second == null) {
        (first._1, first._2, "-")
      } else {
        (first._1, first._2, second._2)
      }
    }).print()

  }

  def joinFunction(env: ExecutionEnvironment) : Unit = {
    import org.apache.flink.api.scala._
    val info1 = ListBuffer[(Int, String)]() // 编号 名字
    info1.append((1, "PK哥"))
    info1.append((2, "J哥"))
    info1.append((3, "小队长"))
    info1.append((4, "猪头呼"))

    val info2 = ListBuffer[(Int, String)]() // 编号 城市
    info2.append((1, "北京"))
    info2.append((2, "上海"))
    info2.append((3, "成都"))
    info2.append((5, "杭州"))

    val data1 = env.fromCollection(info1)
    val data2 = env.fromCollection(info2)

    data1.join(data2).where(0).equalTo(0).apply((first, second) => {
      (first._1, first._2, second._2)
    }).print()

  }

  def distinctFunction(env: ExecutionEnvironment) : Unit = {
    import org.apache.flink.api.scala._
    val info = ListBuffer[String]()
    info.append("hadoop,spark")
    info.append("hadoop,flink")
    info.append("flink,flink")
    val data = env.fromCollection(info)
    data.flatMap(_.split(",")).distinct().print()
  }

  def flatMapFunction(env: ExecutionEnvironment) : Unit = {
    import org.apache.flink.api.scala._
    val info = ListBuffer[String]()
    info.append("hadoop,spark")
    info.append("hadoop,flink")
    info.append("flink,flink")

    val data = env.fromCollection(info)
    data.map(_.split(",")).print()
    data.flatMap(_.split(",")).print()
    data.flatMap(_.split(",")).map((_, 1)).groupBy(0).sum(1).print()
  }

  def firstFunction(env: ExecutionEnvironment) : Unit= {
    import org.apache.flink.api.scala._
    val info = ListBuffer[(Int, String)]()
    info.append((1, "Hadoop"))
    info.append((1, "Spark"))
    info.append((1, "Flink"))
    info.append((2, "Java"))
    info.append((2, "Spring Boot"))
    info.append((3, "Linux"))
    info.append((4, "VUE"))
    val data = env.fromCollection(info)
//    data.first(3).print()
//    data.groupBy(0).first(1).print()
    data.groupBy(0).sortGroup(1, Order.DESCENDING).first(2).print()

  }

  // 假设 DataSource 中有100个元素, 把结果存储到数据库中
  def mapPartitionFunction(env: ExecutionEnvironment) : Unit = {
    import org.apache.flink.api.scala._
    val students = new ListBuffer[String]
    for(i <- 1 to 100) {
      students.append("student: "+ i)
    }
    val data = env.fromCollection(students).setParallelism(2)
//    data.map(x => {
//      // 每一个元素要存储到数据库中, 肯定要先获取到一个connection
//      val connention = DBUtils.getCollention()
//      println(connention + "......")
//
//      //TODO... 保存数据到DB
//      DBUtils.returnCollection(connention)
//    }).print()
    data.mapPartition(x => {
      val connection = DBUtils.getCollention()
      println(connection + "......")
      //TODO... 保存数据到DB
      DBUtils.returnCollection(connection)
      x
    }).print()
  }

  def filterFunction(env: ExecutionEnvironment) : Unit = {
    import org.apache.flink.api.scala._
    val data = env.fromCollection(List(1,2,3,4,5,6,7,8,9,10))
    data.filter(_ > 5).print()
  }

  def mapFunction(env: ExecutionEnvironment) : Unit = {
    import org.apache.flink.api.scala._
    val data = env.fromCollection(List(1,2,3,4,5,6,7,8,9,10))
//    data.map((x: Int) => x + 1).print()
//    data.map(x => x + 1).print()
    data.map(_ + 1).print()
  }



}
