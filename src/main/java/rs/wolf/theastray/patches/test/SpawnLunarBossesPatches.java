package rs.wolf.theastray.patches.test;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.monsters.BlueTheBoss;
import rs.wolf.theastray.relics.Relic11;
import rs.wolf.theastray.relics.RelicTest1;
import rs.wolf.theastray.utils.GlobalIDMst;

import java.util.function.UnaryOperator;

public class SpawnLunarBossesPatches {
    @SpirePatch2(clz = TheEnding.class, method = "initializeBoss")
    public static class ReplaceHeartInEnding {
        @SpirePostfixPatch
        public static void Postfix() {
            if (SpawnNewtForHeart()) {
                TheEnding.bossList.clear();
                TheEnding.bossList.add(BlueTheBoss.ID);
                TheEnding.bossList.add(BlueTheBoss.ID);
                TheEnding.bossList.add(BlueTheBoss.ID);
            } else {
                TheEnding.bossList.replaceAll(s -> {
                    if (BlueTheBoss.ID.equals(s))
                        return MonsterHelper.THE_HEART_ENC;
                    return s;
                });
            }
        }
    }
    
    @SpirePatch(clz = AbstractDungeon.class, method = "setBoss")
    public static class SetBossChecker {
        @SpirePrefixPatch
        public static void Prefix(AbstractDungeon _inst, @ByRef String[] k) {
            boolean spawn = SpawnNewtForHeart();
            if (BlueTheBoss.ID.equals(k[0]) && !spawn) {
                Leader.Log("Not allowing BlueTheBoss, returning Heart");
                k[0] = MonsterHelper.THE_HEART_ENC;
            }
        }
    }
    
//    @SpirePatch(clz = MonsterHelper.class, method = "getEncounter")
    public static class SpawnFinalBossPatch {
        @SpirePrefixPatch
        public static SpireReturn<MonsterGroup> GetLunarBoss(String key) {
            if (BlueTheBoss.ID.equals(key)) {
                return SpireReturn.Return(new MonsterGroup(new BlueTheBoss(0F, 0F)));
            }
            return SpireReturn.Continue();
        }
    }
    
    private static boolean SpawnNewtForHeart() {
        boolean hasTest = LMSK.Player().relics.stream().anyMatch(r -> r instanceof RelicTest1 && r.counter == -1);
        boolean hasEleven = Leader.SaveData.getBool("eleven");
        return hasTest || hasEleven;
    }
}