abstract class IntSet {
  def contains(x: Int): Boolean
  def incl(x: Int): IntSet
  def union(other: IntSet): IntSet
}

// there should be only one Empty everywhere, so should be singleton
object Empty extends IntSet {
  def incl(x: Int) = new NonEmpty(x, Empty, Empty)
  def contains(x: Int) = false
  def union(other: IntSet): IntSet = other
  override def toString = "."
}

class NonEmpty(v: Int, l: IntSet, r: IntSet) extends IntSet {
  def incl(x: Int) =
    if (x < v) new NonEmpty(v, l incl x, r)
    else if (x > v) new NonEmpty(v, l, r incl x)
    else this

  def contains(x: Int) =
    if (x < v) l contains x
    else if (x > v) r contains x
    else true

  def union(other: IntSet): IntSet = l union r union other incl v

  override def toString = "{" + l + v + r + "}"
}

val t1 = new NonEmpty(3, Empty, Empty) //> t1  : NonEmpty = {.3.}
val t2 = t1 incl 4 //> t2  : IntSet = {.3{.4.}}

t1 union t2 //> res0: IntSet = {.3{.4.}}