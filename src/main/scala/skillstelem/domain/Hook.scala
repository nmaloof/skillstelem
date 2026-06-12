package skillstelem.domain

import io.circe.Decoder

import skillstelem.domain.wire.{Claude as WClaude, Cursor as WCursor}

sealed trait Hook

enum AcceptedHooks extends Hook {
   case ClaudeHook(hook: WClaude.ClaudeHook)
   case CursorHook(hook: WCursor.CursorHook)
}

object AcceptedHooks {
   given Decoder[AcceptedHooks] = Decoder[WClaude.ClaudeHook]
      .map(ClaudeHook(_))
      .or(Decoder[WCursor.CursorHook].map(CursorHook(_)))
}

enum Source(val src: String) {
   case Claude extends Source("claude")
   case Cursor extends Source("cursor")
}

case class PostToolUse(
    source: Source,
    sessionId: String,
    skillName: String
)

object PostToolUse {
   def fromAccepted(accepted: AcceptedHooks): Option[PostToolUse] = accepted match {
      case AcceptedHooks.ClaudeHook(hook) =>
         hook match {
            case WClaude.PostToolUse(sessionId, cwd, hookEventName, toolName, toolInput) =>
               Some(PostToolUse(Source.Claude, sessionId, toolInput.skill))
            case WClaude.UserPromptExpansion(sessionId, cwd, hookEventName, expansionType, commandName, commandSource) =>
               Some(PostToolUse(Source.Claude, sessionId, commandName))
         }
      case AcceptedHooks.CursorHook(hook) =>
         hook match {
            case WCursor.PostToolUse(conversationId, hookEventName, toolName, toolInput) =>
               toolInput.skillName.map(PostToolUse(Source.Cursor, conversationId, _))
         }

   }
}
