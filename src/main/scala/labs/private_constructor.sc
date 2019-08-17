sealed abstract case class Counter(i: Int)
object Counter { def apply(i: Int): Option[Counter] = Some(new Counter(i) {}).filter(_.i > 0) }