package reactiveprog.actors.blogpost

import akka.actor.ActorPath
import akka.persistence.{AtLeastOnceDelivery, PersistentActor}
import reactiveprog.actors.blogpost.Publisher.PostPublished

object UserProcessor {
  sealed trait UserCommand
  case class NewPost(text: String, id: Long)

  sealed trait UserMessage
  case class BlogPosted(id: Long)
  case class BlogNotPosted(id: Long, reason: String)

  sealed trait UserEvent
  case class PostCreated(text: String) extends UserEvent
  case object QuotaReached extends UserEvent
}

//class UserProcessor extends PersistentActor {
class UserProcessor(publisher: ActorPath) extends PersistentActor with AtLeastOnceDelivery {

  import UserProcessor._

  val persistenceId = "UserProcessor"

  var state = State(Vector.empty, false)

  case class State(posts: Vector[String], isDisabled: Boolean) {
    def updateWith(e: UserEvent): State = e match {
      case PostCreated(text) =>
        copy(posts = posts :+ text)
      case QuotaReached =>
        copy(isDisabled = true)
    }
  }

  // called when actor receives a message
  def receiveCommand = {
    case NewPost(text, id) =>
      if (state.isDisabled)
        sender() ! BlogNotPosted(id, "quota reached")
      else {
        persist(PostCreated(text)) {event =>
          // at-least-once delivery
          // the last parameter is a sequenceId which is incremented on every delivery
          deliver(publisher)(Publisher.PublishPost(text, _))
          updateState(event)
          sender() ! BlogPosted(id)
        }
        persist(QuotaReached)(updateState)

        //persistAsync
        /*
        val created = PostCreated(text)
        updateState(created)
        updateState(QuotaReached)
        persistAsync(created)(_ => sender() ! BlogPosted(id))
        persistAsync(QuotaReached)(_ => ())
        */
      }

    case Publisher.PostPublished(seqNo) =>
      confirmDelivery(seqNo)
  }

  // called when actor is recovered
  def receiveRecover = {
    case e@PostCreated(text) =>
      // deliveries are done after recovery is complete

      deliver(publisher)(Publisher.PublishPost(text, _))
      updateState(e)

    case PostPublished(seqNo) =>
      confirmDelivery(seqNo)

    case e: UserEvent =>
      updateState(e)
  }

  // helper method to update actor internal state
  def updateState(e: UserEvent): Unit = {
    state = state.updateWith(e)
  }
}