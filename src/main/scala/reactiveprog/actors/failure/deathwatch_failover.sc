import akka.actor.{Actor, Props, Terminated}

class Manager extends Actor {
  def prime(): Receive = {
    val db = context.actorOf(Props[DBActor], "db")
    context.watch(db)

    {
      case Terminated(db) =>
        context.become(backup())
    }
  }

  def backup(): Receive = {???}

  def receive = prime()
}