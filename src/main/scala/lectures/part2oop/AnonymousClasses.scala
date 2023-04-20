package lectures.part2oop

object AnonymousClasses extends App {

  abstract class Animal {
    def eat: Unit
  }

  // provide IMPLEMENTATIONS FOR ALL THE ABSTRACT FIELDS or methods in the abstract types or traits
  // that you instantiate anonymously

  //rule
  // pass in required constructor arguments if needed -> normal class -> Person(name: String)   ->  val jim = new Person("Jim")
  // implement all abstract fileds/methods  -> traits and abstract classs

  val funnyAnimal: Animal = new Animal { // this is an anonymous class
    // when we write new animal with a on the spot implementation
    // with a compiler actualy does is basically
    /*
      cutting this out :
      Animal {
        override def eat: Unit =  println("ahahahahaha") // we instantiated an abstract class
      }

      created
      class AnonymousClasses$$anon$1 extends Animal {
        override def eat: Unit =  println("ahahahahaha")
       }

       val funnyAnimal: Animal = new AnonymousClasses$$anon$1
     */
    override def eat: Unit =  println("ahahahahaha") // we instantiated an abstract class
  }

  println(funnyAnimal.getClass)  // class lectures.part2oop.AnonymousClasses$$anon$1


  class Person(name: String) {
    def sayHi: Unit = println(s"Hi, my name is $name, how can i help ?")
  }

  // anonymous class of Person

  // illegal to extend the class person without supplying the proper parameters   -> X -> val jim = new Person {
  val jim = new Person("Jim") {
    override def sayHi: Unit = println(s"Hi, my name is jim, how can i be of service ?")
  }

  println(jim.getClass)

  // anonymous class work for trait, classes and abstract classes

  trait AnimalLoco {
    def eat: Unit
  }

  val predator = new AnimalLoco {
    override def eat: Unit = println("rawr")
  }

  println(predator.getClass)

}
