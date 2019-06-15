package lecture.advscalap4implicits

import java.util.Date

object JSONSerialization extends App {
  /* We have User, Post, Feed objects and we want serialize to JSON */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createAt: Date)
  case class Feed(user: User, posts: List[Post])

  /* Steps to implement json serialization:
      1. have some intermediate data-types, which could be stringified as json from primitive
        data-types, such as (Date, String, Int, List)
      2. create type classes for conversion from case classes to intermediate data-types
      3. serialize these intermediate data-types to json.
  */

  /*  Step 1   */
  trait JSONValue {
    def stringify: String
  }

  case class JSONString(value: String) extends JSONValue {
    def stringify: String = "\"" + value + "\""
  }
  case class JSONInt(value: Int) extends JSONValue {
    def stringify: String = value.toString
  }
  case class JSONArray(values: List[JSONValue]) extends JSONValue {
    def stringify: String = values.map(_.stringify).mkString("[", ", ", "]")
  }
  case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    def stringify: String = values.map {
      case (key, value) => "\"" + key + "\": " + value.stringify
    }.mkString("{", ", ", "}")
  }

  val data = JSONObject(Map(
    "name" -> JSONString("Manoj"),
    "likes" -> JSONInt(24),
    "posts" -> JSONArray(List(
      JSONString("Scala rocks!"),
      JSONInt(42)
    ))
  ))

  println(data.stringify)

  /*  Step 2 - create type class and TC instances */
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  implicit object UserConverter extends JSONConverter[User] {
    def convert(user: User): JSONObject = JSONObject(Map(
      "name" -> JSONString(user.name),
      "email" -> JSONString(user.email),
      "age" -> JSONInt(user.age)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    override def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "createdAt" -> JSONString(post.createAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    def convert(feed: Feed): JSONObject = JSONObject(Map(
      "user" -> feed.user.toJson,
      "posts" -> JSONArray(feed.posts.map(_.toJson))
    ))
  }

  /*  Step 3 - pimp library to use type class instances */
  implicit class JSONEnrichment[T](value: T) {
    def toJson(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  val bob = User("Bob", 24, "bob@gmail.com")
  val feed = Feed(
    bob,
    List(
      Post("Hey world!", new Date),
      Post("I love scala!", new Date)
    )
  )

  println(feed.toJson.stringify)

}
