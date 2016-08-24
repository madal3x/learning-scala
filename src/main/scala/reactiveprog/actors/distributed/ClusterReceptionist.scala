package reactiveprog.actors.distributed

import akka.actor.{Actor, Address, Props}
import akka.cluster.{Cluster, ClusterEvent}
import akka.cluster.ClusterEvent.{MemberRemoved, MemberUp}
import reactiveprog.actors.urlcrawl.Receptionist

import scala.util.Random

// this will spawn the nodes remotely in the cluster
class ClusterReceptionist extends Actor {
  import Receptionist._

  val cluster = Cluster(context.system)
  cluster.subscribe(self, classOf[MemberUp])
  cluster.subscribe(self, classOf[MemberRemoved])

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  val randomGen = new Random
  def pick[A](coll: IndexedSeq[A]): A = coll(randomGen.nextInt(coll.size))

  def receive = awaitingMembers

  def awaitingMembers: Receive = {
    case current: ClusterEvent.CurrentClusterState =>
      val addresses = current.members.toVector map (_.address)
      val notMe = addresses filter (_ != cluster.selfAddress)
      if (notMe.nonEmpty)
        context.become(active(notMe))

    case MemberUp(member) if member.address != cluster.selfAddress =>
      context.become(active(Vector(member.address)))

    case Get(url) =>
      sender() ! Failed(url, "no nodes available")
  }

  def active(addresses: Vector[Address]): Receive = {
    case MemberUp(member) if member.address != cluster.selfAddress =>
      context.become(active(addresses :+ member.address))

    case MemberRemoved(member, _) =>
      val remaining = addresses filterNot (_ == member.address)
      if (remaining.isEmpty) context.become(awaitingMembers)
      else context.become(active(remaining))

      // if the number of currently running requests < addresses we know about
    case Get(url) if context.children.size < addresses.size =>
      val client = sender()
      val address = pick(addresses)
      // the actor creating runs async later
      context.actorOf(Props(new Customer(client, url, address)))

      // one request running per cluster node, after which we reject
    case Get(url) =>
      sender ! Failed(url, "too many parallel queries")
  }
}