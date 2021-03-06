package scalabook.c33

import scala.util.parsing.combinator._

class Arith extends JavaTokenParsers {

  def expr: Parser[Any] = term~rep("+"~term | "-"~term)

  def term: Parser[Any] = factor~rep("*"~factor | "/"~factor)

  def factor: Parser[Any] = floatingPointNumber | "("~expr~")"
}

object MainTest {

  def main(args: Array[String]): Unit = {
    println( "scalabook.c33 testing ...")
    println("test jenkins integration!!")
  }
}
