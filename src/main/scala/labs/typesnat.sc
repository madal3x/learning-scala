object typesnat {
  sealed trait Nat
  sealed trait _0 extends Nat
  sealed trait Succ[N <: Nat] extends Nat

  type _3 = Succ[Succ[Succ[_0]]]

  class TypeToValue[T, VT](value : VT) { def getValue = value }

  implicit val _0ToInt = new TypeToValue[_0, Int](0)

  implicit def succToInt[P <: Nat](implicit v : TypeToValue[P, Int]) =
    new TypeToValue[Succ[P], Int](1 + v.getValue)

  def toInt[T <: Nat](v : T)(implicit ttv : TypeToValue[T, Int]) : Int =
    ttv.getValue

  val x: _0 = null
  val y: Succ[_0] = null

  toInt(x)

  toInt(y)
  /*
  toInt(v: Succ[_0])(ttv: TypeToValue[Succ[_0], Int]) =
    ttv.getValue

    (succToInt(v: TypeToValue[_0, Int]) = new TypeToValue[Succ[_0], Int](1+v.getValue)).getValue

   */
}