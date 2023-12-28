package advacedlectures.part5ts

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance ?
  // inheritance - type substitution of generics


  class Cage[T]
  // shoud a cage of cat inherit from a cage of animals ?

  // yes = covariance

  class CCage[+T]  // +T - covariance
  val ccage: CCage[Animal] = new CCage[Cat]  // replacing more general to specific thing

  // no  = invariance
  class ICage[T]
  // val icage: ICage[Animal] = new ICage[Cat]// wrong this is like val x: Int = "hello"

  // hell no - opposite = Contravariance
  class XCage[-T] // type substitution works in the opposite sense
  val xcage: XCage[Cat] = new XCage[Animal] // valid substitution

  class InvariantCage[T](val animal: T) // invariant

  class CovariantCage[+T](val animal: T) // covariant position

  // class ContravariantCage[-T](val animal: T)
  /*
  val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  // class CovariantVariableCage[+T](var animal: T) // types of vars are in contravariant position
  /*
  val ccage: CCage[Animal] = new CCage[Cat](new Cat)
  ccage.animal = new Crocodile
   */

  // class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION
  /*
  val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  class InvariantVariableCage[T](var animal: T) // ok
  // covariant and contravariant positions are basically some compiler restrictions

//  trait AnotherCovariantCage[+T] {
//    def addAnimal(animal: T) // Contravariant poisition
//  }
  /*
  val ccage: CCage[Animal] = new CCage[Dog]
  ccage.add(new Cat) // this prohibite add contra variant poisition because you create the Animal type of Dog, so we dont want to add Cat in dog cage
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true // this is fine
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat) // new Dog is incorrect in here
  class Kitty extends Cat
  acc.addAnimal(new Kitty)


  //[B >: A] -> B is super type of A -> widening the type

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  // method argemnts are in contravariant position

  // return types
  class PetShop[-T] {
  //  def get(isItaPuppy: Boolean): T // method return types are in covariant position
  /*
  val catShop = new PetShop[Animal] {
    def get(isItaPuppy: Boolean): Animal = new Cat
  }
  val dogShop: PetShop[Dog] = catShop
  dogShop.get(true) // this return evil cat !
   */

    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal   // S is sub type of T
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  // val evilCat = shop.get(true, new Cat) // this is illegal, becuase Cat is not sub type of Dog
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
  Big rule
  1 - method arguments are in contravariant position
  2 - return types are in covariant position
   */

  /**
   * 1. invariant, covariant and contravariant
   *  Parking[T](things: List[T]) {
   *    park(vehicle: T)
   *    impound(vehicles: List[T])
   *    checkVehicles(conditions: String): List[T]
   *  }
   *
   *  2. used someone else's API: IList[T] -> invariant List
   *  3. Parking = monad!
   *    - flatMap
    */

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  class IList[T]

  // invariance - so boring - only allowed one type
  // 3 - monads - flatMap
  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???
    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  // covariance

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  // contravariance
  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???

    def impound(vehicles: List[T]): XParking[T] = ???

    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: Function1[R, XParking[S]]): XParking[S] = ???
  }

  /*
  rule of thumb

 - use covariance = collections of things -> if you use a parking as a collection of vehicles, then use covariance
 - use contravariance - group of actions
   */

  //  2. used someone else's API: IList[T] -> invariant List

  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???

    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???

    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  // contravariance
  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking2[T] = ???

    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ???

    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }



}
