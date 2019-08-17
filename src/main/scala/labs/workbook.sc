// https://www.youtube.com/watch?v=__zuECMFCRc
// https://github.com/julien-truffaut/fp-api

package labs

import cats.instances.either._
import cats.instances.list._
import cats.syntax.either._
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.{Monad, SemigroupK}
import org.apache.poi.ss.usermodel.{Cell, Workbook}
import org.apache.poi.ss.util.{AreaReference, CellReference}

import scala.annotation.tailrec

object work {
  sealed trait ParserError extends Product with Serializable

  object ParserError {
    private case class InvalidFormat(ref: String, expectedFormat: String, message: String) extends ParserError
    private case class MissingName(name: String) extends ParserError
    private case class MissingCell(ref: String) extends ParserError

    def invalidFormat(ref: String, expectedFormat: String, message: String): ParserError = InvalidFormat(ref, expectedFormat, message)
    def missingName(name: String): ParserError = MissingName(name)
    def missingCell(ref: String): ParserError = MissingCell(ref)
  }

  case class SafeCell(cell: Cell) {
    val reference = s"${cell.getSheet.getSheetName}!${cell.getAddress}"

    def asDouble: Either[ParserError, Double] =
      Either.catchNonFatal(cell.getNumericCellValue).leftMap(e =>
        ParserError.invalidFormat(reference, "Numeric", e.getMessage)
      )

    def asInt: Either[ParserError, Int] =
      asDouble.flatMap(d => Either.fromOption(doubleToInt(d), ParserError.invalidFormat(reference, "Int", s"$d is not an Int")))

    def asString: Either[ParserError, String] =
      Either.catchNonFatal(cell.getStringCellValue).leftMap(e =>
        ParserError.invalidFormat(reference, "String", e.getMessage)
      )

    def asBoolean: Either[ParserError, Boolean] =
      Either.catchNonFatal(cell.getBooleanCellValue).leftMap(e =>
        ParserError.invalidFormat(reference, "Boolean", e.getMessage)
      )

    private def doubleToInt(d: Double): Option[Int] =
      if(d.isValidInt) Some(d.toInt) else None
  }

  /** Typeclass to read data from a workbook */
  trait Parser[A]{ self =>
    def parse(workbook: Workbook): Either[ParserError, A]

    def flatMap[B](f: A => Parser[B]): Parser[B] = new Parser[B] {
      def parse(workbook: Workbook): Either[ParserError, B] =
        self.parse(workbook).flatMap(f(_).parse(workbook))
    }
  }

  object Parser {
    def apply[A](implicit ev: Parser[A]): Parser[A] = ev

    def success[A](value: A): Parser[A] = lift(Right(value))
    def fail[A](error: ParserError): Parser[A] = lift(Left(error))

    def lift[A](res: Either[ParserError, A]): Parser[A] = new Parser[A] {
      def parse(workbook: Workbook): Either[ParserError, A] = res
    }

    def boolean(name: String): Parser[Boolean] =
      single(name).map(_.asBoolean).flatMap(lift)

    def booleanRange(name: String): Parser[List[Boolean]] =
      range(name).map(_.traverse(_.asBoolean)).flatMap(lift)

    def booleanInt(name: String): Parser[Boolean] =
      int(name).flatMap{
        case 0 => success(false)
        case 1 => success(true)
        case x => fail(ParserError.invalidFormat(name, "Boolean (0/1)", x.toString))
      }

    def numeric(name: String): Parser[Double] =
      single(name).map(_.asDouble).flatMap(lift)

    def numericRange(name: String): Parser[List[Double]] =
      range(name).map(_.traverse(_.asDouble)).flatMap(lift)

    def int(name: String): Parser[Int] =
      single(name).map(_.asInt).flatMap(lift)

    def intRange(name: String): Parser[List[Int]] =
      range(name).map(_.traverse(_.asInt)).flatMap(lift)

    def string(name: String): Parser[String] =
      single(name).map(_.asString).flatMap(lift)

    def stringRange(name: String): Parser[List[String]] =
      range(name).map(_.traverse(_.asString)).flatMap(lift)

    def single(name: String): Parser[SafeCell] =
      range(name).flatMap{
        case x :: Nil => success(x)
        case Nil      => fail(ParserError.invalidFormat(name, "single cell", "empty"))
        case _        => fail(ParserError.invalidFormat(name, "single cell", "several cells"))
      }

    def range(name: String): Parser[List[SafeCell]] = new Parser[List[SafeCell]] {
      def parse(workbook: Workbook): Either[ParserError, List[SafeCell]] =
        for {
          area  <- getArea(workbook, name)
          cells <- area.getAllReferencedCells.toList.traverse(getSafeCell(workbook, _))
        } yield cells
    }

    def getArea(workbook: Workbook, name: String): Either[ParserError, AreaReference] =
      Either.catchNonFatal(
        new AreaReference(workbook.getName(name).getRefersToFormula, workbook.getSpreadsheetVersion)
      ).leftMap(_ => ParserError.missingName(name))

    def getSafeCell(workbook: Workbook, cellRef: CellReference): Either[ParserError, SafeCell] =
      Either.catchNonFatal(SafeCell(
        workbook
          .getSheet(cellRef.getSheetName)
          .getRow(cellRef.getRow)
          .getCell(cellRef.getCol)
      )).leftMap(_ => ParserError.missingCell(cellRef.toString))

    implicit val instance: Monad[Parser] with SemigroupK[Parser] = new Monad[Parser] with SemigroupK[Parser] {
      def combineK[A](x: Parser[A], y: Parser[A]): Parser[A] = new Parser[A] {
        def parse(workbook: Workbook): Either[ParserError, A] =
          x.parse(workbook) orElse y.parse(workbook)
      }

      def flatMap[A, B](fa: Parser[A])(f: A => Parser[B]): Parser[B] =
        fa.flatMap(f)

      def tailRecM[A, B](a: A)(f: A => Parser[Either[A, B]]): Parser[B] = new Parser[B] {
        def parse(workbook: Workbook): Either[ParserError, B] = {
          @tailrec
          def loop(thisA: A): Either[ParserError, B] = f(thisA).parse(workbook) match {
            case Left(a1)        => Left(a1)
            case Right(Left(a1)) => loop(a1)
            case Right(Right(b)) => Right(b)
          }
          loop(a)
        }
      }

      def pure[A](x: A): Parser[A] = success(x)
    }
  }

  import Parser._

  case class Production(oil: List[Double], gas: List[Double])

  val production: Parser[Production] =
    numericRange("OilProd").flatMap(oil =>
      numericRange("GasProd").flatMap(gas =>
        success(Production(oil, gas))
      )
    )

  def product[A, B](pa: Parser[A], pb: Parser[B]): Parser[(A, B)] =
    new Parser[(A, B)] {
      def parse(workbook: Workbook): Either[ParserError, (A, B)] =
        for {
          a <- pa.parse(workbook)
          b <- pb.parse(workbook)
        } yield (a, b)
    }

  def map[A, B](pa: Parser[A])(f: A => B): Parser[B] =
    pa.flatMap(a => success(f(a)))

  val production2: Parser[Production] =
    map(
      product(numericRange("OilProd"), numericRange("GasProd"))
    )({ case (oil, gas) => Production(oil, gas) })

  val oil = numericRange("OilProd");
  val gas = numericRange("GasProd");
  val workbook: Workbook
  product(oil, gas).parse(workbook)
}



