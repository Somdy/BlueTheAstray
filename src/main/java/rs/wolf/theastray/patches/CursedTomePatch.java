package rs.wolf.theastray.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.events.city.CursedTome;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.relics.Relic4;
import rs.wolf.theastray.utils.GlobalIDMst;

import java.util.ArrayList;

public class CursedTomePatch {
    @SpirePatch2(clz = CursedTome.class, method = "randomBook")
    public static class AddExtraBookPatch {
        @SpireInsertPatch(rloc = 1, localvars = {"possibleBooks"})
        public static void Insert(ArrayList<AbstractRelic> possibleBooks) {
            if (!LMSK.Player().hasRelic(GlobalIDMst.RelicID(4)))
                possibleBooks.add(new Relic4());
        }
    }
}
