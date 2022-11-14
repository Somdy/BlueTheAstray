package rs.wolf.theastray.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.ui.campfire.TravelerKitSmithOption;
import rs.wolf.theastray.utils.GlobalIDMst;
import rs.wolf.theastray.utils.TAUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Relic7 extends AstrayRelic {
    private static final float HP_THRESHOLD = 0.75F;
    private static boolean onPulse;
    
    public Relic7() {
        super(7);
    }
    
    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], SciPercent(HP_THRESHOLD));
    }
    
    @Override
    public void onEnterRestRoom() {
        onPulse = cpr().currentHealth > cpr().maxHealth * HP_THRESHOLD;
    }
    
    @Override
    public void update() {
        super.update();
        if (TAUtils.RoomAvailable()) {
            if (cpr().currentHealth > cpr().maxHealth * HP_THRESHOLD) {
                beginLongPulse();
            } else {
                stopPulse();
            }
        }
    }
    
    @SpirePatch(clz = CampfireSmithEffect.class, method = "update")
    public static class ReopenSmithPatch {
        @SpirePostfixPatch
        public static void Postfix(CampfireSmithEffect _inst) throws Exception {
            if (_inst.isDone && _inst.duration < 0F) {
                AbstractPlayer p = LMSK.Player();
                if (UsingTravellerKit() && CampfireUI.hidden) {
                    Leader.PatchLog("Try reopening smith option");
                    AbstractRoom currRoom = AbstractDungeon.getCurrRoom();
                    if (currRoom instanceof RestRoom) {
                        Field btns = CampfireUI.class.getDeclaredField("buttons");
                        btns.setAccessible(true);
                        ArrayList<AbstractCampfireOption> buttons = (ArrayList<AbstractCampfireOption>) btns.get(((RestRoom) currRoom).campfireUI);
                        buttons.forEach(o -> o.usable = false);
                        int index = buttons.stream().filter(o -> o instanceof SmithOption)
                                .findFirst()
                                .map(buttons::indexOf)
                                .orElse(-1);
                        if (index < 0) {
                            Leader.PatchLog("Unable to find smith option");
                        } else {
                            Leader.PatchLog("Locating smith option at [" + index + "]");
                            CampfireUI.hidden = false;
                            ((RestRoom) currRoom).campfireUI.somethingSelected = false;
                            buttons.set(index, new TravelerKitSmithOption(p.masterDeck.getUpgradableCards().size() > 0));
                        }
                    }
                }
            }
        }
        public static boolean UsingTravellerKit() {
            return LMSK.Player().hasRelic(GlobalIDMst.RelicID("旅行者套装")) && onPulse;
        }
    }
}
