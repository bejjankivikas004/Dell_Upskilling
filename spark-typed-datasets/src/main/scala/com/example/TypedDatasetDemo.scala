package com.example

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.functions._

object TypedDatasetDemo {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("TypedDatasetDemo")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val employees = Seq(
      Employee(1L, "Asha", "Engineering", 90000.0),
      Employee(2L, "Ravi", "Sales", 65000.0),
      Employee(3L, "Meera", "Engineering", 98000.0),
      Employee(4L, "John", "HR", 60000.0)
    )

    // Untyped DataFrame

    val employeeDF: DataFrame = employees.toDF()

    val untypedResult = employeeDF
      .filter(col("salary") >= 70000.0)
      .select(col("name"), col("department"), col("salary"))
      .orderBy(col("salary").desc)

    println("=== Untyped DataFrame result ===")
    untypedResult.show(truncate = false)

    // Typed Dataset

    val employeeDS: Dataset[Employee] = employees.toDS()

    val typedResult: Dataset[Employee] = employeeDS
      .filter(employee => employee.salary >= 70000.0)
      .orderBy($"salary".desc)

    println("=== Typed Dataset result ===")
    typedResult.show(truncate = false)

    // Typed Projection

    val payViewDS: Dataset[EmployeePayView] = employeeDS
      .filter(_.salary >= 70000.0)
      .map(e => EmployeePayView(
        e.name,
        e.department,
        e.salary
      ))

    println("=== Typed projection result ===")
    payViewDS.show(truncate = false)

    // Typed Map Transformation

    val summaries: Dataset[EmployeeSummary] = employeeDS.map {
      employee =>
        EmployeeSummary(
          name = employee.name,
          annualSalary = employee.salary,
          monthlySalary = employee.salary / 12.0
        )
    }

    println("=== Typed map result ===")
    summaries.show(truncate = false)

    spark.stop()
  }
}