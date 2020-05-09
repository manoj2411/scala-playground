package lecture.advscalap4implicits

import java.util.Date

object JsonSerialization extends App {
  // This is duplicate version of JsonConversion, created as a part of learning it again, possibly more concise

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  trait JsValue { def stringify: String }
  case class JsString(s: String) extends JsValue { override def stringify: String = "\"" + s + "\"" }
  case class JsNum(n: Int) extends JsValue { override def stringify: String = n.toString }
  case class JsDate(d: Date) extends JsValue { override def stringify: String = JsString(d.toString).stringify }
  case class JsList(list: List[JsValue]) extends JsValue {
    override def stringify: String = list.map(_.stringify).mkString("[", ", ", "]")
  }
  case class JsObject(dict: Map[String, JsValue]) extends JsValue {
    override def stringify: String =
      dict.map(p => s"${JsString(p._1).stringify}: ${p._2.stringify}" ).mkString("{", ", ", "}")
  }

  //  val jsonObj = JsObject(Map(
  //    "foo" -> JsString("bar"),
  //    "peanuts" -> JsList(List(JsString("J"), JsString("K"), JsString("L"))),
  //    "age" -> JsNum(42)
  //  ))
  //  println(jsonObj.stringify)

  trait JsonSerializer[T] {
    def toJson(value: T): JsValue
  }
  implicit class JsonEnricher[T](value: T) {
    def toJson(implicit serializer: JsonSerializer[T]) = serializer.toJson(value)
  }
  implicit object UserSerializer extends JsonSerializer[User] {
    override def toJson(user: User): JsValue = JsObject(Map(
      "name" -> JsString(user.name),
      "age" -> JsNum(user.age),
      "email" -> JsString(user.email)
    ))
  }
  implicit object PostSerializer extends JsonSerializer[Post] {
    override def toJson(post: Post): JsValue = JsObject(Map(
      "content" -> JsString(post.content),
      "created_at" -> JsDate(post.createdAt)
    ))
  }
  implicit object FeedSerializer extends JsonSerializer[Feed] {
    override def toJson(feed: Feed): JsValue = JsObject(Map(
      "user" -> feed.user.toJson,
      "posts" -> JsList(feed.posts.map(_.toJson))
    ))
  }

  val bob = User("bob", 22, "bob@fake-email.com")
  //  println(bob.toJson(UserSerializer).stringify)
  val feed = Feed(bob, List(
    Post("abcd", new Date),
    Post("foo ", new Date),
    Post("bar", new Date)
  ))
  println(feed.toJson.stringify)


}
