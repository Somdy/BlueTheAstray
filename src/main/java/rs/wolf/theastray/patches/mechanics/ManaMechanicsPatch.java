package rs.wolf.theastray.patches.mechanics;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.actions.utilities.TriggerStorageAction;
import rs.wolf.theastray.characters.BlueTheAstray;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.ui.manalayout.ManaMst;
import rs.wolf.theastray.utils.GlobalManaMst;

public class ManaMechanicsPatch {
    @SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
    public static class ManaField {
        public static SpireField<ManaMst> manaMst = new SpireField<>(() -> null);
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CONSTRUCTOR)
    public static class InitPlayerManaPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst, String name, AbstractPlayer.PlayerClass clz) {
            if (_inst instanceof BlueTheAstray) return;
            GlobalManaMst.InitPlayerMana(_inst);
        }
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = "update")
    public static class UpdatePlayerManaPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst) {
            if (_inst instanceof BlueTheAstray) return;
            GlobalManaMst.GetPlayerManaMst(_inst).update();
        }
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class PreInitPlayerManaPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst) {
            if (_inst instanceof BlueTheAstray) return;
            GlobalManaMst.GetPlayerManaMst(_inst).initPreBattle();
        }
    }
    
    @SpirePatches({
            @SpirePatch(clz = Ironclad.class, method = "renderOrb"),
            @SpirePatch(clz = TheSilent.class, method = "renderOrb"),
            @SpirePatch(clz = Defect.class, method = "renderOrb"),
            @SpirePatch(clz = Watcher.class, method = "renderOrb")
    })
    public static class RenderOldPlayerManaPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst, SpriteBatch sb, boolean enabled, float current_x, float current_y) {
            if (_inst instanceof BlueTheAstray) return;
            GlobalManaMst.GetPlayerManaMst(_inst).render(sb);
        }
    }
    
    @SpirePatch(clz = CustomPlayer.class, method = "renderOrb")
    public static class RenderModPlayerManaPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst, SpriteBatch sb, boolean enabled, float current_x, float current_y) {
            if (_inst instanceof BlueTheAstray) return;
            GlobalManaMst.GetPlayerManaMst(_inst).render(sb);
        }
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class PlayerUseCardPatch {
        @SpireInsertPatch(locator = ManaLocator.class)
        public static void UseMana(AbstractPlayer _inst, AbstractCard c, AbstractMonster m, int e) {
            if (c instanceof AstrayCard && ((AstrayCard) c).isMagical()) {
                int manaCost = ((AstrayCard) c).getManaOnUse();
                if (manaCost > 0 && !c.isInAutoplay) 
                    GlobalManaMst.UseMana(manaCost);
            }
        }
        private static class ManaLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "cardInUse");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
        @SpireInsertPatch(locator = StorageLocator.class)
        public static void StorageCheck(AbstractPlayer _inst, AbstractCard c, AbstractMonster m, int e) {
            if (c.hasTag(TACardEnums.STORAGE)) {
                boolean energy = EnergyPanel.getCurrentEnergy() <= 0;
                boolean mana = !GlobalManaMst.HasMana();
                Leader.devLog("STORAGE MAY TRIGGER BY [" + c.name + "]");
                LMSK.AddToBot(new TriggerStorageAction(mana, energy, c));
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