package rs.wolf.theastray.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.utils.GlobalIDMst;

import java.util.ArrayList;
import java.util.List;

public class Relic4 extends AstrayRelic {
    private boolean activated;
    private final List<AbstractCard> cards = new ArrayList<>();
    
    public Relic4() {
        super(4);
        activated = false;
    }
    
    @Override
    public void atBattleStart() {
        activated = true;
        grayscale = false;
        usedUp = false;
        beginLongPulse();
        addToBot(new DrawCardAction(cpr(), 2));
    }
    
    @Override
    public void onPlayerEndTurn() {
        activated = false;
        grayscale = true;
        usedUp = true;
        cards.clear();
        stopPulse();
    }
    
    @Override
    public void onEquip() {
        int decrease = cpr().maxHealth / 2;
        cpr().decreaseMaxHealth(decrease);
    }
    
    public static boolean Activated() {
        AbstractRelic r = LMSK.Player().getRelic(GlobalIDMst.RelicID(4));
        if (r instanceof Relic4) {
            return ((Relic4) r).activated;
        }
        return false;
    }
    
    public static void Add(AbstractCard card) {
        if (!Activated()) return;
        AbstractRelic r = LMSK.Player().getRelic(GlobalIDMst.RelicID(4));
        if (r instanceof Relic4 && !((Relic4) r).cards.contains(card)) {
            ((Relic4) r).cards.add(card);
        }
    }
    
    public static void Remove(AbstractCard card) {
        if (!Activated()) return;
        AbstractRelic r = LMSK.Player().getRelic(GlobalIDMst.RelicID(4));
        if (r instanceof Relic4) {
            ((Relic4) r).cards.remove(card);
        }
    }
    
    public static boolean Contains(AbstractCard card) {
        if (!Activated()) return false;
        AbstractRelic r = LMSK.Player().getRelic(GlobalIDMst.RelicID(4));
        if (r instanceof Relic4 && !((Relic4) r).cards.isEmpty()) {
            return ((Relic4) r).cards.contains(card);
        }
        return false;
    }
    
    public static void Func() {
        if (!Activated()) return;
        AbstractRelic r = LMSK.Player().getRelic(GlobalIDMst.RelicID(4));
        if (r != null)
            r.flash();
    }
    
    @SpirePatch2(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class HasEnoughEnergyPatch {
        @SpirePostfixPatch
        public static boolean Postfix(AbstractCard __instance, boolean __result) {
            if (Relic4.Activated()) {
                int cardCost = __instance.freeToPlay() || __instance.isInAutoplay ? 0 : __instance.costForTurn;
                int currentCost = EnergyPanel.totalCount;
                __result = cardCost <= currentCost;
                if (__result) {
                    Relic4.Add(__instance);
                } else {
                    Relic4.Remove(__instance);
                }
            }
            return __result;
        }
    }
    
    @SpirePatch2(clz = AbstractPlayer.class, method = "useCard")
    public static class UseEnergyPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor(){
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("use") && m.getClassName().equals(EnergyManager.class.getName())) {
                        m.replace("{if(!" + Relic4.class.getName() + ".Contains(c))$_=$proceed($$);" +
                                "else{" + Relic4.class.getName() + ".Func();}}");
                    }
                }
            };
        }
    }
}