import akka.actor.SupervisorStrategy.{Escalate, Restart, Stop}
import akka.actor.{Actor, ActorKilledException, ActorRef, OneForOneStrategy, Props}
import scala.concurrent.duration._

case class DBException extends Throwable
case class ServiceDownException extends Throwable

class Manager extends Actor {
  // this should be a val so that it doesn't get reinstantiated for every failure
  override val supervisorStrategy = OneForOneStrategy() {
    case _: DBException => Restart
    case _: ActorKilledException => Stop
    case _: ServiceDownException => Escalate
  }

  context.actorOf(Props[DBActor], "db")
  context.actorOf(Props[ImportantServiceActor], "service")
}

class Manager2 extends Actor {
  var restarts = Map.empty[ActorRef, Int].withDefaultValue(0)

  override val supervisorStrategy = OneForOneStrategy() {
    case _: DBException =>
      restarts(sender) match {
        case n if n > 10 =>
          restarts -= sender(); Stop

        case n =>
          restarts = restarts.updated(sender(), n + 1); Restart
      }
  }
}

class Manager3 extends Actor {
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.minute) {
    case _: DBException => Restart // will turn into Stop if max retries reached within time range
  }
}

class DBActor extends Actor {
  val db = DB.openConnection(???)

  // calls this before restart
  // on restart, the constructor of the Actor is reexecuted
  override def postStop(): Unit = {
    db.close
  }
}

// an actor which has external state
class Listener(source: ActorRef) extends Actor {
  override def preStart(): Unit = {
    source ! RegisterListener(self)
  }

  // the ActorRef stays valid during a restart, so preRestart we do nothing
  // by overriding this the children will not be restarted
  // but the context will do that recursively
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {}

  override def postRestart(reason: Throwable): Unit = {}

  override def postStop(): Unit = {
    source ! UnregisterListener(self)
  }
}