
package com.training

import org.apache.spark.sql.SparkSession

object EmployeeAnalysis {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Employee Analysis")
      .master("local[*]")
      .config("spark.driver.host", "127.0.0.1")
      .config("spark.driver.bindAddress", "127.0.0.1")
      .config("spark.blockManager.port", "0")
      .getOrCreate()

    try {
      import spark.implicits._

      val employees = Seq(
        (1, "John", 30000),
        (2, "David", 40000),
        (3, "Smith", 50000)
      ).toDF("id", "name", "salary")

      employees.show()

    } finally {
      spark.stop()
    }
  }
}
