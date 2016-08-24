trait Matrix // Dummy definitions for expository purposes
trait Vector

trait MultDep[A, B, C] {
  def apply(e1: A, e2: B): C
}

implicit object mmm extends MultDep[Matrix, Matrix, Matrix] {
  def apply(m1: Matrix, m2: Matrix): Matrix = error("TODO")
}

implicit object mvv extends MultDep[Matrix, Vector, Vector] {
  def apply(m1: Matrix, v2: Vector): Vector = error("TODO")
}

implicit object mim extends MultDep[Matrix, Int, Matrix] {
  def apply(m1: Matrix, i2: Int): Matrix = error("TODO")
}

implicit object imm extends MultDep[Int, Matrix, Matrix] {
  def apply(i1: Int, m2: Matrix): Matrix = error("TODO")
}

def mult[A, B, C](a: A, b: B)
                 (implicit instance: MultDep[A, B, C]): C =
  instance(a, b)

val r1: Matrix = mult(new Matrix {}, new Matrix{})