package rs.wolf.theastray.patches.dungeons;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import rs.wolf.theastray.core.Leader;

public class OnNextRoomTransitionPatch {
    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class TransitionPatch {
        @SpireInsertPatch(rloc = 2174 - 2126)
        public static void Insert() {
            Leader.OnNextRoomTransition();
        }
    }
}
