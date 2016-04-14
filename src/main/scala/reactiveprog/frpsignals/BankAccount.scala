package reactiveprog.frpsignals

class BankAccount {
  val balance = Var(0)

  def withdraw(amount: Int): Unit ={
    require(0 < amount)
    require(amount <= balance())

    val b = balance()

    balance() = b - amount
  }

  def deposit(amount: Int): Unit ={
    require(0 < amount)

    val b = balance()

    balance() = b + amount
  }
}