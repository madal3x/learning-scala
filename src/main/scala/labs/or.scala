package labs

object or {
  type ¬[A] = A => Nothing
  type ∨[T, U] = ¬[¬[T] with ¬[U]]
  type ¬¬[A] = ¬[¬[A]]
  type ||[T, U] = { type L[X] = ¬¬[X] <:< (T ∨ U) }

  def size[T : (Int || String)#L](t : T) = t match {
    case i : Int => i
    case s : String => s.length
  }
}
