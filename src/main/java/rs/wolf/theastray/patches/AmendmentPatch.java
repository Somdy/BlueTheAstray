package rs.wolf.theastray.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.powers.unique.AmendmentPower;

public class AmendmentPatch {
//    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardActionPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor(){
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("shuffleBackIntoDrawPile")) {
                        f.replace("$_=$proceed($$)||" + UseCardActionPatch.class.getName() + ".BackToDrawPile($0);");
                    }
                }
            };
        }
        public static boolean BackToDrawPile(AbstractCard card) {
            if (LMSK.Player().hasPower(AmendmentPower.ID)) {
                return AmendmentPower.BackToDrawPile(card);
            }
            return false;
        }
    }
}