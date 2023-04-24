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


  /*
   1 - create a generic trait called MyPredicate[-T] -> have a small method (test(T) => Boolean // return boolean)
       to test whether a value of type T passes a condition so every subclass of MyPredicate T will actually
       have a different implementation of that little method
   2 - Generic trait called MyTransformer[-A,B] -> have a small method( transform(A) => B ) to convert a value of Type A into value of Type B
       so every subclass of MyTransformer have a different implemenation
   3 - GenericMyList:
       - map(transformer) =>  GenericMyList   // retun a new MyList of different type
       - filter(MyPredicate) => GenericMyList
       - flapmap(transformer from A to GenericMyList[B]) =>  GenericMyList[B]

       class EvenPredicate extends MyPredicate[Int]
       class StringToIntTransformer extends MyTransformer[String, Int]

       pseudocode
       [1,2,3].map(n * 2) = [2,4,6]
       filter
       [1,2,3,4].filter(n%2) = [2,4]
       [1,2,3].flatmap(n => [n,n+1]) => [1,2,2,3,3,4]

  */

}
