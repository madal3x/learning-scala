// Peano numbers
object naturals {
  val n1 = new Succ(new Succ(new Succ(Zero)))     //> n1  : Succ = Succ(Succ(Succ(Zero)))
	val n2 = new Succ(Zero)                   //> n2  : Succ = Succ(Zero)
	
	println(n1 + n2)                          //> Succ(Succ(Succ(Succ(Zero))))
	println(n1 - n2)                          //> Succ(Succ(Zero))
	println(n2 - n1)                          //> java.lang.Error: Result not Nat
                                                  //| 	at Zero$.$minus(naturals.scala:23)
                                                  //| 	at Succ.$minus(naturals.scala:50)
                                                  //| 	at naturals$$anonfun$main$1.apply$mcV$sp(naturals.scala:7)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at naturals$.main(naturals.scala:1)
                                                  //| 	at naturals.main(naturals.scala)
}

abstract class Nat {
  def isZero: Boolean
  def predecessor: Nat
  def successor: Nat
  def + (that: Nat): Nat
  def - (that: Nat): Nat
}

object Zero extends Nat {
	def isZero = true
	def predecessor = throw new Error("Zero.predecessor")
	def successor = new Succ(this)
	def + (that: Nat) = that
	def - (that: Nat) = if (that.isZero) this else throw new Error("Result not Nat")
	override def toString = "Zero"
}


class Succ(n: Nat) extends Nat {
	def isZero = false
	def predecessor = n
	def successor = new Succ(this)
	/*
	def + (that: Nat) = {
		def accum(el: Nat, acc: Nat): Nat =
			if (el.isZero) acc else accum(el.predecessor, new Succ(acc))
		
		accum(that, this)
	}
	def - (that: Nat) = {
		def loop(el: Nat, dec: Nat): Nat =
			if (!el.isZero && dec.isZero) throw new Error("Result not Nat")
			else if (el.isZero) dec
			else loop(el.predecessor, dec.predecessor)
			
		loop(that, this)
	}
	*/
	
	def + (that: Nat) = new Succ(n + that)
	def - (that: Nat) = if (that.isZero) this else n - that.predecessor
	
	override def toString = "Succ(" + n + ")"
}