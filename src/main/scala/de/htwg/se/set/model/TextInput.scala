package de.htwg.se.set.model

class TextInput(val input: String):

  def hasCoordinates: Boolean =
    val coordinatesPattern = "^([A-Za-z][1-3] ){0,2}[A-Za-z][1-3]$".r
    input.trim match {
      case coordinatesPattern(_*) => true
      case _ => false
    }

  def coordinates: List[String] =
    if hasCoordinates then
      input.trim.split(" ").toList
    else
      List[String]()

  def getIndex(column: Char, row: Int, columns: Int): Int =
    val columnIdx = column - 'A'
    val rowIdx = row - 1
    val numberOfColumns = 4
    rowIdx * numberOfColumns + columnIdx