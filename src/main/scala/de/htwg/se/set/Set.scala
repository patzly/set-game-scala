package de.htwg.se.set

import de.htwg.se.set.model.Player

object Set {
  def main(args: Array[String]): Unit = {
    val student = Player("Your Name")
    println("Hello, " + student.name)
  }
}
