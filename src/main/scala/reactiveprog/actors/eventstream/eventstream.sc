import akka.actor.Actor
import akka.event.Logging.LogEvent

class Listener extends Actor {
  context.system.eventStream.subscribe(self, classOf[LogEvent])

  def receive = {
    case e: LogEvent => ???
  }

  override def postStop(): Unit = {
    context.system.eventStream.unsubscribe(self)
  }
}