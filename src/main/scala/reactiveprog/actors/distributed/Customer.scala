package reactiveprog.actors.distributed

import akka.actor.{Actor, ActorRef, Address, Deploy, Props, ReceiveTimeout, SupervisorStrategy, Terminated}
import akka.remote.RemoteScope
import akka.remote.transport.ThrottlerTransportAdapter.Direction.Receive
import reactiveprog.actors.urlcrawl.{Controller, Receptionist}

import scala.concurrent.duration._

// makes sure the url is retrieved, but the work is done at a remote node given in the constructor
class Customer(client: ActorRef, url: String, node: Address) extends Actor {
  // this takes precedence over the implicits declared in the Actor trait
  // because this is an ephemeral actor it's not supposed to be seen from the outside
  // therefore this overrides the self to be its parent to be picked up by sender()
  // so that all messages sent by this actor will appear to be sent by its parent
  implicit val s = context.parent

  override val supervisorStrategy = SupervisorStrategy.stoppingStrategy
  // this is the only change to do the work remotely
  val props = Props[Controller].withDeploy(Deploy(scope = RemoteScope(node)))
  val controller = context.actorOf(props, "controller")
  context.watch(controller)

  context.setReceiveTimeout(5 seconds)
  controller ! Controller.Check(url, 2)

  def receive = ({
    case ReceiveTimeout =>
      context.unwatch(controller)
      client ! Receptionist.Failed(url, "controller timed out")

    case Terminated(_) =>
      client ! Receptionist.Failed(url, "controller died")

    case Controller.Result(links) =>
      context.unwatch(controller)
      client ! Receptionist.Result(url, links)
  }: Receive) andThen (_ => context.stop(self))
}