package scalabook.basics



object FunctionTest {

  /* [PartialFunction]
  *
  *   - what's the benefits from using 'PartialFunction'
  *   - what're the typical use case ?
  */

  //
  val ageGroup: PartialFunction[Int, String] = {
      case age if age > 0 => {
        val l = age / 10
        s"(${l * 10} ~ ${l * 10 + 9})"
      }
    }

  def main(args: Array[String]): Unit = {

    println( ageGroup(71) )
    println( ageGroup.isDefinedAt(-49) )
  }
}
