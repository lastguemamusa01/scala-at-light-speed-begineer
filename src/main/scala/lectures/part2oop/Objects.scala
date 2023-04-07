package lectures.part2oop

object Objects extends App {

  // object in scala is dedicated concepts, instead of object is instance of classes
  // class level functionality - functionality that does not depend on an instance of a class
  // -> like universal constants or universal funcionality we should be able to access without relying
  // on an instance of some class

  // Scala does not have class level functionality -> doesn't know static
  // instead having static -> in scala we have object equivalent
  object Person {  // type + it's only instance
    // "static" or "class" - level functionality
    val N_EYES=2   // this is equivalent to java class level functionality
    def canFly: Boolean = false

    // factory methods can build the object
    // purpose is build Person
    // def from(mother: Person, father: Person): Person = new Person("Bobbie")
    // we can use apply
    def apply(mother: Person, father: Person): Person = new Person("Bobbie")
  }

  // we can write same name of class and object -> companions


  class Person(val name: String) {
    // instance-level functionality
  }

  // objects be defined in a similar way that classes can, with the exception that object don't
  // receive parameters.
  // you can access as class level setting



  println(Person.N_EYES)
  println(Person.canFly)

  // scala object = Singleton Instance
  val mary = Person   // instance of the Person type, only instance, this person type can have
  val john = Person
  println(mary == john) // true, mary and john point same instance of object call Person

  val person1 = new Person("Mary")
  val person2 = new Person("Jhon")
  println(person1 == person2) // false
  // by default or definition object is singleton

  val bobbie = Person(person1, person2) // this accept only class

  // scala Applications = Scala Object with
  // def main(args: Array[String]): Unit
  // this method is trasnslate to java public static void main



}
