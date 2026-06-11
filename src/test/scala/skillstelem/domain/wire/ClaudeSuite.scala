package skillstelem.domain.wire

import io.circe.parser.decode

import skillstelem.domain.wire.Claude.*

class ClaudeSuite extends munit.FunSuite {

   test("Decode PostToolUse") {
      val raw = """
        {
            "session_id": "abc123",
            "transcript_path": "/Users/.../.claude/projects/.../00893aaf-19fa-41d2-8238-13269b9b3ca0.jsonl",
            "cwd": "/Users/...",
            "permission_mode": "default",
            "hook_event_name": "PostToolUse",
            "tool_name": "Skill",
            "tool_input": {
                "skill": "review"
            },
            "tool_response": {
                "filePath": "/path/to/file.txt",
                "success": true
            },
            "tool_use_id": "toolu_01ABC123...",
            "duration_ms": 12
        }
        """

      decode[ClaudeHook](raw) match {
         case Right(ptu: PostToolUse) => {
            assertEquals(ptu.toolName, "Skill")
            assertEquals(ptu.toolInput.skill, "review")
         }
         case Right(oth) => fail(s"Expected PostToolUse, got $oth")
         case Left(err)  => fail(s"Decode failed: $err")
      }

   }

   test("Decode UserPromptExpansion") {
      val raw = """
        {
           "session_id": "abc123",
           "transcript_path": "/Users/.../00893aaf.jsonl",
           "cwd": "/Users/...",
           "permission_mode": "default",
           "hook_event_name": "UserPromptExpansion",
           "expansion_type": "slash_command",
           "command_name": "example-skill",
           "command_args": "arg1 arg2",
           "command_source": "plugin",
           "prompt": "/example-skill arg1 arg2"
        }
        """

      decode[ClaudeHook](raw) match {
         case Right(upe: UserPromptExpansion) => {
            assertEquals(upe.expansionType, "slash_command")
            assertEquals(upe.commandName, "example-skill")
         }
         case Right(oth) => fail(s"Expected UserPromptExpansion, got $oth")
         case Left(err)  => fail(s"Decode failed: $err")
      }
   }
}
