// would not work for Array as it needs the type of the elements
// would not work for ordering, you need to know what is the ordering function
trait Functor[F[_], T] {
  def map[U](f: T => U): F[U]
}