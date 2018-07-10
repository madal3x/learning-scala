// https://slideslive.com/38908182/functional-interpreters-and-you

package labs

import scala.concurrent.{Await, Future}

object Expr2 extends App {
  sealed trait Expr[A]
  case class Lit[A](value: A) extends Expr[A]
  case class Lt(a: Expr[Int], b: Expr[Int]) extends Expr[Boolean]
  case class And(a: Expr[Boolean], b: Expr[Boolean]) extends Expr[Boolean]

  // this does not compile
  // val program = Lt(And(Lit(1), Lit(2)), And(Lit(3), Lit(4)))

  val program = And(Lt(Lit(1), Lit(2)), Lt(Lit(3), Lit(4)))

  def eval[A](program: Expr[A]): A =
    program match {
      case Lit(v) => v
      case Lt(a, b) => eval(a) < eval(b)
      case And(a, b) => eval(a) && eval(b)
    }

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration.DurationInt

  def evalAsync[A](program: Expr[A]): Future[A] =
    program match {
      case Lit(v) =>
        Future.successful(v)

      case Lt(a, b) =>
        for {
          a <- evalAsync(a)
          b <- evalAsync(b)
        } yield a < b

      case And(a, b) =>
        for {
          a <- evalAsync(a)
          b <- evalAsync(b)
        } yield a && b
    }

  def prettyPrint[A](program: Expr[A]): String =
    program match {
      case Lit(v) =>
        v.toString

      case Lt(a, b) =>
        s"${prettyPrint(a)} < ${prettyPrint(b)}"

      case And(a, b) =>
        s"${prettyPrint(a)} && ${prettyPrint(b)}"
    }

  println(eval(program))
  println(prettyPrint(program))
  println(Await.result(evalAsync(program), 1 second))

  // ordering was built in, let's make it explicit

  case class FlatMap[A, B](a: Expr[A],
                           f: A => Expr[B]) extends Expr[B]
  case class Pure[A](value: A) extends Expr[A]


  case class Lt2(a: Int, b: Int) extends Expr[Boolean]
  case class And2(a: Boolean, b: Boolean) extends Expr[Boolean]

  // by introducing the flatmap with the fn
  // we have the possibility of inserting scala code into our DSL
  // but made it more difficult to analyze/optimize/introspect the code being run

  val program2 =
    FlatMap(Pure(1), (a: Int) =>
      FlatMap(Pure(2), (b: Int) =>
        FlatMap(Lt2(a, b), (x: Boolean) =>
          FlatMap(Pure(3), (c: Int) =>
            FlatMap(Pure(4), (d: Int) =>
              FlatMap(Lt2(c, d), (y: Boolean) =>
                FlatMap(And2(x, y), (z: Boolean) =>
                  Pure(z)
    )))))))

  def eval2[A](program: Expr[A]): A =
    program match {
      case Pure(v) =>
        v

      case Lt2(a, b) =>
        a < b

      case And2(a, b) =>
        a && b

      case FlatMap(a, fn) =>
        eval2(fn(eval2(a)))
    }

  println(eval2(program2))


  sealed trait ExprAlg[A]
  case class Lt3(a: Int, b: Int) extends ExprAlg[Boolean]
  case class And3(a: Boolean, b: Boolean) extends ExprAlg[Boolean]

  sealed trait ExprMonad[A]
  case class Pure2[A](value: A) extends ExprMonad[A]
  case class Suspend2[A](value: ExprAlg[A]) extends ExprMonad[A]
  case class FlatMap2[A, B](a: ExprMonad[A],
                            f: A => ExprMonad[B]) extends ExprMonad[B]


  // a Free of Expr algebra (type constructor F)
  sealed trait FreeMonad[F[_], A]
  case class Pure3[F[_], A](value: A) extends FreeMonad[F, A]
  case class Suspend3[F[_], A](value: F[A]) extends FreeMonad[F, A]
  case class FlatMap3[F[_], A, B](a: FreeMonad[F, A],
                                  f: A => FreeMonad[F, B]) extends FreeMonad[F, B]



  /*
  val program3 =
    FlatMap3(Pure3(1), (a: Int) =>
      FlatMap3(Pure3(2), (b: Int) =>
        FlatMap3(Suspend3(Lt3(a, b)), (x: Boolean) =>
          FlatMap3(Pure3(3), (c: Int) =>
            FlatMap3(Pure3(4), (d: Int) =>
              FlatMap3(Suspend3(Lt3(c, d)), (y: Boolean) =>
                FlatMap3(Suspend3(And3(x, y)), (z: Boolean) =>
                  Pure3(z)
                )))))))
  */


  import scalaz._, Scalaz._

  type Expr3[A] = Free[ExprAlg, A]

  def lit[A](value: A): Expr3[A] =
    Free.pure[ExprAlg, A](value)

  def lt(a: Int, b: Int): Expr3[Boolean] =
    Free.liftF[ExprAlg, Boolean](Lt3(a, b))

  def and(a: Boolean, b: Boolean): Expr3[Boolean] =
    Free.liftF[ExprAlg, Boolean](And3(a, b))

  /*
  def fail(a: Int, b: Int): Expr3[Boolean] =
    Free.liftF(Fail(a, b))
    */

  val program4: Expr3[Boolean] =
    for {
      a <- lit(1)
      b <- lit(2)
      x <- lt(a, b)
      c <- lit(3)
      d <- lit(4)
      y <- lt(c, d)
      z <- and(x, y)
    } yield z

  // or FunctionK[Expr3, Future] in cats
  object evalAsync extends (ExprAlg ~> Future) {
    def apply[A](expr: ExprAlg[A]): Future[A] =
      expr match {
        case Lt3(a, b) =>
          Future.successful(a < b)

        case And3(a, b) =>
          Future.successful(a && b)
      }
  }

  object eval extends (ExprAlg ~> Id) {
    def apply[A](expr: ExprAlg[A]): A =
      expr match {
        case Lt3(a, b) =>
          a < b

        case And3(a, b) =>
          a && b
      }
  }

  println(program4.foldMap(eval))
  println(Await.result(program4.foldMap(evalAsync), 1 second))

  /*
  combining multiple Free's with cats EitherK

  type Alg[A] = EitherK[Alg1, Alg2, A]
  type Expr[A] = Free[Alg, A]

  or with Freestyle removing all the boilerplate (http://frees.io)
   */

  // church encodings, or encoding as abstract methods
  trait ExprDsl {
    def lit_[A](n: A): A
    def lt_(a: Int, b: Int): Boolean
    def and_(a: Boolean, b: Boolean): Boolean
  }

  def program5(dsl: ExprDsl): Boolean = {
    import dsl._

    and_(lt_(lit_(1), lit_(2)), lt_(lit_(3), lit_(4)))
  }

  object Interpreter extends ExprDsl {
    def lit_[A](n: A): A =
      n

    def lt_(a: Int, b: Int): Boolean =
      a < b

    def and_(a: Boolean, b: Boolean): Boolean =
      a && b
  }

  println(program5(Interpreter))

  abstract class ExprDs2l[F[_]: Monad] {
    def lit__[A](n: A): F[A]
    def lt__(a: Int, b: Int): F[Boolean]
    def and__(a: Boolean, b: Boolean): F[Boolean]
  }

  def program6[F[_]: Monad](dsl: ExprDs2l[F]): F[Boolean] = {
    import dsl._

    for {
      a <- lit__(1)
      b <- lit__(2)
      x <- lt__(a, b)
      c <- lit__(3)
      d <- lit__(4)
      y <- lt__(c, d)
      z <- and__(x, y)
    } yield z
  }

  object InterpreterAsync extends ExprDs2l[Future] {
    def lit__[A](n: A): Future[A] =
      Future.successful(n)

    def lt__(a: Int, b: Int): Future[Boolean] =
      Future.successful(a < b)

    def and__(a: Boolean, b: Boolean): Future[Boolean] =
      Future.successful(a && b)
  }

  println(Await.result(program6(InterpreterAsync), 1 second))
}
