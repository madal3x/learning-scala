package reactiveprog.observer

trait Subscriber {
  def handler(pub: Publisher)
}