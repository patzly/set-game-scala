package de.htwg.se.set.model

import de.htwg.se.set.util.PrintUtil

case class Player(number: Int, singlePlayer: Boolean, easy: Boolean, sets: List[Triplet]):

  def index: Int = number - 1

  def foundSet(set: Triplet): Player =
    if sets.contains(set) then
      println(PrintUtil.red("SET already found."))
      this
    else
      if set.isSet then
        println(PrintUtil.green("That's a SET!"))
        copy(sets = sets.appended(set))
      else
        if sets.isEmpty || singlePlayer then
          println(PrintUtil.red("That's not a SET!"))
          this
        else
          println(PrintUtil.red("That's not a SET! One SET removed."))
          copy(sets = sets.dropRight(1))

  override def toString: String =
    if singlePlayer then
      val max = if easy then 3 else 6
      val setStrings = sets.map(set => if easy then set.toStringEasy else set.toString)
      sets.length + " of " + max + " SETs found:\n" + setStrings.mkString("\n")
    else
      "Player " + number + ": " + PrintUtil.purple(sets.length.toString)