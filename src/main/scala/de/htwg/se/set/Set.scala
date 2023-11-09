package de.htwg.se.set

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.view.Tui
import util.PrintUtil
import model.{Card, Deck, Game, Player, Settings, Triplet}

@main
def main(): Unit =
  println(PrintUtil.bold("Welcome to the SET Game!"))

  val settings = Settings(1, false)

  val deck = Deck(false)
  val cards = deck.tableCardsSinglePlayer(3 * 4)
  val players = List[Player](Player(1, true, false, List[Triplet]()))
  val game = Game(3, 4, deck, cards, List[Card](), players)

  val controller = Controller(settings, game)
  Tui(controller).run()

/*def gameLoop(rows: Int, columns: Int, deck: Deck, tableCards: List[Card], playersCards: List[Card], players: List[Player]): Unit =
  val singlePlayer = players.length == 1
  if !singlePlayer then
    println(s"Input player who found a SET (e.g. 1) or 0 if no SET can be found:")
  val input = if singlePlayer then 1 else Tui.intInput(0, players.length)
  if input == 0 then
    val columnsUpdated = columns + 1
    val cardsAdded = deck.tableCards(rows * columnsUpdated, tableCards, playersCards)
    if cardsAdded.length > tableCards.length then
      println("One column of cards added to the table.")
      println("\n" + Grid(rows, columnsUpdated, cardsAdded, deck.easy))
      gameLoop(rows, columnsUpdated, deck, cardsAdded, playersCards, players)
    else if deck.findSets(tableCards).nonEmpty then
      println(PrintUtil.red("No more cards left, but there still is at least one SET to be found!\n"))
      gameLoop(rows, columns, deck, tableCards, playersCards, players)
    else
      println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
      players.foreach(player => println(player))
      settingsLoop(players.length, deck.easy)
  val player = if singlePlayer then players.head
  else if input != 0 then players(input - 1)
  else players(Tui.intInput(1, players.length) - 1)

  println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
  val coordinates = Tui.coordinatesInput
  val cards = deck.tableCards(rows * columns, tableCards, playersCards)
  val card1 = deck.cardAtCoordinate(cards, coordinates.head, columns)
  val card2 = deck.cardAtCoordinate(cards, coordinates(1), columns)
  val card3 = deck.cardAtCoordinate(cards, coordinates(2), columns)
  val cardsSelected = deck.selectCards(cards, card1, card2, card3)
  println("\n" + Grid(rows, columns, cardsSelected, deck.easy))

  val triplet = Triplet(card1.select, card2.select, card3.select)
  val playerUpdated = player.foundSet(triplet)
  val replaceSet = !singlePlayer && triplet.isSet
  val playersCardsAdded = if replaceSet then deck.playersCardsAdd(playersCards, triplet) else playersCards
  val columnsUpdated = if replaceSet && columns > (if deck.easy then 3 else 4) then columns - 1 else columns
  // TODO: remove min columns condition and replace with isStapleEmpty condition
  val cardsUpdated = if replaceSet then
    deck.tableCards(rows * columnsUpdated, cardsSelected, playersCardsAdded) else deck.unselectCards(cardsSelected)

  if singlePlayer && playerUpdated.sets.nonEmpty then
    println(playerUpdated)
  val playersUpdated = players.updated(player.index, playerUpdated)

  if singlePlayer then
    val finished = if deck.easy then playerUpdated.sets.length == 3 else playerUpdated.sets.length == 6
    if finished then
      println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
      settingsLoop(players.length, deck.easy)

  println("\n" + Grid(rows, columnsUpdated, cardsUpdated, deck.easy))
  gameLoop(rows, columnsUpdated, deck, cards, playersCardsAdded, playersUpdated)*/