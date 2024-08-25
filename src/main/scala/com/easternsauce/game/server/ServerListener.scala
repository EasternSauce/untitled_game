package com.easternsauce.game.server

import com.easternsauce.game.command.{ActionsPerformCommand, RegisterClientRequestCommand, RegisterClientResponseCommand}
import com.easternsauce.game.gamestate.creature.Creature
import com.easternsauce.game.gamestate.event.PlayerDisconnectEvent
import com.easternsauce.game.gamestate.id.GameEntityId
import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive
import com.esotericsoftware.kryonet.{Connection, Listener}

case class ServerListener(game: CoreGameServer) extends Listener {
  override def disconnected(connection: Connection): Unit = {

    val reverseMap = for ((k, v) <- game.clientConnectionIds) yield (v, k)

    if (reverseMap.contains(connection.getID)) {
      val disconnectedCreatureId = reverseMap(connection.getID)
      val playerDisconnectEvent = PlayerDisconnectEvent(
        GameEntityId[Creature](disconnectedCreatureId)
      )
      game.applyEvent(playerDisconnectEvent)

      game.unregisterClient(disconnectedCreatureId, connection.getID)
    }
  }

  override def received(connection: Connection, obj: Any): Unit = {
    obj match {
      case RegisterClientRequestCommand(maybeClientId) =>
        val clientId = maybeClientId.getOrElse(game.generateNewClientId())
        game.registerClient(clientId, connection.getID)
        connection.sendTCP(RegisterClientResponseCommand(clientId))
      case ActionsPerformCommand(actions) =>
        actions.foreach(game.applyEvent)
      case _: KeepAlive =>
    }
  }
}
