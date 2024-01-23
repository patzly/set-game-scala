package de.htwg.se.set.controller.controller

import de.htwg.se.set.model.GameMode.{GAME_END, SETTINGS}
import de.htwg.se.set.model.game.{Card, Deck, Game, Player}
import de.htwg.se.set.model.settings.Settings
import de.htwg.se.set.model.{Color, GameMode, ICard, IDeck, IPlayer, Shading, Symbol}
import org.mockito.ArgumentMatchers.{any, anyInt, anyString}
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Json

import scala.collection.immutable.List
import scala.io.Source
import scala.util.Using
import scala.xml.XML

class CommandSpec extends AnyWordSpec with Matchers with MockitoSugar:

  private def testController(singlePlayer: Boolean) =
    val settings = Settings(if singlePlayer then 1 else 2, easy = false)
    val columns = 3
    val easy = false
    val deck = Deck(easy)
    val cards = deck.tableCards(columns, List(), List())
    val players = if singlePlayer then
      List(Player(1, true, easy, List()))
    else
      List(Player(1, false, easy, List()), Player(2, false, easy, List()))
    val game = Game(columns, deck, cards, List(), players, None)
    Controller(settings, game)

  "A Command" when:
    "saving a snapshot" should:
      "invoke the snapshot method on the controller" in:
        val spyController = spy(testController(false))
        val command = new Command(spyController)
        command.saveSnapshot()
        verify(spyController, times(1)).snapshot
    "undoing a command" should:
      "restore the saved snapshot if available" in:
        val spyController = spy(testController(false))
        val snapshot = mock[Snapshot]
        val command = new Command(spyController)
        when(spyController.snapshot).thenReturn(snapshot)
        command.saveSnapshot()
        command.undo()
        verify(spyController, times(1)).restoreSnapshot(snapshot)
      "throw an exception if no snapshot is available" in:
        val spyController = spy(testController(false))
        val command = new Command(spyController)
        assertThrows[IllegalStateException]:
          command.undo()
  "A StartGameCommand" when:
    "in single-player mode" should:
      "execute the start game sequence" in:
        val spyController = spy(testController(true))
        val command = StartGameCommand(spyController)
        command.execute()
        verify(spyController, times(1)).setColumns(anyInt())
        verify(spyController, times(1)).setDeck(any[IDeck])
        verify(spyController, times(1)).setTableCards(any[List[ICard]])
        verify(spyController, times(1)).setPlayersCards(any[List[ICard]])
        verify(spyController, times(1)).setPlayers(any[List[IPlayer]])
        verify(spyController, times(1)).selectPlayer(1)
        verify(spyController, times(1)).setGameMode(any[GameMode])
        verify(spyController, times(1)).setMessage(anyString())
        verify(spyController, times(1)).changeState(any[GameState])
    "in multiplayer mode" should:
      "execute the start game sequence" in:
        val spyController = spy(testController(false))
        val command = StartGameCommand(spyController)
        command.execute()
        verify(spyController, times(1)).setColumns(anyInt())
        verify(spyController, times(1)).setDeck(any[IDeck])
        verify(spyController, times(1)).setTableCards(any[List[ICard]])
        verify(spyController, times(1)).setPlayersCards(any[List[ICard]])
        verify(spyController, times(1)).setPlayers(any[List[IPlayer]])
        verify(spyController, times(1)).unselectPlayer()
        verify(spyController, times(1)).setGameMode(any[GameMode])
        verify(spyController, times(1)).setMessage(anyString())
        verify(spyController, times(1)).changeState(any[GameState])
  "A GoToPlayerCountCommand" should:
    "change into ChangePlayerCountState" in:
      val spyController = spy(testController(false))
      val command = GoToPlayerCountCommand(spyController)
      command.execute()
      verify(spyController, times(1)).changeState(ChangePlayerCountState(spyController))
  "A ChangePlayerCountCommand" should:
    "change player count" in:
      val spyController = spy(testController(false))
      val command = ChangePlayerCountCommand(spyController, 2)
      command.execute()
      verify(spyController, times(1)).setPlayerCount(2)
      verify(spyController, times(1)).changeState(SettingsState(spyController))
  "A SwitchEasyCommand" should:
    "toggle easy mode" in:
      val spyController = spy(testController(false))
      val command = SwitchEasyCommand(spyController)
      command.execute()
      //verify(spyController, times(1)).setEasy(!spyController.settings.easy)
      verify(spyController, times(1)).changeState(SettingsState(spyController))
  "A SelectPlayerCommand" should:
    "select the specified player" in:
      val spyController = spy(testController(false))
      val command = SelectPlayerCommand(spyController, 1)
      command.execute()
      verify(spyController, times(1)).selectPlayer(1)
      verify(spyController, times(1)).setMessage(anyString())
      verify(spyController, times(1)).changeState(GameState(spyController))
  "An AddColumnCommand" when:
    "cards available and maximum columns not reached" should:
      "add a column" in:
        val spyController = spy(testController(false))
        val command = AddColumnCommand(spyController)
        command.execute()
        verify(spyController, times(1)).addColumn()
        val cardsAdded = spyController.game.deck.tableCards(
          spyController.game.columns, spyController.game.tableCards, spyController.game.playersCards
        )
        verify(spyController, times(1)).setMessage(anyString())
        verify(spyController, times(1)).setTableCards(cardsAdded)
        verify(spyController, times(1)).changeState(SelectPlayerState(spyController))
        verify(spyController, never).removeColumn()
    "cards available and maximum columns reached but still a set to be found" should:
      "don't add a column" when:
        val settings = Settings(2, easy = false)
        val columns = 6
        val deck = Deck(easy = false)
        val cards = List(
          Card(1, Color.RED, Symbol.OVAL, Shading.SOLID),
          Card(2, Color.RED, Symbol.OVAL, Shading.SOLID),
          Card(3, Color.RED, Symbol.OVAL, Shading.SOLID),
          Card(1, Color.RED, Symbol.OVAL, Shading.OUTLINED),
          Card(2, Color.RED, Symbol.OVAL, Shading.OUTLINED),
          Card(3, Color.RED, Symbol.OVAL, Shading.OUTLINED),
          Card(1, Color.RED, Symbol.OVAL, Shading.STRIPED),
          Card(2, Color.RED, Symbol.OVAL, Shading.STRIPED),
          Card(3, Color.RED, Symbol.OVAL, Shading.STRIPED),
          Card(1, Color.RED, Symbol.DIAMOND, Shading.SOLID),
          Card(2, Color.RED, Symbol.DIAMOND, Shading.SOLID),
          Card(3, Color.RED, Symbol.DIAMOND, Shading.SOLID),
          Card(1, Color.RED, Symbol.SQUIGGLE, Shading.SOLID),
          Card(2, Color.RED, Symbol.SQUIGGLE, Shading.SOLID),
          Card(3, Color.RED, Symbol.SQUIGGLE, Shading.SOLID),
          Card(1, Color.GREEN, Symbol.OVAL, Shading.SOLID),
          Card(2, Color.GREEN, Symbol.OVAL, Shading.SOLID),
          Card(3, Color.GREEN, Symbol.OVAL, Shading.SOLID) // 18 cards, 6 columns with sets
        )
        val players = List(
          Player(1, singlePlayer = false, easy = false, List()),
          Player(2, singlePlayer = false, easy = false, List())
        )
        val game = Game(6, deck, cards, List(), players, None)
        val controller = Controller(settings, game)
        val spyController = spy(controller)
        val command = AddColumnCommand(spyController)
        command.execute()
        verify(spyController, times(1)).setMessage(anyString())
        verify(spyController, times(1)).removeColumn()
        verify(spyController, times(1)).changeState(SelectPlayerState(spyController))
    "end game if no more cards and no sets are available" in:
      val settings = Settings(2, easy = false)
      val columns = 1
      val deck = Deck(easy = false)
      val cards = List(
        Card(1, Color.RED, Symbol.OVAL, Shading.SOLID),
        Card(1, Color.RED, Symbol.OVAL, Shading.SOLID),
        Card(3, Color.GREEN, Symbol.OVAL, Shading.SOLID)
      )
      val players = List(
        Player(1, singlePlayer = false, easy = false, List()),
        Player(2, singlePlayer = false, easy = false, List())
      )
      val game = Game(6, deck, cards, deck.allCards, players, None)
      val controller = Controller(settings, game)
      val spyController = spy(controller)
      val command = AddColumnCommand(spyController)
      command.execute()
      verify(spyController, never).removeColumn()
      verify(spyController, times(1)).setGameMode(GAME_END)
      verify(spyController, times(1)).changeState(GameEndState(spyController))
  "An ExitCommand" should:
    "exit and go to settings" in:
      val spyController = spy(testController(false))
      val command = ExitCommand(spyController)
      command.execute()
      verify(spyController, times(1)).setGameMode(SETTINGS)
      verify(spyController, times(1)).changeState(SettingsState(spyController))
  "A LoadXmlCommand" when:
    "loading a xml file with a valid hash" should:
      "load the progress" in:
        val spyController = spy(testController(false))
        val command = LoadXmlCommand(spyController, XML.loadFile("src/test/resources/valid_progress.xml"))
        command.execute()
        verify(spyController, times(1)).restoreSnapshot(any[Snapshot])
    "loading a xml file with an invalid hash" should:
      "not load the progress" in:
        val spyController = spy(testController(false))
        val command = LoadXmlCommand(spyController, XML.loadFile("src/test/resources/invalid_progress.xml"))
        verify(spyController, never).restoreSnapshot(any[Snapshot])
  "A LoadJsonCommand" when:
    "loading a JSON file with a valid hash" should:
      "load the progress" in:
        val spyController = spy(testController(false))
        val content = Using(Source.fromFile("src/test/resources/valid_progress.json"))(_.mkString).getOrElse("")
        val command = LoadJsonCommand(spyController, Json.parse(content))
        command.execute()
        verify(spyController, times(1)).restoreSnapshot(any[Snapshot])
    "loading a JSON file with an invalid hash" should:
      "not load the progress" in:
        val spyController = spy(testController(false))
        val content = Using(Source.fromFile("src/test/resources/invalid_progress.json"))(_.mkString).getOrElse("")
        val command = LoadJsonCommand(spyController, Json.parse(content))
        verify(spyController, never).restoreSnapshot(any[Snapshot])