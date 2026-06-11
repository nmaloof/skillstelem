package skillstelem.domain.wire

import io.circe.derivation.{Configuration, ConfiguredDecoder}

object Cursor {

   given Configuration = Configuration.default
        .withSnakeCaseMemberNames
        .withDiscriminator("hook_event_name")
        .withTransformConstructorNames(s => s.head.toLower +: s.tail)

   sealed trait CursorHook derives ConfiguredDecoder

   case class ToolInput(filePath: String) derives ConfiguredDecoder

   case class PostToolUse(
       conversationId: String,
       hookEventName: String,
       toolName: String,
       toolInput: ToolInput
   ) extends CursorHook
}
