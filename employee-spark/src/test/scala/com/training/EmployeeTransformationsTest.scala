
package com.training

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterAll
import org.apache.spark.sql.SparkSession

class EmployeeTransformationsTest
  extends AnyFunSuite with BeforeAndAfterAll {

  private var spark: SparkSession = _

  override def beforeAll(): Unit = {
    super.beforeAll()

    spark = SparkSession.builder()
      .appName("Employee Test")
      .master("local[*]")
      .config("spark.ui.enabled", "false")
      .config("spark.sql.shuffle.partitions", "2")
      .getOrCreate()
  }

  override def afterAll(): Unit = {
    try {
      if (spark != null) {
        spark.stop()
      }
    } finally {
      super.afterAll()
    }
  }

  test("Increase employee salary") {

    val sparkSession = spark
    import sparkSession.implicits._

    val employees = Seq(
      (1, "John", 30000)
    ).toDF("id", "name", "salary")

    val result =
      EmployeeTransformations.increaseSalary(employees)

    val actualSalary =
      result.collect()(0).getAs[Int]("salary")

    assert(actualSalary == 35000)
  }

  test("Remove duplicate employees") {

    val sparkSession = spark
    import sparkSession.implicits._

    val employees = Seq(
      (1, "John", 30000),
      (1, "John", 30000),
      (2, "David", 40000)
    ).toDF("id", "name", "salary")

    val result =
      EmployeeTransformations.removeDuplicates(employees)

    assert(result.count() == 2)
  }
}
