package exercises

object FunctionalOneExercise extends App {

/*
1. a Fn takes 2 strings and concat, using Function Type
*/

  def fun  = new Function2[String, String, String] {
    override def apply(str1 : String, str2 : String)= str1 + str2
  }

  println(fun("Manoj ", "Sehrawat"))
}
