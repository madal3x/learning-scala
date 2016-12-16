object WriterK {
  type Writer[A] = (A, String)

  def >=>[A, B, C](m1: A => Writer[B],
                   m2: B => Writer[C]): A => Writer[C] = (a: A) => {

    val (b, s1) = m1(a)
    val (c, s2) = m2(b)

    (c, s1 + s2)
  }

  def id[A](a: A): Writer[A] = (a, "")

  def upCase(s: String): Writer[String] = (s.toUpperCase, "upCase ")
  def toWords(s: String): Writer[Seq[String]] = (s.split(" "), "toWords ")

  def process = >=>(upCase, toWords)

  process("a fost o data ca niciodata")
}

object PartialK {
  def >=>[A, B, C](p1: PartialFunction[A, B],
                   p2: PartialFunction[B, C]): PartialFunction[A, C] =
    p1 andThen p2

  def safeRoot: PartialFunction[Double, Double] = {
    case x: Double if x >=0 =>
      Math.sqrt(x)
  }

  def safeReciprocal: PartialFunction[Double, Double] = {
    case x: Double if x != 0 =>
      1 / x
  }

  def safeRootReciprocal = >=>(safeReciprocal, safeRoot)

  safeRootReciprocal(2)
}

object OptionK {
  // sqrt(x)
  def safeRoot(x: Double): Option[Double] =
    if (x >= 0) Some(Math.sqrt(x))
    else None

  // 1/x
  def safeReciprocal(x: Double): Option[Double] =
    if (x != 0) Some(1 / x)
    else None

  def >=>[A, B, C](o1: A => Option[B],
                   o2: B => Option[C]): A => Option[C] = (a: A) => {

    o1(a).flatMap(o2)
  }

  // sqrt(1/x)
  def safeRootReciprocal = >=>(safeReciprocal, safeRoot)

  safeRootReciprocal(2)
}

