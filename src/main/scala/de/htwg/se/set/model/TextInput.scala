package de.htwg.se.set.model

class TextInput(val input: String):

  def nonEmpty: Boolean = input.nonEmpty

  def hasCoordinates: Boolean =
    val coordinatesPattern = "^([A-Za-z][1-3] +){2}[A-Za-z][1-3]$".r
    input.trim match {
      case coordinatesPattern(_*) => true
      case _ => false
    }

  def coordinates: List[String] =
    if hasCoordinates then
      input.trim.split(" +").toList
    else
      List[String]()