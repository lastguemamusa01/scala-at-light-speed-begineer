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

  // type class -> template
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

  /*
  exercises
  1 - Equality
  */

  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  implicit object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }



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


  /*
  Exercise: Implement the Type Class pattern for the Equality type class
   */

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val anotherMin = User("Min", 29, "anotherMin@hotmail.com")
  println(Equal(min, anotherMin))   // instead of using Equal.apply(min. anotherMin) ad-hoc polymorphism

  // ad-hoc polymorphism - we achieved polymorphism in the sense that if 2 disntict or potentilally
  // unrelated types have equalizers implemented, then we can call this equal thing on them
  // regardless of their type
  // polymorphism = because depending on the actual type of the values being compared, the compiler
  // takes care to fetch the correct type class instance for our types





}
