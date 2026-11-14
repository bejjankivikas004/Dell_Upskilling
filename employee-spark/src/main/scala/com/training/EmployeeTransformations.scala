
package com.training

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

object EmployeeTransformations {

  def increaseSalary(employees: DataFrame): DataFrame = {
    employees.withColumn("salary", col("salary") + 5000)
  }

  def removeDuplicates(employees: DataFrame): DataFrame = {
    employees.dropDuplicates()
  }
}
