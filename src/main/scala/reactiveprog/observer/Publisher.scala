package reactiveprog.observer

trait Publisher {
  private var subscribers: Set[Subscriber] = Set()

  def subscribe(sub: Subscriber) =
    subscribers += sub

  def unsubscribe(sub: Subscriber) =
    subscribers -= sub

  def publish(): Unit = {
    subscribers.foreach(_.handler(this))
  }
}