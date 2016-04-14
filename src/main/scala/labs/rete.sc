package labs

import scala.collection.mutable.ListBuffer

case class Condition(id: Var, attr: Attr, eqTo: Either[Val, Var])
case class Fact(id: Id, attr: Attr, value: Either[Val, Id])
case class Rule(conditions: List[Condition])

sealed trait Expr
case class Id(v: String) extends Expr
case class Var(v: String) extends Expr
case class Attr(v: String) extends Expr
case class Val(v: String) extends Expr

class AlphaNode {
  private val conditions = new scala.collection.mutable.ListBuffer[Condition]()

  def addFact(fact: Fact): Unit = {

  }
}

class Rete {
  private val aphaNetwork = ListBuffer[AlphaNode]()

  def addRule(rule: Rule): Unit = {
    for (cond <- rule.conditions) {

    }
  }

  def addFact(fact: Fact): Unit = {

  }

}
object rete {
  println("a")
}
