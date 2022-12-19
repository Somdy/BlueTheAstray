package rs.wolf.theastray.patches.mechanics;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.patches.TACardEnums;

public class AstrayReturnToHandPatch {
    @SpirePatch2(clz = UseCardAction.class, method = "update")
    public static class ReturnToHandPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor(){
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ("moveToDiscardPile".equals(m.getMethodName())) {
                        m.replace("{if(" + ReturnToHandPatch.class.getName() + ".ReturnToHand($1)){$0.moveToHand($1);" +
                                AbstractDungeon.class.getName() + ".player.onCardDrawOrDiscard();}else{$_=$proceed($$);}}");
                    }
                }
            };
        }
        public static boolean ReturnToHand(AbstractCard card) {
            return card.hasTag(TACardEnums.RETURN_TO_HAND) && LMSK.Player().hand.size() < BaseMod.MAX_HAND_SIZE;
        }
    }
}