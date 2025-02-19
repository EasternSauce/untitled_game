package com.easternsauce.game.client

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Buttons
import com.easternsauce.game.Constants
import com.easternsauce.game.connectivity.GameClientConnectivity
import com.easternsauce.game.core.{CoreGame, Gameplay}
import com.easternsauce.game.gamestate.GameState
import com.easternsauce.game.gamestate.ability.AbilityType
import com.easternsauce.game.gamestate.event._
import com.easternsauce.game.gamestate.id.AreaId
import com.easternsauce.game.gameview.GameScreen
import com.easternsauce.game.math.MousePosTransformations
import com.easternsauce.game.screen.gameplay.client.ClientGameplayScreen
import com.easternsauce.game.screen.pausemenu.client.ClientPauseMenuScreen
import com.easternsauce.game.screen.startmenu.client.ClientStartMenuScreen
import com.esotericsoftware.kryonet.Client

abstract class CoreGameClientBase extends CoreGame {
  implicit val game: CoreGame = this

  private var clientRegistered = false

  private var _gameplay: Gameplay = _
  private var _connectivity: GameClientConnectivity = _

  private var _gameplayScreen: GameScreen = _
  private var _startMenuScreen: GameScreen = _
  private var _pauseMenuScreen: GameScreen = _

  override protected def init(): Unit = {
    _gameplayScreen = ClientGameplayScreen(this)
    _startMenuScreen = ClientStartMenuScreen(this)
    _pauseMenuScreen = ClientPauseMenuScreen(this)

    _gameplay = Gameplay()
    _gameplay.init()

    _connectivity = GameClientConnectivity(this)
  }

  override def update(delta: Float): Unit = {
    val areaId = clientCreatureAreaId.getOrElse(Constants.DefaultAreaId)

    handleInputs()

    gameplay.updateTimers(delta)
    gameplay.update(areaId, delta)
    gameplay.render(areaId, delta)

    processEvents(areaId)

    processGameStateOverride()
  }

  protected def processEvents(areaId: AreaId): Unit

  private def processGameStateOverride(): Unit = {
    scheduledOverrideGameState.foreach { gameState =>
      gameplay.gameStateHolder.forceGameStateOverride(gameState)
      clientCreatureAreaId.foreach(gameplay.physics.correctBodyPositions(_))
      scheduledOverrideGameState = None
    }
  }

  def scheduleOverrideGameState(gameState: GameState): Unit = {
    scheduledOverrideGameState = Some(gameState)
  }

  override protected def handleInputs(): Unit = {
    if (gameplay.buttonHeldChecker.isButtonHeld(Buttons.LEFT)) {
      handleMoveInput()
    }
    if (gameplay.buttonHeldChecker.isButtonHeld(Buttons.RIGHT)) {
      handleAttackInput()
    }
  }

  private def handleAttackInput(): Unit = {
    val clientCreature = clientCreatureId
      .filter(gameState.creatures.contains)
      .map(gameState.creatures(_))

    val currentAbilityType = AbilityType.Arrow

    clientCreature.foreach { creature =>
      if (
        creature.isAlive &&
        (!creature.params.abilityCooldownTimers(currentAbilityType).isRunning ||
          creature.params.abilityCooldownTimers(currentAbilityType).time > 1f)
      ) {
        val destination = MousePosTransformations.mouseWorldPos(
          Gdx.input.getX.toFloat,
          Gdx.input.getY.toFloat,
          creature.pos
        )

        sendBroadcastEvents(
          List(
            CreaturePerformAbilityEvent(
              creature.id,
              creature.currentAreaId,
              currentAbilityType,
              creature.pos,
              destination,
              None
            )
          )
        )
      }
    }
  }

  private def handleMoveInput(): Unit = {
    val clientCreature = clientCreatureId
      .filter(gameState.creatures.contains)
      .map(gameState.creatures(_))

    clientCreature.foreach { creature =>
      val destination = MousePosTransformations.mouseWorldPos(
        Gdx.input.getX.toFloat,
        Gdx.input.getY.toFloat,
        creature.pos
      )

      sendBroadcastEvents(
        List(
          CreatureGoToEvent(
            creature.id,
            creature.currentAreaId,
            destination
          )
        )
      )
    }
  }

  override def sendBroadcastEvents(events: List[GameStateEvent]): Unit = {
    game.queues.broadcastEvents ++= events
  }

  def registerClient(clientId: String): Unit = {
    clientData.clientId = Some(clientId)
    clientRegistered = true
  }

  override def gameplay: Gameplay = _gameplay
  override protected def connectivity: GameClientConnectivity = _connectivity
  def client: Client = _connectivity.endPoint

  override protected def gameplayScreen: GameScreen = _gameplayScreen
  override protected def startMenuScreen: GameScreen = _startMenuScreen
  override protected def pauseMenuScreen: GameScreen = _pauseMenuScreen

  override def dispose(): Unit = {
    super.dispose()
    if (client != null) {
      client.close()
    }
  }

  def isOffline: Boolean

}
