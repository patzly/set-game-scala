package de.htwg.se.set.modelComponent.gameComponent

import de.htwg.se.set.util.PrintUtil

case class Player(number: Int, singlePlayer: Boolean, easy: Boolean, sets: List[Triplet]):

  def index: Int = number - 1

  override def toString: String =
    if singlePlayer then
      val max = if easy then 3 else 6
      val setStrings = sets.map(set => set.toString)
      sets.length + " of " + max + " SETs found:\n" + setStrings.mkString("\n")
    else
      "Player " + number + ": " + PrintUtil.purple(sets.length.toString)