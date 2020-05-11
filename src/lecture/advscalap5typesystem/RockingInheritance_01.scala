package lecture.advscalap5typesystem

object RockingInheritance_01 extends App {
  /*  convenience    */
  trait Writable[T] { def write(value: T): Unit  }
  trait Closable { def close(status: String): Unit }
  trait GenericStream[T] { def foreach(f: T => Unit): Unit }

  /*  build a new type in as method param   */
  def processStream[T](stream: GenericStream[T] with Writable[T] with Closable): Unit = {
    stream.foreach(println)
    stream.close("Y")
  }

  /*  Diamond problem -- Last override gets picked     */

  /*    Super Problem - type linearization    */

  trait Cold { def print = println("cold") }
  trait Green extends Cold { override def print = { println("Green"); super.print } }
  trait Blue extends Cold { override def print = { println("Blue") ; super.print } }
  class Red { def print = println("Red") }
  class White extends Red with Green with Blue {
    override def print: Unit = { println("White"); super.print }
  }
  new White print

}
