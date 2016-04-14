import reactiveprog.frpsignals._

object accounts {
  def consolidated(accts: List[BankAccount]): Signal[Int] =
    Signal(accts.map(_.balance()).sum)
  val a, b = new BankAccount
  val c = consolidated(List(a, b))
  c()
  a deposit 20
  c()
  b deposit 30
  c()
  val xchange = Signal(246.00)
  val inDollar = Signal(c() * xchange())
  inDollar()
  b withdraw 10
  inDollar()
}