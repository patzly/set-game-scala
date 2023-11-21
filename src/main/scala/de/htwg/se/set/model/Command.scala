package de.htwg.se.set.model

import de.htwg.se.set.controller.Controller

sealed trait Command:

  def execute(): Unit

case class StartGameCommand(controller: Controller) extends Command:
  
  override def execute(): Unit =
    controller.setColumns(if controller.settings.easy then 3 else 4)
    controller.setDeck(Deck(controller.settings.easy))
    val deck = controller.game.deck
    val cardsMultiPlayer = deck.tableCards(controller.game.columns, List[Card](), List[Card]())
    val cardsSinglePlayer = deck.tableCardsSinglePlayer(controller.game.columns)
    controller.setTableCards(if controller.settings.singlePlayer then cardsSinglePlayer else cardsMultiPlayer)
    controller.setPlayers((1 to controller.settings.playerCount)
      .map(i => Player(i, controller.settings.singlePlayer, controller.settings.easy, List[Triplet]())).toList)

case class ChangePlayerCountCommand(controller: Controller, playerCount: Int) extends Command:

  override def execute(): Unit = controller.setPlayerCount(playerCount)

case class SwitchEasyCommand(controller: Controller) extends Command:

  override def execute(): Unit = controller.setEasy(!controller.settings.easy)