// https://slides.com/rodolfohansen/correct-by-construction

package labs

import cats._
import cats.implicits._

object ArrowChoiceBiMap extends App {
  sealed trait Person
  case class Teacher(name: String, students: List[Person]) extends Person
  case class Student(name: String, teacher: Teacher) extends Person
  type Pay = Double
  type Fee = Double

  def findPerson(name: String): Option[Either[Student, Teacher]] = ???
  def names: List[String] = ???

  def payCalc: Teacher => Pay = _.students.foldLeft(0.0){
    case (pay, t:Teacher) => pay + 0.1 * payCalc(t)
    case (pay, s:Student) => pay + 2.5
  }

  def feeCalc: Student => Fee = {
    case Student(_, t) => 1.10 * payCalc(t) / t.students.length
  }

  // F[A, E] +++ F[B, D] : F[Either[A, B], Either[E, D]]
  // F: Function1[_, _]
  // A: Student
  // E: Fee
  // B: Teacher
  // D: Payment
  // Function1[Student, Fee] +++ Function1[Teacher, Payment] : Function1[Either[Student, Teacher], Either[Fee, Payment]]
  val calc: Either[Student, Teacher] => Either[Fee, Pay] =
    feeCalc +++ payCalc

  lazy val feesAndPayments: List[Either[Fee, Pay]] =
    names flatMap findPerson map calc

  lazy val profit: Double = feesAndPayments foldMap {
    case Left(fee) => fee
    case Right(payment) => -payment
  }
}
