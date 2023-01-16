package rs.wolf.theastray.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import rs.wolf.theastray.core.Leader;

public class DefeatHeartPatch {
    @SpirePatch2(clz = CorruptHeart.class, method = "die")
    public static class MarkHeartDefeatedPatch {
        @SpireInsertPatch(rloc = 3)
        public static void Insert(AbstractMonster __instance) {
            if (!Leader.DEFEATED_HEART && AbstractDungeon.player.chosenClass == TACardEnums.BlueTheAstray) {
                Leader.DEFEATED_HEART = true;
                Leader.SaveConfig();
                BaseMod.playerPortraitMap.put(TACardEnums.BlueTheAstray, Leader.GetPortrait());
            }
        }
    }
}