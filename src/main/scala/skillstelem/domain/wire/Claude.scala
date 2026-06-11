package skillstelem.domain.wire

import io.circe.derivation.{Configuration, ConfiguredDecoder}

object Claude {

   given Configuration = Configuration.default.withSnakeCaseMemberNames.withDiscriminator("hook_event_name")

   sealed trait ClaudeHook derives ConfiguredDecoder

   case class ToolInput(skill: String) derives ConfiguredDecoder

   case class PostToolUse(
       sessionId: String,
       cwd: String,
       hookEventName: String,
       toolName: String,
       toolInput: ToolInput
   ) extends ClaudeHook

   case class UserPromptExpansion(
       sessionId: String,
       cwd: String,
       hookEventName: String,
       expansionType: String,
       commandName: String,
       commandSource: String
   ) extends ClaudeHook
}
