// https://www.youtube.com/watch?v=q6JCvdMWtmo&t=71s
// https://github.com/edmundnoble/scalaworld2017

package labs

import cats._
import cats.data.StateT
import cats.free._
import cats.implicits._
import cats.instances.either._
import cats.instances.list._
import cats.syntax.either._
import cats.syntax.functor._
import cats.syntax.traverse._

object ErrorInject extends App {
  object MyEither {
    sealed trait Either[+L, +R]
    case class Right[+R](r: R) extends Either[Nothing, R]
    case class Left[+L](l: L) extends Either[L, Nothing]
  }
  case class EitherK[F[_], G[_], A](value: F[A] Either G[A])

  case class DividedByZero(dividend: Int)
  def divide(dividend: Int, divisor: Int): DividedByZero Either Int =
    if (divisor == 0) Left(DividedByZero(dividend))
    else Right(dividend / divisor)

  case class NoLogarithm(number: Double)
  def log10(number: Double): NoLogarithm Either Double =
    if (number <= 0) Left(NoLogarithm(number))
    else Right(Math.log10(number))

  object ErrorADT {
    sealed trait DividedByZeroOrNoLogarithm
    object DividedByZeroOrNoLogarithm {
      case class DividedByZero(dividend: Int) extends DividedByZeroOrNoLogarithm
      case class NoLogarithm(number: Double) extends DividedByZeroOrNoLogarithm
    }
    def divideAndLog(dividend: Int, divisor: Int): DividedByZeroOrNoLogarithm Either Double = for {
      divideResult <- divide(dividend, divisor).leftMap {
        case DividedByZero(dividend) => DividedByZeroOrNoLogarithm.DividedByZero(dividend)
      }
      logResult <- log10(divideResult).leftMap {
        case NoLogarithm(number) => DividedByZeroOrNoLogarithm.NoLogarithm(number)
      }
    } yield logResult
  }

  object ErrorEither {
    type DividedByZeroOrNoLogarithm = DividedByZero Either NoLogarithm
    def divideAndLog(dividend: Int, divisor: Int): DividedByZeroOrNoLogarithm Either Double = for {
      divideResult <- divide(dividend, divisor).leftMap(Left(_))
      logResult <- log10(divideResult).leftMap(Right(_))
    } yield logResult
    //    L1 Either L2 => (L1 Either L2) Either L3 ===> easy!
    //    L1 Either L3 => (L1 Either L2) Either L3 ===> not so easy!

  }

  object ErrorCompose {
    type DividedByZeroOrNoLogarithm[A] = DividedByZero Either (NoLogarithm Either A)
    def divideAndLog(dividend: Int, divisor: Int): DividedByZeroOrNoLogarithm[Double] = {
      divide(dividend, divisor).right.map(log10(_))
    }
    //    Right(Left(NoLogarithm(-2.0)) ===> division was successful, logarithm wasn’t
    //    Left(DividedByZero(5)) ===> division wasn’t successful
  }

  object SVGList {
    sealed trait SVG
    case class DrawText(text: String, x: Double, y: Double, next: SVG) extends SVG
    case class DrawEllipse(x: Double, y: Double, rx: Double, ry: Double, next: SVG) extends SVG
    case class DrawCircle(x: Double, y: Double, r: Double, next: SVG) extends SVG
    case class DrawRect(x: Double, y: Double, w: Double, h: Double, next: SVG) extends SVG
    case object DrawNothing extends SVG
  }
  object SVGConcrete {
    sealed trait SVG
    case class DrawText(text: String, x: Double, y: Double) extends SVG
    case class DrawEllipse(x: Double, y: Double, rx: Double, ry: Double) extends SVG
    case class DrawCircle(x: Double, y: Double, r: Double) extends SVG
    case class DrawRect(x: Double, y: Double, w: Double, h: Double) extends SVG
    def svgProgram(xZero: Double, yZero: Double): List[SVG] = {
      val radius = 10
      DrawText("Scala World 2017", xZero, yZero) ::
        DrawCircle(xZero - radius, yZero - radius, radius) ::
        Nil
    }

    def printSVG(svg: List[SVG]): String = svg match {
      case DrawText(t, x, y) :: ss => s"Text($t, $x, $y)\n" + printSVG(ss)
      case DrawEllipse(x, y, rx, ry) :: ss => s"Ellipse($x, $y, $rx, $ry)\n" + printSVG(ss)
      case DrawCircle(x, y, r) :: ss => s"Circle($x, $y, $r)\n" + printSVG(ss)
      case DrawRect(x, y, w, h) :: ss => s"Rect($x, $y, $w, $h)\n" + printSVG(ss)
      case Nil => ""
    }
  }

  object RandomAccessConcrete {
    sealed trait RandomAccess[A]
    case class Write(index: Int, data: String) extends RandomAccess[Unit]
    case class Read(index: Int, length: Int) extends RandomAccess[String]
    def dupe(index: Int, length: Int): Free[RandomAccess, Unit] = for {
      data <- Free.liftF(Read(index, length))
      _ <- Free.liftF(Write(index + length, length, data))
    } yield ()

    def interpret: RandomAccess ~> StateT[Either[IndexOutOfBoundsException, ?], String, ?] = Lambda[RandomAccess ~> IO].apply {
      case Write(i, l, d) => StateT[Either[IndexOutOfBoundsException, ?], String, Unit](s => if (s.length < i + l) new IndexOutOfBoundsException.left else s.substring(0, i) + d + s.substring(i + l))
      case Read(i, l) => StateT[Either[IndexOutOfBoundsException, ?], String, String](s => if (s.length < i + l) new IndexOutOfBoundsException.left else s.substring(i, i + l - 1))
    }

  }

  object RandomAccessUnfinished {
    sealed trait RandomAccess
    case class Write(index: Int, data: String) extends RandomAccess
    case class Read[A](index: Int, length: Int, fromData: String => A) extends RandomAccess
  }

  object RandomAccessUnfinished2 {
    sealed trait RandomAccess[A]
    case class Write[A](index: Int, data: String) extends RandomAccess[A]
    case class Read[A](index: Int, length: Int, fromData: String => A) extends RandomAccess[A]
  }

  object RandomAccessFree {
    sealed trait RandomAccess[A]
    case class Write[A](index: Int, data: String, nextInstruction: RandomAccess[A]) extends RandomAccess[A]
    case class Read[A](index: Int, length: Int, doWithData: String => RandomAccess[A]) extends RandomAccess[A]
    case class End[A](value: A) extends RandomAccess[A]
  }

  //case class EitherK[F[_], G[_], A](value: F[A] Either G[A])

  object DivideInject {
    type DividedByZeroOrNoLogarithm = DividedByZero Either NoLogarithm
    trait Inject[E, S] {
      def apply(s: S): E
    }
    def divideAndLog[E](dividend: Int, divisor: Int)
                       (implicit div: Inject[E, DividedByZero], log: Inject[E, NoLogarithm]): E Either Double = for {
      divideResult <- divide(dividend, divisor).leftMap(div(_))
      logResult <- log10(divideResult).leftMap(log(_))
    } yield logResult
  }
  trait InjectK[E[_], S[_]] { def apply[A](s: S[A]): E[A] }

  def produceInt: Int = 1
  def consumeIntConsumer[A](consumer: Int => A): A = consumer(1)

  object DivideCPS {
    def divide[A](dividend: Int, divisor: Int, dividedByZero: Int => A, result: Int => A): A =
      if (divisor == 0) dividedByZero(dividend)
      else result(dividend / divisor)
    def log10[A](number: Double, noLogarithm: Double => A, result: Double => A): A =
      if (number <= 0) noLogarithm(number)
      else result(Math.log10(number))
    def divideAndLog[A](dividend: Int, divisor: Int,
                        dividedByZero: Int => A, noLogarithm: Double => A,
                        result: Double => A): A = {
      divide(dividend, divisor, dividedByZero, log10(_, noLogarithm, result))
    }

    object DivideMonad {
      def divide[F[_]: Monad](dividend: Int, divisor: Int, dividedByZero: Int => F[Int]): F[Int] =
        if (divisor == 0) dividedByZero(dividend)
        else (dividend / divisor).pure[F]

      def log10[F[_]: Monad](number: Double, noLogarithm: Double => F[Double]): F[Double] =
        if (number <= 0) noLogarithm(number)
        else Math.log10(number).pure[F]

      def divideAndLog[F[_]: Monad](dividend: Int, divisor: Int,
                                    dividedByZero: Int => F[Int], noLogarithm: Double => F[Double]): F[Double] = {
        divide(dividend, divisor, dividedByZero).flatMap(log10(_, noLogarithm))
      }
    }

    object ListProp {
      val a = Nil
      val b = Nil
      val c = Nil
      (a ++ b) ++ c == a ++ (b ++ c)
      a ++ Nil == a
      Nil ++ a == a
    }

    object SVGFinal {
      trait SVG[A] {
        def drawText(text: String, x: Double, y: Double): A
        def drawEllipse(x: Double, y: Double, rx: Double, ry: Double): A
        def drawCircle(x: Double, y: Double, r: Double): A
        def drawRect(x: Double, y: Double, w: Double, h: Double): A
      }
      def svgProgram[A: Monoid](svg: SVG[A], xZero: Double, yZero: Double): A = {
        val radius = 10
        svg.drawText("Scala World 2017", xZero, yZero) |+|
        svg.drawCircle(xZero - radius, yZero - radius, radius)
      }
      val svgPrint: SVG[String] = new SVG[String] {
        def drawText(t: String, x: Double, y: Double) = s"Text($t, $x, $y)\n"
        def drawEllipse(x: Double, y: Double, rx: Double, ry: Double) = s"Ellipse($x, $y, $rx, $ry)\n"
        def drawCircle(x: Double, y: Double, r: Double) = s"Circle($x, $y, $r)\n"
        def drawRect(x: Double, y: Double, w: Double, h: Double) = s"Rect($x, $y, $w, $h)\n"
      }
    }

    object RandomAccessFinal {
      trait RandomAccess[F[_]] {
        def write(index: Int, data: String): F[Unit]
        def read(index: Int, length: Int): F[String]
      }

      def dupe[F[_]: Monad](ra: RandomAccess[F], index: Int, length: Int): F[Unit] = for {
        data <- ra.read(index, length)
        _ <- ra.write(index, data)
      } yield ()

      val interpreter = new RandomAccess[StateT[Either[IndexOutOfBoundsException, String], String, Unit]] {
        def write(i: Int, l: Int, data: String) =
          StateT[Either[IndexOutOfBoundsException, String], String, Unit](s =>
            if (s.length < i + l)
              Left(new IndexOutOfBoundsException)
            else
              s.substring(0, i) + data + s.substring(i + l))
        def read(index: Int, length: Int) =
          StateT[Either[IndexOutOfBoundsException, String], String, Unit](s =>
            if (s.length < index + length)
              Left(new IndexOutOfBoundsException)
            else
              s.substring(index, index + length - 1))
      }

    }

  }
}
