// blocking introduces dead-lock - can be mitigated by ordering the locked objects
// blocking is bad for CPU
// synchronises communication between sender and receiver
object BankAccount {
  def transfer(from: BankAccount, to: BankAccount, amount: Int): Unit = {
    from.synchronized {
      to.synchronized {
        from.withdraw(amount)
        to.deposit(amount)
      }
    }
  }
}
class BankAccount {
  private var _balance = 0

  def deposit(amount: Int): Int = this.synchronized {
    if (amount > 0) _balance += amount
    _balance
  }

  def withdraw(amount: Int): Int = this.synchronized {
    require(0 < amount)
    require(amount <= _balance)

    _balance -= amount
    _balance
  }
  def balance = _balance
}
val b1 = new BankAccount
val b2 = new BankAccount

b1.deposit(50)
b1.deposit(40)
b1.withdraw(30)
BankAccount.transfer(b1, b2, 50)

b1.balance
b2.balance