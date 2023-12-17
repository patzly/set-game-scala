package de.htwg.se.set.module

import com.google.inject.AbstractModule
import de.htwg.se.set.controller.IController
import de.htwg.se.set.controller.controller.Controller
import de.htwg.se.set.model.game.{Deck, Game}
import de.htwg.se.set.model.settings.Settings
import de.htwg.se.set.model.{ICard, IGame, IPlayer, ISettings}
import net.codingwell.scalaguice.ScalaModule

class SetModule extends AbstractModule with ScalaModule:

  override def configure(): Unit =
    val settings = Settings(1, false)
    val game = Game(4, Deck(false), List[ICard](), List[ICard](), List[IPlayer](), None)
    bind[ISettings].toInstance(settings)
    bind[IGame].toInstance(game)
    bind(classOf[IController]).to(classOf[Controller])