package scalabook.c29

// ========================== Food ==========================
abstract class Food(val name: String) {
  override def toString = name
}
object Apple extends Food("Apple")
object Orange extends Food("Orange")
object Cream extends Food("Cream")
object Sugar extends Food("Sugar")

// ========================== Recipe ==========================
class Recipe(
  val name: String,
  val ingredients: List[Food],
  val instructions: String
) {
  override def toString: String = name
}

object FruitSalad extends Recipe(
  "fruit salad",
  List(Apple, Orange, Cream, Sugar),
  "Stir it all together."
)

// ========================== Database ==========================
abstract class Database {

  case class FoodCategory(name: String, foods: List[Food])

  def allFoods: List[Food]
  def allRecipes: List[Recipe]
  def allCategories: List[FoodCategory]

  def foodNamed(name: String): Option[Food] =
    allFoods.find( _.name == name )
}

object SimpleDatabase extends Database {

  val allFoods = List(Apple, Orange, Cream, Sugar)
  val allRecipes = List(FruitSalad)
  val allCategories = List(
    FoodCategory("fruits", List(Apple, Orange)),
    FoodCategory("misc", List(Cream, Sugar))
  )
}

object StudentDatabase extends Database {
  object FrozenFood extends Food("FrozenFood")
  object HeatItUp extends Recipe(
    "heat it up", List(FrozenFood), "Microwave the 'food' for 10 minutes."
  )

  val allFoods = List(FrozenFood)
  val allRecipes = List(HeatItUp)
  val allCategories = List(
    FoodCategory("edible", List(FrozenFood))
  )
}
// ========================== Browser ==========================
abstract class Browser {
  val db: Database

  def recipesUsing(food: Food): List[Recipe] =
    db.allRecipes.filter( r => r.ingredients.contains(food) )

  def displayCategory(cat: db.FoodCategory) = {
    println( cat )
  }
}
object SimpleBrowser extends Browser {
  val db: Database = SimpleDatabase
}

object StudentBrowser extends Browser {
  val db: Database = StudentDatabase
}
/*
*
* */
object RecipeApp {

  def simple_browser() = {
    println( SimpleBrowser.recipesUsing(Apple) )

//    SimpleBrowser.displayCategory( StudentDatabase.allCategories.head )
//    StudentBrowser.displayCategory( db.allCategories.head )
  }
  /*
  *  main application
  * */
  def main(args: Array[String]): Unit = {
    println("c29.RecipeApp starts ...")

    simple_browser()
  }
}
