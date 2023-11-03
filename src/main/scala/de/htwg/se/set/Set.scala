package de.htwg.se.set

import de.htwg.se.set.view.Tui
import util.PrintUtil
import model.{Card, Deck, Grid, Player, Triplet}

import scala.annotation.tailrec

@main
def main(): Unit =
  println(PrintUtil.bold("Welcome to the SET Game!"))
  settingsLoop(1, false)

@tailrec
def settingsLoop(playerCount: Int, easy: Boolean): Unit =
  val playersString = if playerCount == 1 then "1 player" else s"$playerCount players"
  val easyString = if easy then "easy mode" else "normal mode"
  println(PrintUtil.blue(PrintUtil.bold("\nSettings: ") + PrintUtil.yellow(playersString + ", " + easyString)))
  println(PrintUtil.bold("1") + " Start game")
  println(PrintUtil.bold("2") + " Change number of players")
  println(PrintUtil.bold("3") + " Switch to " + (if easy then "normal" else "easy") + " mode")
  val input = Tui.intInput(1, 3)
  if input == 1 then
    val rows = 3
    val columns = if easy then 3 else 4
    val deck = new Deck(easy)
    val cardsMultiPlayer = deck.tableCards(rows * columns, List[Card](), List[Card]())
    val cardsSinglePlayer = deck.tableCardsSinglePlayer(rows * columns)
    val cards = if playerCount == 1 then cardsSinglePlayer else cardsMultiPlayer
    println("\n" + new Grid(rows, columns, cards, easy))
    val players = (1 to playerCount).map(i => Player(i, playerCount == 1, easy, List[Triplet]())).toList
    gameLoop(rows, columns, deck, cards, List[Card](), players)
  else if input == 2 then
    println("Enter number of players:")
    val playerCount = Tui.intInput(1)
    settingsLoop(playerCount, easy)
  else if input == 3 then
    settingsLoop(playerCount, !easy)

@tailrec
def gameLoop(rows: Int, columns: Int, deck: Deck, tableCards: List[Card], playersCards: List[Card], players: List[Player]): Unit =
  val singlePlayer = players.length == 1
  if !singlePlayer then
    println(s"Player who found a SET (e.g. 1):")
  val player = if singlePlayer then players.head else players(Tui.intInput(1, players.length) - 1)

  println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
  val coordinates = Tui.coordinatesInput
  val cards = deck.tableCards(rows * columns, tableCards, playersCards)
  val card1 = deck.cardAtCoordinate(cards, coordinates.head, columns)
  val card2 = deck.cardAtCoordinate(cards, coordinates(1), columns)
  val card3 = deck.cardAtCoordinate(cards, coordinates(2), columns)
  val cardsSelected = deck.selectCards(cards, card1, card2, card3)
  println("\n" + new Grid(rows, columns, cardsSelected, deck.easy))

  val triplet = Triplet(card1.select, card2.select, card3.select)
  val playerUpdated = player.foundSet(triplet)
  val replaceSet = !singlePlayer && triplet.isSet
  val playersCardsAdded = if replaceSet then deck.playersCardsAdd(playersCards, triplet) else playersCards
  val cardsUpdated = if replaceSet then
    deck.tableCards(rows * columns, cardsSelected, playersCardsAdded) else deck.unselectCards(cardsSelected)

  if singlePlayer && playerUpdated.sets.nonEmpty then
    println(playerUpdated)
  val playersUpdated = players.updated(player.index, playerUpdated)

  if singlePlayer then
    val finished = if deck.easy then playerUpdated.sets.length == 3 else playerUpdated.sets.length == 6
    if finished then
      println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found. Good job!")))
      settingsLoop(players.length, deck.easy)
  else
    // TODO: multiplayer game end or column extending

  println("\n" + new Grid(rows, columns, cardsUpdated, deck.easy))
  gameLoop(rows, columns, deck, cards, playersCardsAdded, playersUpdated)