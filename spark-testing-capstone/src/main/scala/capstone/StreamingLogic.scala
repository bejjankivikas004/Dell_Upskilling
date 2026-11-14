
package capstone

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Encoders

object StreamingLogic {

  // Provide encoder for String data
  implicit val stringEncoder = Encoders.STRING

  // Transform streaming events into uppercase
  def uppercaseEvents(events: Dataset[String]): Dataset[String] =
    events.map(_.trim.toUpperCase)

}
