package reactiveprog.observer

class BankAccount extends Publisher {
  private var balance = 0

  def currentBalance = balance

  def withdraw(amount: Int): Unit = {
    require(0 < amount)
    require(amount <= balance)

    balance -= amount

    publish()
  }

  def deposit(amount: Int): Unit = {
    require(0 < amount)

    balance += amount

    publish()
  }
}
