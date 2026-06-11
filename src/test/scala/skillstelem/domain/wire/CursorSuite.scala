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

}
