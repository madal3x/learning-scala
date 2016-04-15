sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {
  def size[A](t: Tree[A]): Int = t match {
    case Leaf(_) => 1
    case Branch(l, r) => 1 + size(l) + size(r)
  }

  def maximum(t: Tree[Int]): Int = t match {
    case Leaf(x) => x
    case Branch(l, r) => maximum(l).max(maximum(r))
  }

  def depth[T](t: Tree[T]): Int = t match {
    case Leaf(_) => 0
    case Branch(l, r) => 1 + depth(l).max(depth(r))
  }

  def map[A, B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(x) => Leaf(f(x))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
  }

  def fold[A, B](t: Tree[A])(lf: A => B)(bf: (B, B) => B): B = t match {
    case Leaf(x) => lf(x)
    case Branch(l, r) => bf(fold(l)(lf)(bf), fold(r)(lf)(bf))
  }

  //???
  def sizeWithFold[A](t: Tree[A]): Int =
    fold(t)(x => 1)((l, r) => 1 + l + r)

  def maximumWithFold(t: Tree[Int]): Int =
    fold(t)(x => x)((l, r) => l.max(r))

  def depthWithFold[A](t: Tree[A]): Int =
    fold(t)(_ => 0)((l, r) => 1 + l.max(r))

  def mapWithFold[A, B](t: Tree[A])(f: A => B): Tree[B] =
    fold(t)(a => Leaf(f(a)): Tree[B])((l, r) => Branch(l, r))
}

val t = Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4)))
Tree.size(t)
Tree.maximum(t)
Tree.depth(t)
Tree.map(t)(_ * 2)