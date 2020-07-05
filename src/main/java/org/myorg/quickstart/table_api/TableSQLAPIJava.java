package org.myorg.quickstart.table_api;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.types.Row;

public class TableSQLAPIJava {

    public static void main(String[] args) throws Exception {

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        BatchTableEnvironment tableEnv = BatchTableEnvironment.create(env);
        DataSource<Sales> source = env.readCsvFile("F:\\code\\flink-demo\\sales.csv")
                .ignoreFirstLine()
                .pojoType(Sales.class,"transactionId","customerId","itemId","amountPaid");
        Table sales = tableEnv.fromDataSet(source);
        tableEnv.createTemporaryView("sales", sales);
        Table resultTable = tableEnv.sqlQuery("select customerId,sum(amountPaid) as money from sales group by customerId");
        DataSet<Row> result = tableEnv.toDataSet(resultTable, Row.class);
        result.print();
    }

    public static class Sales{
        private String transactionId;
        private String customerId;
        private String itemId;
        private Double amountPaid;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public Double getAmountPaid() {
            return amountPaid;
        }

        public void setAmountPaid(Double amountPaid) {
            this.amountPaid = amountPaid;
        }

        @Override
        public String toString() {
            return "Sales{" +
                    "transactionId='" + transactionId + '\'' +
                    ", customerId='" + customerId + '\'' +
                    ", itemId='" + itemId + '\'' +
                    ", amountPaid=" + amountPaid +
                    '}';
        }

    }

}
