package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.interfaces.DeMagicSensitiveGear;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class EvilMouthPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("EvilMouthPower");
    private List<AbstractCard> magics;
    
    public EvilMouthPower(int cards, int times) {
        super(ID, "evilmouth", PowerType.BUFF, AbstractDungeon.player);
        setValues(cards, times);
        preloadString(s -> {
            setAmtValue(0, cards);
            setAmtValue(1, times);
        });
        updateDescription();
        magics = new ArrayList<>();
    }
    
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (amount > 0 && TAUtils.IsMagical(card) && !magics.contains(card)) {
            flash();
            AbstractMonster m = null;
            if (action.target instanceof AbstractMonster)
                m = (AbstractMonster) action.target;
            AbstractCard copy = card.makeSameInstanceOf();
            magics.add(copy);
            copy.current_x = card.current_x;
            copy.current_y = card.current_y;
            if (m != null) {
                copy.target_x = m.hb.x - m.hb.width / 2F;
                copy.target_y = m.hb.cY;
                copy.calculateCardDamage(m);
            } else {
                copy.target_x = Settings.WIDTH / 2F;
                copy.target_y = Settings.HEIGHT / 2F;
            }
            copy.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, m, card.energyOnUse, 
                    true, true), false);
            amount--;
            if (amount == 0) {
                addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new EvilMouthPower(amount, extraAmt);
    }
}
