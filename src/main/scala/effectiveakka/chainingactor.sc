import akka.actor.Actor

trait ChainingActor extends Actor {
  private var chainedReceives: List[Receive] = List()
  def registerReceive(newReceive: Receive) {
    chainedReceives = newReceive :: chainedReceives
  }
  def receive = chainedReceives.reduce(_ orElse _)
}

trait IntActor extends ChainingActor {
  registerReceive {
    case i: Int => println("Int!")
  }
}

trait StringActor extends ChainingActor {
  registerReceive {
    case s: String => println("String!")
  }
}