package com.easternsauce.game

import com.easternsauce.game.gamestate.creature.{AnimationDefinition, FramesDefinition}

object Constants {
  val WindowWidth = 1360
  val WindowHeight = 720

  val ViewportWorldWidth = 1650f
  val ViewportWorldHeight = 864f

  val TileSize = 64

  val MapTextureScale = 1f

  val TileCenterX = 0
  val TileCenterY = 0

  val LayersByRenderingOrder: List[String] = List(
    "fill",
    "background",
    "object",
    "manual_object_bottom",
    "manual_object_top"
  )

  val HumanAnimationDefinition: AnimationDefinition = AnimationDefinition(
    frameWidth = 128,
    frameHeight = 128,
    stanceFrames = FramesDefinition(start = 0, count = 4, frameDuration = 0.3f),
    walkFrames = FramesDefinition(start = 4, count = 8, frameDuration = 0.075f),
    attackFrames =
      FramesDefinition(start = 12, count = 4, frameDuration = 0.1f),
    deathFrames =
      FramesDefinition(start = 16, count = 8, frameDuration = 0.12f),
    spellcastFrames =
      Some(FramesDefinition(start = 24, count = 4, frameDuration = 0.3f)),
    bowFrames =
      Some(FramesDefinition(start = 28, count = 4, frameDuration = 0.1f))
  )

  val RatAnimationDefinition: AnimationDefinition = AnimationDefinition(
    frameWidth = 128,
    frameHeight = 128,
    stanceFrames = FramesDefinition(start = 0, count = 4, frameDuration = 0.4f),
    walkFrames = FramesDefinition(start = 4, count = 8, frameDuration = 0.04f),
    attackFrames =
      FramesDefinition(start = 12, count = 7, frameDuration = 0.08f),
    deathFrames =
      FramesDefinition(start = 17, count = 11, frameDuration = 0.045f)
  )

  val ZombieAnimationDefinition: AnimationDefinition = AnimationDefinition(
    frameWidth = 128,
    frameHeight = 128,
    stanceFrames = FramesDefinition(start = 0, count = 4, frameDuration = 0.4f),
    walkFrames = FramesDefinition(start = 4, count = 8, frameDuration = 0.08f),
    attackFrames =
      FramesDefinition(start = 12, count = 4, frameDuration = 0.1f),
    deathFrames = FramesDefinition(start = 22, count = 6, frameDuration = 0.1f)
  )

  val WyvernAnimationDefinition: AnimationDefinition = AnimationDefinition(
    frameWidth = 256,
    frameHeight = 256,
    stanceFrames = FramesDefinition(start = 0, count = 8, frameDuration = 0.1f),
    walkFrames = FramesDefinition(start = 8, count = 8, frameDuration = 0.08f),
    attackFrames =
      FramesDefinition(start = 16, count = 8, frameDuration = 0.1f),
    deathFrames = FramesDefinition(start = 48, count = 8, frameDuration = 0.1f)
  )

}
