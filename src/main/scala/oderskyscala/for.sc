def isPrime(a: Int): Boolean =
  2 until a forall (a % _ != 0)

def primes(n: Int) = {
  (1 until n) flatMap (i =>
    (1 until i) map (j => (i, j))) filter (p =>
      isPrime(p._1 + p._2))
}

def primesWithFor(n: Int) =
  for {
    i <- 1 until n
    j <- 1 until i
    if isPrime(i+j)
  } yield (i, j)

def primesWithForTranslated(n: Int) =
  (1 until n).flatMap(i =>
    (1 until i).withFilter(j => isPrime(i + j))
      .map(j => (i, j)))

def scalarProduct(xs: List[Double], ys: List[Double]) =
  (for ((x, y) <- xs zip ys) yield x * y).sum

primes(10)
primesWithFor(10)
scalarProduct(List(1,2,3), List(4,5,6))


def queens(n: Int): Set[List[Int]] = {
  def placeQueens(k: Int): Set[List[Int]] =
    if (k == 0) Set(List())
    else
      for {
        queens <- placeQueens(k - 1)
        col <- 0 until n
        if isSafe(col, queens)
      } yield col :: queens

  def isSafe(col: Int, qs: List[Int]): Boolean = {
    val row = qs.length
    val queensWithRow = (row - 1 to 0 by -1) zip qs

    /*queensWithRow.forall(p =>
      p._2 != col && (row - p._1 != col - p._2))*/

    queensWithRow forall {
      case (r, c) => c != col && math.abs(col - c) != row - r
    }
  }

  placeQueens(n)
}

queens(4)


case class Book(title: String, authors: List[String])

//val books = List.empty[Book]
// makes sure there are no duplicates
val books = Set.empty[Book]

val bookTitlesWithAuthorNameBird =
  for {
    b <- books
    a <- b.authors
    if a startsWith "Bird"
  } yield b.title

val bookTitlesWithAuthorNameBirdExpanded =
  /*books.flatMap(b =>
    for (a <- b.authors if a startsWith "Bird") yield b.title
  )*/
  /*books.flatMap(b =>
    for (a <- b.authors withFilter (a => a startsWith "Bird")) yield b.title
  )*/

  books.flatMap(b =>
    b.authors withFilter(a => a startsWith "Bird") map
      (y => b.title))

val bookTitlesWithProgramInTitle =
  for {
    b <- books
    if b.title contains "Program"
  } yield b.title

val authorsWithAtLeastTwoBooks =
  for {
    b1 <- books
    b2 <- books
    //if b1 != b2
    // no duplicate authors
    if b1.title < b2.title
    a1 <- b1.authors
    a2 <- b2.authors
    if a1 == a2
  } yield a1