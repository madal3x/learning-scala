sealed trait Option[+A] {
  //pm
  def map[B](f: A => B): Option[B] = this match {
    case Some(x) => Some(f(x))
    case None => None
  }
  def flatMap[B](f: A => Option[B]): Option[B] =
    map(f) getOrElse None
  //pm
  def getOrElse[B >: A](default: => B): B = this match {
    case Some(x) => Some(x)
    case None => Some(default)
  }
  def orElse[B >: A](ob: => Option[B]): Option[B] =
    this map Some(_) getOrElse ob

  //def filter(f: A => Boolean): Option[A]
}

case class Some[+A](get: A) extends Option[A]
case object None extends Option[Nothing]