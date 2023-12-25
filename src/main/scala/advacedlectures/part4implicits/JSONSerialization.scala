package advacedlectures.part4implicits

import java.util.Date

object JSONSerialization extends App {


  /*
  Users, posts, feeds
  Serialize to JSON
   */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
  1 - intermediate data types: Int, String, List, Date
  2 - type classes for conversion to intermediate data types
  3 - serialize to JSON
   */

  sealed trait JSONValue { // intermediate data type
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    def stringify: String =
      "\"" + value + "\""

  }

  final case class JSONNumber(value: Int) extends JSONValue {
    def stringify: String = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    def stringify: String = values.map(_.stringify).mkString("[",",","]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    /*
    {
      name: "Min"
      age: 22
      friends: [ ... ]
      latestPost: {
        content: "scala rocks"
        date: ...
      }
    }
     */
    def stringify: String = values.map {
      case(key, value) => "\"" + key+ "\":" + value.stringify
    }
      .mkString("{",",","}" )

  }

  val data = JSONObject(Map(
    "user" -> JSONString("Min"),
    "posts" -> JSONArray(List(
      JSONString("Scala rocks"),
      JSONNumber(453)
    ))
  ))

  println(data.stringify)

  // type class
  /*
  1 - type class
  2 - type class instances (implicit)
  3 - pim library to use type class instances
   */


  // 2.1
  trait JSONConverter[T] {
    def convert(vale: T): JSONValue
  }

  // 2.3 conversion

  // Ops es equal Enrichment is same
  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  // 2.2
  // existing data types
  implicit object StringConverter extends JSONConverter[String] {
    def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // custom data type

  implicit object UserConverter extends  JSONConverter[User] {
    def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "created:" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    def convert(feed: Feed): JSONValue = JSONObject(Map(
      // posts depends on PostConverter and also user depends on UserConverter -> unnecessary dependencies
//      "user" -> UserConverter.convert(feed.user), //TODO
//      "posts" -> JSONArray(feed.posts.map(PostConverter.convert)) // TODO

      "user" -> feed.user.toJSON, // declare the Ops before the 2.1, you can optimize like this
      "posts" -> JSONArray(feed.posts.map(_.toJSON))
    ))
  }

  // call stringify on result
  val now = new Date(System.currentTimeMillis())
  val min = User("Min", 33, "min253@gmail.com")
  val feed = Feed(min, List(
    Post("hello", now),
    Post("look at this cute puppy", now)
  ))

  println(feed.toJSON.stringify)


}
