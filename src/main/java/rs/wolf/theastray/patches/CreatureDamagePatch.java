package rs.wolf.theastray.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.core.Leader;

public class CreatureDamagePatch {
    @SpirePatch2(clz = AbstractPlayer.class, method = "damage")
    public static class PlayerDamagePatch {
        public static void Prefix(AbstractCreature __instance, DamageInfo info) {
            Leader.PreOnCreatureDamage(__instance, info);
        }
    }
}