// https://www.youtube.com/watch?v=jd5e71nFEZM

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import cats._
import cats.data.OptionT
import cats.implicits._

val futureListF = Functor[Future].compose(Functor[List])
val data: Future[List[Int]] = Future(List(1,2,3))

futureListF.map(data)(_ + 1)

case class User(name: String)
case class Address(city: String)

def getUser(name: String): Future[Option[User]] = ???
def getAddress(user: User): Future[Option[Address]] = ???
def getAge(user: User): Future[Int] = ???
def getName(user: User): Option[String] = ???

val m: OptionT[Future, String] = for {
  user <- OptionT(getUser("alex"))
  address <- OptionT(getAddress(user))
} yield address.city

val n: OptionT[Future, String] = for {
  user <- OptionT(getUser("123"): Future[Option[User]])
  age <- OptionT.liftF(getAge(user): Future[Int])
  name <- OptionT.fromOption(getName(user): Option[String])
} yield s"$name $age"

import emm._
import emm.compat.cats._

type E = Future |: Option |: List |: Base

val bam = for {
  f <- Future(3).liftM[E]
  o <- Option(2).liftM[E]
  l <- List(1, 1).liftM[E]
} yield f + o + l

val r = bam.run