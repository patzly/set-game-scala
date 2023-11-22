package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{SettingsState, TuiState}
import de.htwg.se.set.util.{Event, Observer, PrintUtil}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}

case class Tui(controller: Controller) extends Observer:

  controller.add(this)

  private var state: TuiState = SettingsState(this)

  def run(): Unit =
    println(controller.settingsToString)
    stateLoop()

  @tailrec
  private def stateLoop(): Unit =
    state.run()
    stateLoop()

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => println(controller.settingsToString)
      case Event.CARDS_CHANGED => println(controller.gameToString)
      case _ =>

  def changeState(newState: TuiState): Unit = state = newState
    
  final def stringInput: String = StdIn.readLine().trim

  @tailrec
  final def intInput: Int = Try(stringInput.toInt) match
    case Success(value) => value
    case _ =>
      println(PrintUtil.red("Invalid input. Try again:"))
      intInput

  @tailrec
  final def intInput(min: Int, max: Int): Int =
    val user = intInput
    if min <= user && user <= max then
      user
    else
      println(PrintUtil.red(s"Only whole numbers from $min to $max allowed. Try again:"))
      intInput(min, max)

  @tailrec
  final def coordinatesInput: List[String] =
    val input = stringInput
    val coordinatesPattern = "^([A-Za-z][1-3] +){2}[A-Za-z][1-3]$".r
    input match
      case coordinatesPattern(_*) =>
        val coordinates = input.split(" +").toSet
        if coordinates.size == 3 then
          coordinates.toList
        else
          println(PrintUtil.red("Only different coordinates possible. Try again:"))
          coordinatesInput
      case _ =>
        println(PrintUtil.red("Invalid input. Try again:"))
        coordinatesInput