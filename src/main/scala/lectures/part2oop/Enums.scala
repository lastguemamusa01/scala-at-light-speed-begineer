package lectures.part2oop

object Enums {

  // prior scala 3 - enums were a headache to create
  // scala 3 - have first class support for enums
  // enums are data type that you can define much like a class
  // this is not work on scala 2, only work for scala 3

//  enum Permissions {
//    // data type will only have a bunch of instances that we create in the class or in this case, in our body
//    // we create with keyword case
//    // case READ, WRITE, EXECUTE, NONE   // you can only use this constant
//    // indexes start form 0 , 1 ,2 , 3 , so READ is 0
//    // ENUMS ARE USED AS A DATA TYPE ->you can enumerate all the possible cases or instances
//    // you created a sealed data type that cannot be extended
//    // You can add fields or methods
//    // enums work like any class but with constant specified
//
//    def openDocument(): Unit =
//      if(this == READ) println("opening document...")
//      else println("reading not allowed")
//  }

  // val somePermissions: Permissions = Permissions.READ   // You can define enums as data type and use the constant READ

  // enums can take constructor arguments
//  enum PermissionWithBits(bits: int) {
//    case READ extends PermissionWithBits(4) // 100
//    case WRITE extends PermissionWithBits(2) // 010
//    case EXECUTE extends PermissionWithBits(1) // 001
//    case NONE extends PermissionWithBits(0) // 000
//  }

  // you can have companion objects with enum

//  object PermissionWithBits {
    // methods or fields
    // you can use companion object for an enum as a source for factory methods
    // in factory methods given some arguments will return on possible instance of your enum
    // it depends on your arguments, you receive one of the constant

//    def fromBits(bits: Int): PermissionWithBits = // whatever
//      PermissionWithBits.NONE

//  }

  // Standard API of enum -> built in

//  val somePermissionsOrdinal = somePermissions.ordinal  // return int value of somePermisssions
//  val allPermissions = PermissionWithBits.values // array of all possible values of the enum
//  val readPermission: Permissions = Permissions.valueOf("READ") // Permissions.READ

  // java is less boilerplate like enum in scala with constructor arguments

//  def main(args: Array[String]): Unit = {
//   somePermissions.openDocument()
//   println(somePermissionsOrdinal)  // 0
//  }
}
