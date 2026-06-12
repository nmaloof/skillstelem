package skillstelem.domain

import io.circe.parser.decode

import skillstelem.domain.wire.WireSamples

class HookSuite extends munit.FunSuite {

   test("Claude Wire into Domain") {
      val rawPtu = WireSamples.Claude.ptuSample
      val rawUpe = WireSamples.Claude.upeSample

      decode[AcceptedHooks](rawPtu) match {
         case Right(value) => {
            val ptu = PostToolUse.fromAccepted(value)
            assertEquals(ptu.get.skillName, "review")
         }
         case Left(err) => fail(s"Decode failed: $err")
      }

      decode[AcceptedHooks](rawUpe) match {
         case Right(value) => {
            val ptu = PostToolUse.fromAccepted(value)
            assertEquals(ptu.get.skillName, "example-skill")
         }
         case Left(err) => fail(s"Decode failed: $err")
      }
   }

   test("Cursor Wire into Domain") {

      val rawPtu = WireSamples.Cursor.rawPtu
      decode[AcceptedHooks](rawPtu) match {
         case Right(value) => {
            val ptu = PostToolUse.fromAccepted(value)
            assertEquals(ptu, None)
         }
         case Left(err) => fail(s"Decode failed: $err")
      }

      val rawPtuSkill = WireSamples.Cursor.rawPtuSkill
      decode[AcceptedHooks](rawPtuSkill) match {
         case Right(value) => {
            val ptu = PostToolUse.fromAccepted(value)
            assertEquals(ptu.get.skillName, "sample-skill")
         }
         case Left(err) => fail(s"Decode failed: $err")
      }
   }

}
