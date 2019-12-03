package labs

object SortFuns extends App {
  val lists = List(
    List(3,2,1),
    List(1,2,3),
    List(1),
    List(),
    List(2,1,3)
  )

  lists.map(l => (mergeSort(l), insertSort(l), normalSort(l))).foreach(println)

  def mergeSort(l: List[Int]): List[Int] = {
    def mergeImp(a: List[Int], b: List[Int]): List[Int] = {
      var ai = 0
      var bi = 0
      var merged: List[Int] = List();
      while (ai < a.length || bi < b.length) {
        if (ai >= a.length || (bi < b.length && a(ai) > b(bi))) {
          merged = merged :+ b(bi)
          bi = bi + 1
        } else {
          merged = merged :+ a(ai)
          ai = ai + 1
        }
      }

      merged
    }

    def mergeRec(as: List[Int], bs: List[Int]): List[Int] = (as, bs) match {
      case (Nil, Nil) =>
        Nil
      case (Nil, bs @ _ :: _) =>
        bs
      case (as @ _ :: _, Nil) =>
        as
      case (a :: _, b :: _) if (a > b) =>
        b :: mergeRec(as, bs.tail)
      case (a :: _, b :: _) if (a <= b) =>
        a :: mergeRec(as.tail, bs)
    }

    val merge = List(mergeRec _, mergeImp _)(new util.Random().nextInt(2))

    l match {
      case _ :: Nil | Nil =>
        l
      case el1 :: el2 :: Nil if el1 <= el2  =>
        l
      case el1 :: el2 :: Nil if el1 > el2  =>
        el2 :: el1 :: Nil
      case _ =>
        val len = l.length
        val mid = len / 2

        merge(mergeSort(l.slice(0, mid)), mergeSort(l.slice(mid, len)))
    }
  }

  def insertSort(l: List[Int]): List[Int] = {
    val len = l.length
    val arr = l.toArray
    if (len == 0 || len == 1) {
      l
    } else {
      /*
      var i = 1;
      while (i < len) {
        var j = i;
        while (j > 0 && arr(j - 1) > arr(j)) {
          val x = arr(j - 1)
          arr(j - 1)  = arr(j)
          arr(j) = x

          j-=1
        }

        i+=1
      }
      */

      for {
        i <- 1 until len
        j <- i until 0 by -1
        if arr(j - 1) > arr(j)
      } {
        val x = arr(j - 1)
        arr(j - 1)  = arr(j)
        arr(j) = x
      }

      arr.toList
    }
  }

  def normalSort(l: List[Int]): List[Int] = {
    val len = l.length
    val arr = l.toArray
    if (len == 0 || len == 1) {
      l
    } else {
      /*
      var i = 0
      while (i < len - 1) {
        var j = i + 1
        while (j < len) {
          if (arr(i) > arr(j)) {
            val x = arr(i)
            arr(i)  = arr(j)
            arr(j) = x
          }

          j+=1
        }

        i+=1
      }
      */

      for {
        i <- 0 until len-1
        j <- i+1 until len
        if arr(i) > arr(j)
      } {
        val x = arr(i)
        arr(i)  = arr(j)
        arr(j) = x
      }

      arr.toList
    }
  }
}
