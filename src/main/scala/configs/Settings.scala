package configs

import com.typesafe.config.ConfigFactory;

object Settings {
  private val config = ConfigFactory.load();

  object StreamingConfigs {
    private val streamingconfigs = config.getConfig("hdfsstreaming")

    lazy val checkpoint = streamingconfigs.getString("checkpoint")
    lazy val hdfsPath = streamingconfigs.getString("hdfsPath")
  }

}
