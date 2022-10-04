package rs.wolf.theastray.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.core.Leader;

public class PatchToStopStsLibUnreasonablyCallingNonEssentialMethodAndPrintingSPAMLogsWhenViewingUpgrades {
    @SpirePatch(cls = "com.evacipated.cardcrawl.mod.stslib.patches.FlavorText$FlavorIntoCardStrings",
            method = "postfix", optional = true)
    public static class StopCallingGetCardStringMethodPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractCard __instance) {
            if (__instance instanceof AstrayCard) {
//                Leader.Log("WHY DON'T U ASK IF IM USING FLAVOR TEXT BEFORE MADLY CALLING METHOD [GET CARD STRINGS]");
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}