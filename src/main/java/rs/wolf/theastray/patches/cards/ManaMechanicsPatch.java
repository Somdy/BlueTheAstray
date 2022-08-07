package rs.wolf.theastray.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.actions.utilities.TriggerStorageAction;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.GlobalManaMst;

public class ManaMechanicsPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class PlayerUseCardPatch {
        @SpireInsertPatch(locator = StorageLocator.class)
        public static void UseMana(AbstractPlayer _inst, AbstractCard c, AbstractMonster m, int e) {
            if (c instanceof AstrayCard && ((AstrayCard) c).isMagical()) {
                int manaCost = ((AstrayCard) c).getManaOnUse();
                GlobalManaMst.UseMana(manaCost);
            }
        }
        private static class ManaLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(EnergyManager.class, "use");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
        @SpireInsertPatch(locator = StorageLocator.class)
        public static void StorageCheck(AbstractPlayer _inst, AbstractCard c, AbstractMonster m, int e) {
            if (c.hasTag(TACardEnums.STORAGE)) {
                boolean energy = EnergyPanel.getCurrentEnergy() <= 0;
                boolean mana = GlobalManaMst.HasMana();
                LMSK.AddToBot(new TriggerStorageAction(energy, mana));
            }
        }
        private static class StorageLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "canUseAnyCard");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}