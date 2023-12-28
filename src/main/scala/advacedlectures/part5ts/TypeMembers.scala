package advacedlectures.part5ts

object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  // abstract type members are mostly for us to help the compiler
  // do some type inference for us, don't used too much in practice
  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal // abstract type, upper bounded animal
    type SuperBoundedAnimal >: Dog <: Animal // lower bound Dog y  super bound of Animal
    type AnimalC = Cat  // aliases, this is another name AnimalC of Cat

  }

  val ac = new AnimalCollection
  // val dog: ac.AnimalType = ???

  // val cat: ac.BoundedAnimal = new Cat // this is not allowed

  val pup: ac.SuperBoundedAnimal = new Dog // this is allowed

  val cat: ac.AnimalC = new Cat // this is fine, compiler knows type aliases

  type CatAlias = Cat //  type alises also work outside
  val anotherCat: CatAlias = new Cat // type aliases are often used in practice, especially when you have name collisions
  // with a lot of packages imported

  // type are used alternative to generics

  trait MyList {
    type T

    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int

    def add(element: Int): MyList = ???
  }

  // .type - we can use some values type as a type alias

  type CatsType = cat.type
  val newCat: CatsType = cat // this is fine
  // new CatsType // , but this is only association, not new instanciatiation

  // locked
  trait MList { // api signature
    type A

    def head: A

    def tail: MList
  }

  // Number type
  // type members and type member constraints (bounds)

  trait ApplicableToNumbers {
    type A <: Number // only applicable to the numbers
  }
  // not ok
  //  class CustomList(hd: String, tl: CustomList) extends MyList with ApplicableToNumbers {
  //    type A = String
  //    def head: String = hd
  //    def tail = tl
  //  }

  // ok
  class IntList(hd: Int, tl: IntList) extends MList {
    type A = Int

    def head = hd

    def tail = tl
  }





}
