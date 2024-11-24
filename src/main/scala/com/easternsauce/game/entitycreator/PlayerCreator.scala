package com.easternsauce.game.entitycreator

import com.easternsauce.game.Constants
import com.easternsauce.game.core.CoreGame
import com.easternsauce.game.gamestate.GameState
import com.easternsauce.game.gamestate.creature.{Creature, CreatureType}
import com.easternsauce.game.gamestate.id.GameEntityId
import com.easternsauce.game.math.Vector2f
import com.softwaremill.quicklens.ModifyPimp

case class PlayerCreator() extends EntityCreator {
  override def createEntities(implicit
      game: CoreGame
  ): GameState => GameState = { gameState =>
    val result = game.queues.playersToCreate.foldLeft(gameState) {
      case (gameState, playerToCreate: PlayerToCreate) =>
        createPlayer(gameState, playerToCreate)
    }

    game.queues.playersToCreate.clear()

    result
  }

  private def createPlayer(
      gameState: GameState,
      playerToCreate: PlayerToCreate
  ): GameState = {
    val creatureId = GameEntityId[Creature](playerToCreate.clientId)

    if (gameState.creatures.contains(creatureId)) {
      gameState
        .modify(_.activePlayerIds)
        .using(_ + creatureId)
    } else {
      gameState
        .modify(_.creatures)
        .using(
          _.updated(
            creatureId,
            Creature.produce(
              creatureId,
              Constants.DefaultAreaId,
              Vector2f(
                5f,
                415f
              ),
              player = true,
              creatureType = CreatureType.Human,
              spawnPointId = None
            )
          )
        )
        .modify(_.activePlayerIds)
        .using(_ + creatureId)
    }
  }
}
