// @sloshy

package labs

sealed trait MyLanguage[F[_]] {
  def doAThing: F[String]
  def doAnotherThing: F[Boolean]
}

object MyLanguage {
  val commonThing = ???

  val anotherCommonThing = ???

  def apply[F[_]] = new MyLanguage[F] {
    def doAThing = ???
    def doAnotherThing = ???
  }
}
