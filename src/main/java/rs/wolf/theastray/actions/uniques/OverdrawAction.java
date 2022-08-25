package rs.wolf.theastray.actions.uniques;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.powers.unique.MagicalBodyPower;
import rs.wolf.theastray.utils.TAUtils;

public abstract class OverdrawAction extends AstrayGameAction {
    protected AbstractGameAction followup;
    public static class FullHandDraw extends OverdrawAction {
        private boolean shuffled;
        
        private FullHandDraw(int excess, AbstractGameAction followup) {
            this.amount = excess;
            this.followup = followup;
            shuffled = false;
            actionType = ActionType.DRAW;
            duration = Settings.ACTION_DUR_XFAST;
        }
    
        @Override
        public void update() {
            int drawSize = cpr().drawPile.size();
            int discardSize = cpr().discardPile.size();
            if (amount <= 0 || (drawSize + discardSize == 0)) {
                isDone = true;
                return;
            }
            if (!shuffled) {
                if (amount > drawSize) {
                    int tmp = amount - drawSize;
                    addToTop(new FullHandDraw(tmp, followup));
                    addToTop(new EmptyDeckShuffleAction());
                    if (drawSize != 0) {
                        addToTop(new FullHandDraw(drawSize, null));
                    }
                    isDone = true;
                    return;
                }
                shuffled = true;
            }
            duration -= Gdx.graphics.getDeltaTime();
            if (amount > 0 && duration <= 0F) {
                duration = Settings.ACTION_DUR_XFAST;
                amount--;
                overdraw();
                if (amount == 0) endWithFollowup();
            }
        }
    }
    
    public static class HalfHandDraw extends OverdrawAction {
        private boolean checked;
        private HalfHandDraw(int excess, AbstractGameAction followup) {
            this.amount = excess;
            this.followup = followup;
            checked = false;
            actionType = ActionType.DRAW;
            duration = Settings.ACTION_DUR_XFAST;
        }
    
        @Override
        public void update() {
            if (!checked) {
                boolean drawing = AbstractDungeon.actionManager.actions.stream().anyMatch(a -> a instanceof DrawCardAction);
                if (drawing) {
                    int actionSize = AbstractDungeon.actionManager.actions.size();
                    for (int i = actionSize - 1; i >= 0; i--) {
                        if (AbstractDungeon.actionManager.actions.get(i) instanceof DrawCardAction) {
                            AbstractDungeon.actionManager.actions.add(i + 1, new HalfHandDraw(amount, followup));
                            isDone = true;
                            return;
                        }
                    }
                }
                checked = true;
            }
            int drawSize = cpr().drawPile.size();
            int discardSize = cpr().discardPile.size();
            if (amount <= 0 || (drawSize + discardSize == 0)) {
                isDone = true;
                return;
            }
            addToTop(new FullHandDraw(amount, followup));
            isDone = true;
        }
    }
    
    void endWithFollowup() {
        isDone = true;
        if (followup != null)
            addToTop(followup);
    }
    
    void overdraw() {
        if (!cpr().drawPile.isEmpty()) {
            CardCrawlGame.sound.playAV("CARD_DRAW_8", -0.12F, 0.55F);
            AbstractCard c = cpr().drawPile.getTopCard();
            DrawCardAction.drawnCards.add(c);
            c.current_x = CardGroup.DRAW_PILE_X;
            c.current_y = CardGroup.DRAW_PILE_Y;
            c.setAngle(0F, true);
            c.lighten(false);
            c.drawScale = 0.12F;
            c.targetDrawScale = 0.75F;
            c.triggerWhenDrawn();
            cpr().discardPile.moveToDiscardPile(c);
            cpr().drawPile.removeTopCard();
            for (AbstractPower p : cpr().powers) {
                p.onCardDraw(c);
                if (p instanceof MagicalBodyPower)
                    ((MagicalBodyPower) p).function(1);
            }
            for (AbstractRelic r : cpr().relics) {
                r.onCardDraw(c);
            }
            return;
        }
        endWithFollowup();
    }
    
    public static class OverdrawActionPatch {
        public static int originAmount;
        @SpirePatch(clz = DrawCardAction.class, method = SpirePatch.CONSTRUCTOR,
                paramtypez = {AbstractCreature.class, int.class, boolean.class})
        public static class DrawAmountPatch {
            @SpirePrefixPatch
            public static void Prefix(AbstractGameAction _inst) {
                if (_inst.amount > 0) originAmount = _inst.amount;
            }
        }
        @SpirePatch(clz = DrawCardAction.class, method = "update")
        public static class HandFullCheckPatch {
            @SpireInstrumentPatch
            public static ExprEditor FullHandInstrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("createHandIsFullDialog") && m.getLineNumber() < 113) {
                            TAUtils.Log("Hand Is Already Full.");
                            m.replace("if(" + OverdrawActionPatch.class.getName() + ".CanOverdraw($0)){"
                                    + OverdrawActionPatch.class.getName() + ".FullHandOverdraw(this.amount,this.followUpAction);" +
                                    "this.isDone=true;return;}else{$_ = $proceed($$);}");
                        }
                    }
                };
            }
            @SpireInstrumentPatch
            public static ExprEditor HalfHandInstrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("createHandIsFullDialog") && m.getLineNumber() > 113) {
                            TAUtils.Log("Hand Is Half Full.");
                            m.replace("if(" + OverdrawActionPatch.class.getName() + ".CanOverdraw($0)){"
                                    + OverdrawActionPatch.class.getName() + ".HalfHandOverdraw(" +
                                    Math.class.getName() + ".abs(handSizeAndDraw),this.followUpAction);"
                                    + OverdrawActionPatch.class.getName() + ".originAmount=0;" + "}else{$_ = $proceed($$);}");
                        }
                    }
                };
            }
        }
        
        public static void FullHandOverdraw(int excess, AbstractGameAction action) {
            TAUtils.Log("full hand overdrawn amount: [" + excess + "]");
            LMSK.AddToTop(new FullHandDraw(excess, action));
        }
        
        public static void HalfHandOverdraw(int excess, AbstractGameAction action) {
            TAUtils.Log("half hand overdrawn amount: [" + excess + "]");
            LMSK.AddToTop(new HalfHandDraw(excess, action));
        }
        
        public static boolean CanOverdraw(@NotNull AbstractPlayer p) {
            return p.powers.stream().anyMatch(po -> po instanceof MagicalBodyPower);
        }
    }
}