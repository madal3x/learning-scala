package reactiveprog.actors.distributed

import akka.actor.Actor
import akka.cluster.{Cluster, ClusterEvent}
import reactiveprog.actors.urlcrawl.WebClient

class ClusterWorker extends Actor {
  val cluster = Cluster(context.system)
  cluster.subscribe(self, classOf[ClusterEvent.MemberRemoved])
  // default port where the first node starts is 2552
  val main = cluster.selfAddress.copy(port = Some(2552))
  cluster.join(main)

  def receive = {
    // when main stops, this also stops
    case ClusterEvent.MemberRemoved(member, _) =>
      if (member.address == main)
        context.stop(self)
  }

  // the webclient is called in the ClusterWorker
  override def postStop(): Unit = {
    WebClient.shutdown()
  }
}
