package skillstelem.modules

import skillstelem.algebras.HookAlg
import skillstelem.local.LocalDev

class Algebras(val hook: HookAlg) {}

object Algebras {
   def makeLocal = new Algebras(LocalDev.HookPrinter())
}
