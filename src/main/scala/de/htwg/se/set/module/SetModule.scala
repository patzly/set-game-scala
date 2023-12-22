package de.htwg.se.set.module

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.set.controller.IController
import de.htwg.se.set.controller.controller.Controller
import de.htwg.se.set.model.game.{Deck, Game}
import de.htwg.se.set.model.settings.Settings
import de.htwg.se.set.model.{GameMode, ICard, IDeck, IGame, IPlayer, ISettings}
import net.codingwell.scalaguice.ScalaModule

class SetModule extends AbstractModule with ScalaModule:

  override def configure(): Unit =
    val defaultPlayerCount = 1
    val defaultEasyMode = false
    val defaultGameMode = GameMode.SETTINGS
    val defaultColumns = 4
    
    // Settings
    bindConstant().annotatedWith(Names.named("playerCount")).to(defaultPlayerCount)
    bindConstant().annotatedWith(Names.named("easy")).to(defaultEasyMode)
    bind[GameMode].toInstance(defaultGameMode)
    bind(classOf[ISettings]).to(classOf[Settings])

    // Game
    bindConstant().annotatedWith(Names.named("columns")).to(defaultColumns)
    bind(classOf[IDeck]).to(classOf[Deck])
    bind[List[ICard]].toInstance(List[ICard]())
    bind[List[IPlayer]].toInstance(List[IPlayer]())
    bind[Option[IPlayer]].toInstance(None)
    bind(classOf[IGame]).to(classOf[Game])
    
    // Controller
    bind(classOf[IController]).to(classOf[Controller])