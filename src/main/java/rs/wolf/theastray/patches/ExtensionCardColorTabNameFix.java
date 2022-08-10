package rs.wolf.theastray.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import javassist.CtBehavior;
import rs.wolf.theastray.utils.TAUtils;

public class ExtensionCardColorTabNameFix {
    @SpirePatch(optional = true,
            cls = "basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix$Render",
            method = "Insert")
    public static class TabBarNameFix {
        private static final UIStrings uiStrings = TAUtils.UIStrings("ExtensionCardTab");
        
        @SpireInsertPatch(locator = TabNameLocator.class, localvars = {"tabName", "color"})
        public static void InsertFix(ColorTabBar _inst, SpriteBatch sb, float y, ColorTabBar.CurrentTab curTab,
                                     @ByRef String[] tabName, Color color) {
            if (tabName[0].equalsIgnoreCase("theastray_ex_color"))
                tabName[0] = uiStrings.TEXT[0];
        }
        
        private static class TabNameLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }
}