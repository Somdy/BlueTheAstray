package rs.wolf.theastray.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.core.Leader;

public class CreatureDamagePatch {
    @SpirePatches2({
            @SpirePatch2(clz = AbstractPlayer.class, method = "damage"),
            @SpirePatch2(clz = AbstractMonster.class, method = "damage")
    })
    public static class PlayerDamagePatch {
        @SpirePrefixPatch
        public static void Pre(AbstractCreature __instance, DamageInfo info) {
            Leader.PreOnCreatureDamage(__instance, info);
        }
        @SpirePostfixPatch
        public static void Post(AbstractCreature __instance, DamageInfo info) {
            Leader.PostOnCreatureDamage(__instance, info);
        }
    }
}