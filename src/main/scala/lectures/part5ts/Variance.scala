package lectures.part5ts

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance?
  // "inheritance" - type substitution of generics

  class Cage[T]
  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
  //  val icage: ICage[Animal] = new ICage[Cat]
  //  val x: Int = "hello"

  // hell no - opposite = contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T) // COVARIANT POSITION

  //  class ContravariantCage[-T](val animal: T)
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  //  class CovariantVariableCage[+T](var animal: T) // types of vars are in CONTRAVARIANT POSITION
  /*
    val ccage: CCage[Animal] = new CCage[Cat](new Cat)
    ccage.animal = new Crocodile
   */

  //  class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */
  class InvariantVariableCage[T](var animal: T) // ok

  //  trait AnotherCovariantCage[+T] {
  //    def addAnimal(animal: T) // CONTRAVARIANT POSITION
  //  }
  /*
    val ccage: CCage[Animal] = new CCage[Dog]
    ccage.add(new Cat)
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION.

  // return types
  class PetShop[-T] {
    //    def get(isItaPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    /*
      val catShop = new PetShop[Animal] {
        def get(isItaPuppy: Boolean): Animal = new Cat
      }
      val dogShop: PetShop[Dog] = catShop
      dogShop.get(true)   // EVIL CAT!
     */

    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  //  val evilCat = shop.get(true, new Cat)
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
    Big rule
    - method arguments are in CONTRAVARIANT position
    - return types are in COVARIANT position
   */

  /**
   * Ex1 - Design an Invariant, covariant, contravariant
   * Parking[T](things List[T]) {
   *  park(vehicle: T)
   *  impound(vehicles: List[T])
   *  checkVehicles(conditions: String): List[T]
   * }
   * Ex2 - Used someone else's API: IList[T]
   * Ex3 - Parking = monad!
   * - flatMap
   */

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  class IList[T]

  class IParking[T](vehicles: List[T]) {//Invariant
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicle(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) { //Covariance
    def park[S >: T](vehicle: S): CParking[T] = ???
    def impound[S >: T](vehicles: List[S]): CParking[T] = ???
    def checkVehicle(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }
  class XParking[-T](vehicles: List[T]) { //conotravariance
    def park(vehicle: T): XParking[T] = ??? //This is fine since T is contravariant and occurs in a contravariant position
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicle[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T , S](f: R => XParking[S]): XParking[S] = ???
  }

  /*
  Note
  - use covariance = COLLECTION OF THINGS - CPARKING
  - use contravariance = GROUP OF ACTIONS - XPARKING
   */
  class CParking2[+T](vehicles: IList[T]) { //Covariance
    def park[S >: T](vehicle: S): CParking2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicle[S >: T](conditions: String): IList[S] = ???
  }


  class XParking2[-T](vehicles: IList[T]) { //conotravariance
    def park(vehicle: T): XParking2[T] = ??? //This is fine since T is contravariant and occurs in a contravariant position
    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ???
    def checkVehicle[S <: T](conditions: String): List[S] = ???
  }

  // flatMap

}
