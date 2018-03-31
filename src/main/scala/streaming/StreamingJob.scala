package streaming

import utils.SparkUtils._
import configs.Settings

import org.apache.spark.sql.SaveMode
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.Duration

object StreamingJob {

  def main(args: Array[String]): Unit = {
    //setup spark context
    val sc = getSparkContext("Spark Streaming")
    val destination = Settings.StreamingConfigs
    val sqlContext = getSQLContext(sc)
    import sqlContext.implicits._

    val batchDuration = Seconds(4)

    def streaminApp(sc: SparkContext, batchDuration: Duration) = {
      //setup spark streaming context
      val ssc = new StreamingContext(sc, batchDuration)
      val hdfsconf = Settings.StreamingConfigs
      val hdfspath = hdfsconf.hdfsPath
      val lines = ssc.textFileStream(hdfspath)

      val words = lines.flatMap(_.split(" "))
      val wordcounts = words.map(x => (x,1)).reduceByKey(_ + _)
      wordcounts.print()
      ssc
    }

    val ssc = getStreamingContext(streaminApp, sc, batchDuration)
    ssc.start()
    ssc.awaitTermination()

}
