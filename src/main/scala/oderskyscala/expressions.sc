package expressions

object expressions {
  def show(e: Expr): String = e match {
    case Number(n) => n.toString
    case Sum(l, r) => show(l) + "+" + show(r)
    case Prod(Sum(l1, r1), Sum(l2, r2)) => "(" + show(l1) + "+" + show(l2) + ")" + "*" + "(" + show(r1) + "+" + show(r2) + ")"
    case Prod(Sum(l1, r1), r) => "(" + show(l1) + "+" + show(r1) + ")" + "*" + show(r)
    case Prod(l, Sum(l2, r2)) => show(l) + "*" + "(" + show(l2) + "+" + show(r2) + ")"
    case Prod(l, r) => show(l) + "*" + show(r)
  } //> show: (e: Expr)String

  show(Sum(Prod(Number(2), Number(3)), Number(4))) //> res0: String = 2*3+4
  show(Prod(Sum(Number(2), Number(3)), Number(4))) //> res1: String = (2+3)*4
}

// Expression problem
// new classes => decompose in class
// new methods => decompose in super-type
// or both
trait Expr {
  def eval: Int = this match {
    case Number(n) => n
    case Sum(l, r) => l.eval + r.eval
    case Prod(l, r) => l.eval * r.eval
  }
}

case class Number(n: Int) extends Expr

case class Sum(l: Expr, r: Expr) extends Expr

case class Prod(l: Expr, r: Expr) extends Expr

// OR the alternative non-solutions
/*
trait Expr {
	def isNumber
	def isSum
	..
}

def eval(e: Expr) = {
	if (e.isNumber) ...
}

or

def eval(e: Expr)	= {
	if (e.isInstanceOf[Number])
		e.asInstanceof[Number]...
}
*/