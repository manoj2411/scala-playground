package playground

import scala.collection.mutable

object ScalaPlayground extends App {

  val hmap: mutable.HashMap[String, String] = mutable.HashMap(("k1", "v1"), ("foo", "bar"))
  val hmap2 : mutable.HashMap[String, String] = mutable.HashMap("k1" -> "v1", "bar" -> "foo")

  println("Hash: " + hmap("foo"))
  println("Hash2: " + hmap2("bar1"))

}
