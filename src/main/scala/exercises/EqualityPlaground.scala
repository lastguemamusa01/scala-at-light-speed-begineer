package exercises

import advacedlectures.part4implicits.TypeClasses.User

object EqualityPlaground extends App {

  /*
  exercises
  1 - Equality
  */

  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  /*
  Exercise: Implement the Type Class pattern for the Equality type class
   */

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val min = User("Min", 32, "min253@rockthejvm.com")
  val anotherMin = User("Min", 29, "anotherMin@hotmail.com")
  println(Equal(min, anotherMin)) // instead of using Equal.apply(min. anotherMin) ad-hoc polymorphism
  // ad-hoc polymorphism - we achieved polymorphism in the sense that if 2 disntict or potentilally
  // unrelated types have equalizers implemented, then we can call this equal thing on them
  // regardless of their type
  // polymorphism = because depending on the actual type of the values being compared, the compiler
  // takes care to fetch the correct type class instance for our types

  /*
  Exercise - improve the Equal Type Class with an implicit conversion class
  ===(anotherValue: T)
  !==(anotherValue: T)
   */

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = ! equalizer.apply(value, other)

  }

  println(min ===  anotherMin)
  /*
   compiler rewrite to -> min.===(anotherMin)
   compiler wrap that into a new type -> new TypeSafeEqual[User](min).===(anotherMin)
   compiler rewrite to -> new TypeSafeEqual[User](min).===(anotherMin)(NameEquality)
   */
  /*
  type safe
   */

  println(min == 43) // only work in scala 2
  // println(min === 43)  // compiler mandatory must the 2 object need to be same type -> type safe, this throw error

}
