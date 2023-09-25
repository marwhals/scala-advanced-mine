package lectures.part4implicits

object PimpMyLibrary extends App {
  // Decorate classes with additional  functionality and  properties

  implicit class RichInt(val value: Int) extends AnyVal{
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)
    def times(function: () => Unit): Unit = {
      def timesAux(n : Int): Unit = {
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }
        timesAux(value)
      }
    }
    def *[T](list:  List[T]): List[T] = {
      def concatenate(n: Int):  List[T]  =
        if (n <= 0)  List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }

  new RichInt(42).sqrt

  42.isEven // new RichInt(42).isEven

  //type enrichmment = pimping

  1 to 10

  import scala.concurrent.duration._

  3.seconds

  //compiler doesn't do multiple implicit searches
  //42.isOdd

  //ex enrich the String class and then enrich the Int class further
  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string)
    def encrypt(cypherDistance: Int): String = string.map(c => (c  + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)
  println("John".encrypt(2))
  3.times(() => println("Scala Rocks!"))

  println(4 * List(1,2,3))

  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6"/ 2) //  stringToInt("6")/2

  // equivalent: implicit class  RichAltInt(value: Int)
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  // danger zonoe
  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionedValue = if (3) "Ok" else "Something wrong"
  println(aConditionedValue)

  /**
   * Pimp my library - type enrichment
   * - Keep type enrichment to implicit classes and type classes
   * - avoid implicit defs as much as possible
   * - package implicits clearly, bring into scoop only what you need
   * - If conversions are needed make the specific
   */





}
