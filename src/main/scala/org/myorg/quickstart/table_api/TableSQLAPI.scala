package org.myorg.quickstart.table_api

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.types.Row

object TableSQLAPI {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = BatchTableEnvironment.create(env)

    import org.apache.flink.api.scala._
    val csv = env.readCsvFile[SalesLog]("F:\\code\\flink-demo\\sales.csv", ignoreFirstLine = true)
    val salesTable = tableEnv.fromDataSet(csv)
    tableEnv.createTemporaryView("sales",salesTable)
    val resultTable = tableEnv.sqlQuery("select customerId,sum(amountPaid) as money from sales group by customerId")
    val result = tableEnv.toDataSet[Row](resultTable)
    result.print()

  }

  case class SalesLog(transactionId:String,customerId:String,itemId:String,amountPaid:Double)
  case class result(customerId:String,money:Double)

}
