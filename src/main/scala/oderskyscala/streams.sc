package streams

object streams {
  (1000 to 10000) filter (_ % 2 == 0) take 2 //> res0: scala.collection.immutable.IndexedSeq[Int] = Vector(1000, 1002)
  (1000 to 10000).toStream filter (_ % 2 == 0) take 2
  // next of Stream is evaluated on demand (through Stream.cons call-by-name parameter
  //> res1: scala.collection.immutable.Stream[Int] = Stream(1000, ?)

  def streamRange(lo: Int, hi: Int): Stream[Int] =
    if (lo > hi) Stream.empty
    else Stream.cons(lo, streamRange(lo + 1, hi))
  //> streamRange: (lo: Int, hi: Int)Stream[Int]

  def listRange(lo: Int, hi: Int): List[Int] =
    if (lo > hi) List()
    else lo :: listRange(lo + 1, hi) //> listRange: (lo: Int, hi: Int)List[Int]

  val s1 = (1 to 5).toStream
  //> s1  : scala.collection.immutable.Stream[Int] = Stream(1, ?)
  val s2 = (6 to 9).toList
  //> s2  : List[Int] = List(6, 7, 8, 9)
  val s3 = (6 to 9).toStream //> s3  : scala.collection.immutable.Stream[Int] = Stream(6, ?)

  s1 :: s2 //> res2: List[Any] = List(Stream(1, ?), 6, 7, 8, 9)
  s1 ++ s3 //> res3: scala.collection.immutable.Stream[Int] = Stream(1, ?)
  11 :: s2 //> res4: List[Int] = List(11, 6, 7, 8, 9)
  11 #:: s3 //> res5: scala.collection.immutable.Stream[Int] = Stream(11, ?)
}