package scalabook.c29

/* Key Notes:
*  - package can divide program into modules, but it doesn't provide abstraction, you
*    cannot inherit from or re-configure a module.
*  - modularity essentials
*    * separation of implementation from APIs
*    * replacing a dependency without re-compiling main application
*    * approach to wrie modules, like DI
*  - common techniques (refer to the 'Recipe' example)
*    * first: [abstract] class + object, to separate 'abstract layer' from 'implementation'
*    * then : use 'trait' to further organize 'abstract class' and 'object'
*  - use 'self type' in trait
*    when 'trait A' is only ever mixed together with 'trait B', we can refer to things within
*    'trait B', like in 'SimpleWays'
*  - wire up different implementation 'modules'
*    * based on static 'configuration' or CLI parameters
*  - nested class will become different types in different sub-classes, like
*    'SimpleDatabase.FoodCategory' != 'StudentDatabase.Foodgory', which they got from the common
*    super class 'Databse'
*  - 'singleton type' is an important technique
*
* */

trait SimpleThings {
  val a: String
  val b: Int
}

trait SimpleWays {
  this: SimpleThings =>

  def way() = println( this.a )
}

object ScalaModules {

}
