package skillstelem.domain.wire

import io.circe.parser.decode

import skillstelem.domain.wire.Cursor.*

class CursorSuite extends munit.FunSuite {

   test("Decode PostToolUse") {
      val raw = """
         {
            "conversation_id": "abc1234",
            "hook_event_name": "postToolUse",
            "tool_name": "Read",
            "tool_input": {
               "file_path": "/home/test"
            },
            "duration": 14.755
         }
        """

      decode[CursorHook](raw) match {
         case Right(ptu: PostToolUse) => {
            assertEquals(ptu.toolName, "Read")
         }
         case Left(err) => fail(s"Decode failed: $err")
      }
   }

   test("Read Skill Name from ToolInput") {
      val toolInputs = List(
        ToolInput("/home/user1/.cursor/skills/sample-skill/SKILL.md"),
        ToolInput("/home/user1/docs/skills/skill-writer/skill.md"),
        ToolInput("/home/user1/.cursor/mcp-assigned-to-me/assigned.json")
      )

      assertEquals(
        toolInputs.map(_.skillName),
        List(Some("sample-skill"), Some("skill-writer"), None)
      )
   }

}
