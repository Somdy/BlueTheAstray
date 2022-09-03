package rs.wolf.theastray.ui.campfire;

import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import rs.lazymankits.utils.LMSK;
import rs.lazymankits.vfx.utils.SwapUpgBranchEffect;
import rs.wolf.theastray.utils.TAImageMst;
import rs.wolf.theastray.utils.TAUtils;

public class EnlightenOption extends AbstractCampfireOption implements TAUtils {
    private static final UIStrings uiStrings = TAUtils.UIStrings("EnlightenOption");
    public static final String[] TEXT = uiStrings.TEXT;
    
    public EnlightenOption(boolean active) {
        label = TEXT[0];
        description = TEXT[active ? 1 : 2];
        usable = active;
        img = TAImageMst.CAMPFIRE_ENLIGHTEN_BTN;
    }
    
    @Override
    public void useOption() {
        if (usable) {
            effectToList(new SwapUpgBranchEffect(LMSK.Player().masterDeck.group).setMsg(TEXT[3]));
        }
    }
}