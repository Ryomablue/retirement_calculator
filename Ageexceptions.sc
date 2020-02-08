
import java.io.IOException
import java.net.URL

import scala.annotation.tailrec
import scala.util.control.NonFatal
case class Person(name: String, age: Int)
case class AgeNegativeException(message: String) extends Exception(message)

def createPerson(description: String): Person = {
  val split = description.split(" ")
  val age = split(1).toInt
  if (age < 0)
    throw AgeNegativeException(s"age: $age should be > 0")
  else
    Person(split(0), age)
}

def averageAge(descriptions: Vector[String]): Double = {
  val total = descriptions.map(createPerson).map(_.age).sum
  total / descriptions.length
}

def personsSummary(personsInput: String): String = {
  val descriptions = personsInput.split("\n").toVector
  val avg = try {
    averageAge(descriptions)
  } catch {
    case e:AgeNegativeException =>
      println(s"one person has a negative age: $e")
      0
    case NonFatal(e) =>
      println(s"something was wrong in the input: $e")
      0
  }
  s"${descriptions.length} person(s) with an average age of $avg"
}

val stream = new URL("https://www.packtpub.com/").openStream()
val htmlPage: String =
  try {
    @tailrec
    def loop(builder: StringBuilder): String = {
      val i = stream.read()
      if (i != -1)
        loop(builder.append(i.toChar))
      else
        builder.toString()
    }
    loop(StringBuilder.newBuilder)
  } catch {
    case e: IOException => s"cannot read URL: $e"
  }
  finally {
    stream.close()
  }



createPerson("John 25")
personsSummary(
  """John 25
    |Sharleen 45""".stripMargin)
personsSummary(
  """John 25
    |Sharleen45""".stripMargin)
personsSummary(
  """John -25
    |Sharleen 45""".stripMargin)
