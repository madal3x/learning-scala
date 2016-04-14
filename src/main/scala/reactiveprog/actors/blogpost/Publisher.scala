package reactiveprog.actors.blogpost

import akka.persistence.PersistentActor

object Publisher {
  sealed trait PublisherCommand
  case class PublishPost(text: String, seqNo: Long)

  sealed trait PublisherMessage
  case class PostPublished(seqNo: Long)
}

class Publisher extends PersistentActor{
  import Publisher._

  var expectedSeqNo = 0L

  val persistenceId = "Publisher"

  def receiveCommand = {
    case PublishPost(text, seqNo) =>
      if (seqNo > expectedSeqNo)
        () // ignore, not yet ready for this
      else if (seqNo < expectedSeqNo)
        sender() ! PostPublished(seqNo)
      else {
        persist(PostPublished(seqNo)) { e =>
          sender() ! e
          expectedSeqNo += 1
        }
      }
  }

  def receiveRecover = {
    case PostPublished(seqNo) =>
      expectedSeqNo = seqNo + 1
  }
}