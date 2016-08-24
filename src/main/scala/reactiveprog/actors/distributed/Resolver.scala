package reactiveprog.actors.distributed

import akka.actor.{Actor, ActorIdentity, ActorPath, ActorRef, Identify}


case class Resolve(path: ActorPath)
case class Resolved(path: ActorPath, ref: ActorRef)
case class NotResolved(path: ActorPath)

class Resolver extends Actor {
  def receive = {
    case Resolve(path) =>
      context.actorSelection(path) ! Identify((path, sender))

    case ActorIdentity((path: ActorPath, client: ActorRef), Some(ref)) =>
      client ! Resolved(path, ref)

    case ActorIdentity((path: ActorPath, client: ActorRef), None) =>
      client ! NotResolved(path)
  }
}