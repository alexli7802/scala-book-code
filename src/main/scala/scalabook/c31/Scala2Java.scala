package scalabook.c31

/* Key notes:
*  - use 'javap' to investigate '*.class', and find how scala code is translated into Java
*    (try this to see how trait/case class/... are compiled into Java !!!)
*  - @deprecated, @volatile, @serializable, @SerialVersionUID(1234L), @transient in Scala
*    will be given equivalent Java annotations to keep consistent semantics
*  - wildcard
*    Iterator<?> ---> Iterator[_],  Iterator<? extends Component> ----> Iterator[_ <: Component]
* */

import java.io._
class Reader(fname: String) {
  private val in = new BufferedReader(new FileReader(fname))

  @throws(classOf[IOException])      // anti-FP: throw Exception is 'side effect'
  def read() = in.read()
}

trait FortuneTeller {
  def tell(name: String): Int
}
object Scala2Java {

  case class Person(name: String, age: Int)      // scala.Int -> java.int

  val numbers = List[Any](32, 54, 10)       // -> java.lang.Integer

  def tellFortunes(name: String)(teller: FortuneTeller): Int =
    teller.tell(name)

  def main(args: Array[String]) = {

    println("Scala2Java testing ... ")

    // a feature from Java 8 been used in Scala !!!
    val myRet = tellFortunes("alex")(name => name.length )

    println( myRet )
  }
}
