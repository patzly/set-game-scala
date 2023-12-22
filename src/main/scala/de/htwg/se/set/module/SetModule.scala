package de.htwg.se.set.module

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.set.controller.IController
import de.htwg.se.set.controller.controller.base.Controller
import de.htwg.se.set.model.game.base.{Deck, Game}
import de.htwg.se.set.model.settings.base.Settings
import de.htwg.se.set.model.{GameMode, ICard, IDeck, IGame, IPlayer, ISettings}
import net.codingwell.scalaguice.ScalaModule

class SetModule extends AbstractModule with ScalaModule:

  private val defaultPlayerCount = 1
  private val defaultEasy = false
  private val defaultGameMode = GameMode.SETTINGS
  private val defaultColumns = 4

  override def configure(): Unit =
    bindConstant().annotatedWith(Names.named("playerCount")).to(defaultPlayerCount)
    bindConstant().annotatedWith(Names.named("easy")).to(defaultEasy)
    bind[GameMode].toInstance(defaultGameMode)
    bind(classOf[ISettings]).to(classOf[Settings])
    
    bindConstant().annotatedWith(Names.named("columns")).to(defaultColumns)
    bind(classOf[IDeck]).to(classOf[Deck])
    bind[List[ICard]].toInstance(List[ICard]())
    bind[List[IPlayer]].toInstance(List[IPlayer]())
    bind[Option[IPlayer]].toInstance(None)
    bind(classOf[IGame]).to(classOf[Game])
    
    bind(classOf[IController]).to(classOf[Controller])