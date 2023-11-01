package de.htwg.se.set.view

import de.htwg.se.set.util.PrintUtil

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}

object Tui:

  def stringInput: String = StdIn.readLine().trim

  @tailrec
  def intInput: Int = Try(stringInput.toInt) match {
    case Success(value) => value
    case _ =>
      println(PrintUtil.red("Ungültige Eingabe. Erneut versuchen:"))
      intInput
  }

  @tailrec
  def intInput(min: Int, max: Int): Int =
    val user = intInput
    if min <= user && user <= max then
      user
    else
      println(PrintUtil.red(s"Nur ganze Zahlen von $min bis $max zulässig. Erneut versuchen:"))
      intInput(min, max)

  @tailrec
  def intInput(min: Int): Int =
    val user = intInput
    if min <= user then
      user
    else
      println(PrintUtil.red(s"Nur ganze Zahlen ab $min zulässig. Erneut versuchen:"))
      intInput(min)

  @tailrec
  def coordinatesInput: List[String] =
    val input = stringInput
    val coordinatesPattern = "^([A-Za-z][1-3] +){2}[A-Za-z][1-3]$".r
    input match {
      case coordinatesPattern(_*) => input.split(" +").toList
      case _ =>
        println(PrintUtil.red(s"Ungültige Eingabe. Erneut versuchen:"))
        coordinatesInput
    }