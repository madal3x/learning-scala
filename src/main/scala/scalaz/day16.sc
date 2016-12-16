import scalaz._, Scalaz._

val memoFib: Int => Int = Memo.mutableHashMapMemo {
  case 0 => 0
  case 1 => 1
  case n: Int => memoFib(n-1) + memoFib(n-2)
}
