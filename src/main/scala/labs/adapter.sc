/**
 * When expected type of expression is Log, yet a Logger instance is used,
 * Scala compiler will automatically wrap that instance in the adapter class.
 */

trait Log {
  def warning(message: String)
  def error(message: String)
}

final class Logger {
  def log(level: String, message: String): Unit = {

  }
}

implicit class LoggerToLogAdapter(logger: Logger) extends Log {
  def warning(message: String) {
    logger.log("WARNING", message)
  }

  def error(message: String) {
    logger.log("ERROR", message)
  }
}

val log: Log = new Logger()