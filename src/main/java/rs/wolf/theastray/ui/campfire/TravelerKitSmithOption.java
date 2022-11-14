package rs.wolf.theastray.ui.campfire;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import rs.wolf.theastray.utils.TAUtils;

public class TravelerKitSmithOption extends AbstractCampfireOption implements TAUtils {
    private static final UIStrings uiStrings = TAUtils.UIStrings("TravelerKitSmithOption");
    public static final String[] TEXT = uiStrings.TEXT;
    
    public TravelerKitSmithOption(boolean active) {
        label = TEXT[0];
        description = TEXT[active ? 1 : 2];
        usable = active;
        img = ImageMaster.CAMPFIRE_SMITH_BUTTON;
    }
    
    @Override
    public void useOption() {
        if (usable) {
            effectToList(new CampfireSmithEffect());
        }
    }
}