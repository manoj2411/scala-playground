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

}
