
object SampleScala extends App {
  //  1.
  //  val concatString = new Function2[String, String, String] {
  def concatString: (String, String) => String = new Function2[String, String, String] {
    override def apply(str1: String, str2: String) = str1 + str2
  }
  //  println(concatString("Manoj", " Sehrawat"))

}


