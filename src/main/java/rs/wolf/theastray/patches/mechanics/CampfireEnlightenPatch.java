package rs.wolf.theastray.patches.mechanics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.characters.BlueTheAstray;
import rs.wolf.theastray.ui.campfire.EnlightenOption;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CampfireEnlightenPatch {
    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class AddOptionToCampfireUIPatch {
        @SpireInsertPatch(rloc = 5)
        public static void Insert(CampfireUI _inst) throws Exception {
            AbstractPlayer p = LMSK.Player();
            if (!(p instanceof BlueTheAstray)) return;
            boolean active = p.masterDeck.group.stream()
                    .anyMatch(c -> c instanceof AstrayCard && ((AstrayCard) c).swappable());
            Field btns = CampfireUI.class.getDeclaredField("buttons");
            btns.setAccessible(true);
            ArrayList<AbstractCampfireOption> buttons = (ArrayList<AbstractCampfireOption>) btns.get(_inst);
            buttons.add(new EnlightenOption(active));
        }
    }
}