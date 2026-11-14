
package org.apache.spark.sql.execution.streaming

import capstone.{SparkTestSession, StreamingLogic}
import org.apache.spark.sql.Encoders
import org.scalatest.funsuite.AnyFunSuite

class StreamingLogicSpec extends AnyFunSuite with SparkTestSession {

  test("uppercaseEvents processes records added in memory batches") {

    implicit val sqlContext = spark.sqlContext
    implicit val encoder = Encoders.STRING

    val input = MemoryStream[String]
    val queryName = "uppercase_events_test"

    val query = StreamingLogic.uppercaseEvents(input.toDS())
      .writeStream
      .format("memory")
      .queryName(queryName)
      .outputMode("append")
      .start()

    try {
      input.addData(" first ", "second")
      query.processAllAvailable()

      val firstBatch = spark.table(queryName)
        .orderBy("value")
        .collect()
        .map(_.getString(0))
        .toSeq

      assert(firstBatch == Seq("FIRST", "SECOND"))

      input.addData("third")
      query.processAllAvailable()

      val allRows = spark.table(queryName).count()
      assert(allRows == 3L)

    } finally {
      query.stop()
    }
  }
}
