
object SampleScala extends App {
  import abc._
  //  1.
  //  val concatString = new Function2[String, String, String] {
  def concatString: (String, String) => String = new Function2[String, String, String] {
    override def apply(str1: String, str2: String) = str1 + str2
  }
  println(concatString("Manoj", " Sehrawat"))

  val c: A = new C
  val d: A = new D
  val a: A = new A

  println("Is instance(a): " + a.isInstanceOf[B])
  println("Is instance(c): " + c.isInstanceOf[B])
  println("Is instance(d): " + d.getClass.getName )
  println("Is instance(b): " + (new B).isInstanceOf[B])
}

package abc {
  class A

  class B extends A

  class C extends B

  class D extends A

}

