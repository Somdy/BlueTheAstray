package rs.wolf.theastray.patches.mechanics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.characters.BlueTheAstray;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.ui.campfire.EnlightenOption;
import rs.wolf.theastray.utils.GlobalIDMst;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CampfireUIPatch {
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
    
//    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
//    public static class ReplaceSmithOptionPatch {
//        @SpirePostfixPatch
//        public static void Postfix(CampfireUI _inst) throws Exception {
//            AbstractPlayer p = LMSK.Player();
//            if (p.hasRelic(GlobalIDMst.RelicID("旅行者套装"))) {
//                Field btns = CampfireUI.class.getDeclaredField("buttons");
//                btns.setAccessible(true);
//                ArrayList<AbstractCampfireOption> buttons = (ArrayList<AbstractCampfireOption>) btns.get(_inst);
//                int index = buttons.stream().filter(o -> o instanceof SmithOption)
//                        .findFirst()
//                        .map(buttons::indexOf)
//                        .orElse(-1);
//                
//            }
//        }
//    }
}