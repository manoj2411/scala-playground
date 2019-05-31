package playground

object EnumSample extends App {

  def test(name: String) = Topics.withNameWithDefault(name) match {
    case Topics.Bookings =>
      println("Bookings")
    case Topics.Reviews =>
      println("Reviews")
    case Topics.Triggers =>
      println("Triggers")
    case Topics.Unknown => // fallback to old default.
      println("default")
  }

  test("bookings")
  test("reviews")
  test("abcd 123")
}

object Topics extends Enumeration {
  val Bookings, Reviews, Triggers, Unknown = Value
  def withNameWithDefault(name: String): Value =
    values.find(_.toString.toLowerCase == name.toLowerCase()).getOrElse(Unknown)
}