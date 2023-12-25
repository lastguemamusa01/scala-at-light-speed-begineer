package advacedlectures.part4implicits

import java.util.Date

object TypeClasses extends App {

  /*
  type class is a trait that takes a type and describes what operations can be appllied to that type
   */

  // server side rendering
  // backend developer

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/></div>"
  }

  User("Min", 32, "min253@rockthejvm.com").toHtml
  /*
  2 disadvantages
  1 - for the types we write, dont work for another types, need some convertions
  2 - one implementation out of quite a number ->
   */

  // option 2 - pattern matching

  object HTMLSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(n, a, e) =>
      case _ =>
    }
  }


  /*
  disadvantages of pattern matching
  1 - lost type safety -> the value of the pattern matching can be anything
  2 - need to modify the code every time when there are new data structure
  3 - still one implementation
  not the best design
   */

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/></div>"
  }

  val min = User("Min", 32, "min253@rockthejvm.com")
  println(UserSerializer.serialize(min))

  /*
  advantages
  1 - we can define serializers for other types - more generic
   */

  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString()}</div>"
  }

  /*
  advantages
  2 - we can define multiple serializers
   */

  object PartialUserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} </div>"
  }

  // HTMLSerializer is called a type class
  /*
  type class specifies a set of operations -> serialize that can be applied to a given type
   */

  // part2

  // implicit type class instances by implicit values and parameters

  // companion object of type class HTMLSerializer
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }

  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(min))

  // access to the entire type class interface
  println(HTMLSerializer[User].serialize(min))

  // part 3
  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(min.toHTML) // re written by the compiler like -> println(new HTMLEnrichment[User](min).toHTML(UserSerialize))

  /*
  - extends to new type
  - choose implementation
  - super expressive!
   */

  println(2.toHTML)
  println(min.toHTML(PartialUserSerializer))

  /*
   - type class itself -> HTMLSerializer[T] { .. }
   - type class instances (some of which are implicit) -> UserSerializer, IntSerializer, etc.
   - conversion with implicit classes -> HTMLEnrichment
   */

  // context bounds

  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body> ${content.toHTML(serializer)}</body></html>"

//  def htmlSugar[T: HTMLSerializer](content: T): String =  // [T: HTMLSerializer] -> context bouds, this tell to the compiler that inject after (content: T)
//    s"<html><body> ${content.toHTML}</body></html>"       // this (implicit serializer: HTMLSerializer[T])

  def htmlSugar[T: HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]  // you can use serializer by name, also method with compact method signature
    // user serializer
    s"<html><body> ${content.toHTML(serializer)}</body></html>"

  }

  // implicitly -> method
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code -> if we want to surface them out
  val standardPerms = implicitly[Permissions]

  // declare type class trait, type class instances ofter implicit that the object extendes of type class trait

  // option 1 - invoking ytp class instances, using companion object
  // call it implicit way

  // option 2 - enriching types with type classes -> implicit class that rewrite method of type class trait, add the implicit parameter
  // call that in enriched types -> like build in method

}
