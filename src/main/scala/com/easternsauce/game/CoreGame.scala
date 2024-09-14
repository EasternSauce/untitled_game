package com.easternsauce.game

import com.badlogic.gdx.{Game, Gdx}
import com.easternsauce.game.connectivity.GameConnectivity
import com.easternsauce.game.gamestate.GameState
import com.easternsauce.game.gamestate.creature.Creature
import com.easternsauce.game.gamestate.event.GameStateEvent
import com.easternsauce.game.gamestate.id.{AreaId, GameEntityId}
import com.easternsauce.game.gameview.GameScreen
import com.esotericsoftware.kryonet.Listener

import scala.collection.mutable.ListBuffer

abstract class CoreGame extends Game {

  protected var gameplayScreen: GameScreen = _
  protected var startMenuScreen: GameScreen = _
  protected var pauseMenuScreen: GameScreen = _

  protected var broadcastEventsQueue: ListBuffer[GameStateEvent] = _

  var clientData: ClientData = _

  protected def init(): Unit

  override def create(): Unit = {
    init()

    gameplayScreen.init()
    startMenuScreen.init()
    pauseMenuScreen.init()

    clientData = ClientData()

    broadcastEventsQueue = ListBuffer()

    setScreen(startMenuScreen)
  }

  protected def connectivity: GameConnectivity
  def gameplay: Gameplay

  def update(delta: Float): Unit

  protected def handleInputs(): Unit

  def close(): Unit = {
    Gdx.app.exit()
  }

  def clientCreatureId: Option[GameEntityId[Creature]] = {
    clientData.clientId.map(GameEntityId[Creature])
  }

  def clientCreatureAreaId: Option[AreaId] = {
    clientCreatureId
      .filter(gameState.creatures.contains(_))
      .map(gameState.creatures(_))
      .map(_.currentAreaId)
  }

  def gameState: GameState = gameplay.gameState

  def listener: Listener = {
    connectivity.listener
  }

  def processBroadcastEventsForArea(
      area: AreaId,
      gameState: GameState
  ): GameState

  def sendEvent(
      event: GameStateEvent
  ): Unit // TODO: does it duplicate applyEvent?

  def applyEventsToGameState(events: List[GameStateEvent]): Unit = {
    gameplay.applyEventsToGameState(events)
  }

  def setClientData(clientId: String, host: String, port: String): Unit = {
    if (clientId.nonEmpty) {
      clientData.clientId = Some(clientId)
    }
    if (host.nonEmpty) {
      clientData.host = Some(host)
    }
    if (port.nonEmpty) {
      clientData.port = Some(port)
    }

    clientData.clientId.foreach(schedulePlayerToCreate)
  }

  def setPauseScreen(): Unit = {
    setScreen(pauseMenuScreen)
  }

  def setGameplayScreen(): Unit = {
    setScreen(gameplayScreen)
  }

  def schedulePlayerToCreate(clientId: String): Unit = {
    gameplay.schedulePlayerToCreate(clientId)
  }

}
