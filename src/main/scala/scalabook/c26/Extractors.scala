package scalabook.c26

/* pattern matching - constructor pattern: to decompose and analyze data
*   - object instance -> 'case class', or
*   - plain string    -> 'extractor/pattern'
*
* */

// ----------------------- Email Extractor -----------------------
// - 'object Email' is an extractor because of 'unapply(...)'
// - 'object Email' can be used as a constructor function: Email("joe","gmail.com")
object Email extends ((String,String)=>String) {
  // injection method
  def apply(usr: String, dom: String) = usr + "@" + dom

  // extraction method
  def unapply(src: String): Option[(String,String)] = {
    val parts = src.split("@")
    if (parts.length == 2) Some(parts(0), parts(1)) else None
  }
}

// ----------------------- Twice Extractor -----------------------
// an extractor pattern binds only 1 variable
object Twice {
  def unapply(s: String): Option[String] = {
    val (fst, lst) = s.splitAt(s.length / 2)
    if (fst != "" && fst == lst) Some(fst) else None
  }

  def apply(s: String): String = s + s
}

// ----------------------- UpperCase -----------------------
// an extractor pattern binds no variables
object UpperCase {
  def unapply(s: String): Boolean = s.toUpperCase == s
}

object Extractors {

  def userTwiceUpper(s: String) = s match {
    case x @ UpperCase() => println("UpperCase: " + x)
    case Email(usr,dom) => println(usr + ", " + dom)
//    case Email(Twice(x), dom) => println(x + ", " + dom)
//    case Email(Twice(x @ UpperCase()),dom) => println(x + ", " + dom)
    case _ => println("no match!")
  }
  def main(args: Array[String]): Unit = {
    println("========= c26.Extractors starts ===========")
    userTwiceUpper("ABaB")
    userTwiceUpper("ABAB@gmail.com")
  }
}
