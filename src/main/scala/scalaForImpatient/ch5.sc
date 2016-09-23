import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer

class Counter {
  private var value = 0
  def increment() {
    value += 1
  }
  def current = value
}

val counter = new Counter
// brackets for methods with mutation (or side-effect)
counter.increment()
// without for accessor
counter.current

// public accessor .name and mutator .name=..
class Person(var name: String) {

}
class PersonSame {
  var name: String = _
}

val alex = new Person("Alex")
// method name_=
alex.name
alex.name = "Mihai"

val alex2 = new PersonSame
// method name_=
alex2.name = "Alex"

// value object
case class Name(firstName: String, middleName: String, lastName: String) {
  def withFirstName(otherFirstName: String) = this.copy(otherFirstName)
}

val marius = Name("Marius", "Alexandru", "Madan")
val altul = marius.withFirstName("Altul")

class RealPerson(private var _name: String) {
  def name = _name
  def changeName(aName: String): Unit = {
    // not the functional way to assert, as it throws exception
    assert(aName.length > 2, "Greater than 2 chars")

    _name = name
  }
}

val realAlex = new RealPerson("Alex")
realAlex.changeName("But")

// generates setName and getName
class PersonJavaBeanCompliant(@BeanProperty var name: String)

// primary and auxiliary constructors
// (each aux constructor needs to call
// either another aux constructor or primary construct
class Person5 {
  private var name = ""
  private var age = 0

  //aux
  def this(name: String) {
    this()
    this.name = name
  }

  //aux
  def this(name: String, age: Int) {
    this(name)
    this.age = age
  }
}

class Person6(var name: String, var age: Int) {
  println("new person created with name " + name)

  def hi(name: String): Unit = {
    println("hi " + name)
  }
}

/**
  * If there are no parameters after the class name,
  * then the class has a primary constructor with no parameters.
  * That constructor simply executes all statements in the body of the class
  */

new Person5("booboo")
new Person5("leo", 25)

new Person6("booboo", 25).hi("Alex")

class PersonWithPrivateConstructor private(val id: Int)

//inner classes
class Network(val name: String) {

  // outer points to Network.this
  outer =>
  class Member(val name: String) {
    val contacts = new ArrayBuffer[Member]
    println(name + " inside " + outer.name)
  }

  // different Network object => different Member object
  private val members = new ArrayBuffer[Member]

  def join(name: String) = {
    val m = new Member(name)
    members += m
    m
  }
}

val linkedin = new Network("linkedin")
val fb = new Network("fb")

val fred = linkedin.join("Fred")
val wilma = linkedin.join("Wilma")
val barney = fb.join("Barney")

fred.contacts += wilma
//fred.contacts += barney - doesn't work

// to have general members
// move members to companion object
object NetworkTwo {
  class Member(val name: String) {
    val contacts = new ArrayBuffer[Member]
  }
}

class NetworkTwo {
  private val members = new ArrayBuffer[NetworkTwo.Member]
}

// or use a type projection Network#Member meaning a Member of any Network
class NetworkThree {
  class Member(val name: String) {
    val contacts = new ArrayBuffer[NetworkThree#Member]
  }
}
