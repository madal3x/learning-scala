package reactiveprog.actors.distributed

import akka.actor.{Actor, Props, ReceiveTimeout}
import akka.cluster.{Cluster, ClusterEvent}
import reactiveprog.actors.urlcrawl.Receptionist

import scala.concurrent.duration._

class ClusterMain extends Actor {
  val cluster = Cluster(context.system)
  cluster.subscribe(self, classOf[ClusterEvent.MemberUp])
  cluster.subscribe(self, classOf[ClusterEvent.MemberRemoved])
  cluster.join(cluster.selfAddress)

  val receptionist = context.actorOf(Props[ClusterReceptionist], "cluster-receptionist")
  context.watch(receptionist)

  def getLater(d: FiniteDuration, url: String): Unit = {
    import context.dispatcher
    context.system.scheduler.scheduleOnce(d, receptionist, Receptionist.Get(url))
  }

  getLater(Duration.Zero, "http://www.google.com")

  def receive = {
    case ClusterEvent.MemberUp(member) =>
      if (member.address != cluster.selfAddress) {
        // someone joined
        getLater(1 seconds, "http://www.google.com")
        getLater(2 seconds, "http://www.google.com/1")
        getLater(2 seconds, "http://www.google.com/2")
        getLater(3 seconds, "http://www.google.com/3")
        getLater(4 seconds, "http://www.google.com/4")
        getLater(5 seconds, "http://www.google.com/5")
        context.setReceiveTimeout(3 seconds)
      }

    case Receptionist.Result(url, set) =>
      println(set.toVector.sorted.mkString(s"Results for '$url':\n", "\n", "\n"))

    case Receptionist.Failed(url, reason) =>
      println(s"Failed to fetch '$url': $reason\n")

    case ReceiveTimeout =>
      cluster.leave(cluster.selfAddress)

    case ClusterEvent.MemberRemoved(m, _) =>
      context.stop(self)
  }
}
