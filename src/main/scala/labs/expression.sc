
// https://www.slideshare.net/GiuseppeScopelliti/expression-problem-in-scala
import scala.language.reflectiveCalls
import scalaz._, Scalaz._

object Names {
  def names[T <: {def name: String}](ns: Vector[T]) =
    ns.map(_.name).mkString(" ")
}

implicit class NamesMaker[T <: {def name: String}](ns: Vector[T]) {
  def names = ns.map(_.name).mkString(" ")
}

trait PayrollSystem {
  case class Employee(name: String, id: Long)

  type P <: Payroll

  type Result = Throwable \/ String

  trait Payroll {
    def processEmployees(es: Vector[Employee]): Result
  }

  def processPayroll(p: P): Result
}

trait USPayrollSystem extends PayrollSystem {
  class USPayroll extends super.Payroll {
    def processEmployees(es: Vector[Employee]): Result =
      s"US employees: ${es.names}".right
  }
}

trait ContractorPayrollSystem extends PayrollSystem {
  case class Contractor(name: String)

  trait Payroll extends super.Payroll {
    def processContractors(cs: Vector[Contractor]): Result
  }
}

trait USContractorPayrollSystem extends USPayrollSystem with ContractorPayrollSystem {
  class USPayroll extends super.USPayroll with Payroll {
    def processContractors(cs: Vector[Contractor]): Result =
      s"US contractors ${cs.names}".right
  }
}

object USEmployeeAndContractorPayroll extends USContractorPayrollSystem {
  type P = USPayroll

  def processPayroll(p: USPayroll): Result = {
    for {
      es <- p.processEmployees(Vector(Employee("e1", 1)))
      cs <- p.processContractors(Vector(Contractor("c1"), Contractor("c2")))
    } yield es + "\n" + cs

    /*p
      .processEmployees(Vector(Employee("e1", 1)))
      .left
      .map(_ +
        p.processContractors(
          Vector(Contractor("c1"), Contractor("c2"))))*/
  }
}

USEmployeeAndContractorPayroll.processPayroll(new USEmployeeAndContractorPayroll.USPayroll)


for {
  e1 <- "event 1 ok".right
  e2 <- "event 2 failed!".left[String]
  e3 <- "event 3 failed!".left[String]
} yield e1 |+| e2 |+| e3

for {
  e1 <- "event 1 ok".right
  e2 <- "event 2 failed!".left[String]
  e3 <- "event 3 failed!".left[String]
} yield e1 + e2 + e3