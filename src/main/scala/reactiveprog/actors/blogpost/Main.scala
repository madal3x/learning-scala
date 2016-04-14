package reactiveprog.actors.blogpost

import akka.actor.{Actor, Props}

class Main extends Actor {
  val publisher = context.actorOf(Props[Publisher])
  val userProcessor = context.actorOf(Props(new UserProcessor(publisher.path)))

  userProcessor ! UserProcessor.NewPost("blabla", 123)
  userProcessor ! UserProcessor.NewPost("blabla2", 124)
  userProcessor ! UserProcessor.NewPost("blabla3", 125)

  def receive = {
    case m: Any =>
      println(m)
  }

  context.stop(self)
}