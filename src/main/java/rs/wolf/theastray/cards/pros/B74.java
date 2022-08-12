package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.actions.common.DrawExptCardAction;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;

public class B74 extends AstrayProCard {
    public B74() {
        super(74, -2, CardTarget.SELF);
        setMagicValue(1, true);
        setExtraMagicValue(1, true);
        
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {}
    
    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (!upgraded) {
            if (cardRandomRng().randomBoolean())
                addToTop(new ModifyManaAction(magicNumber));
            else 
                addToTop(new GainEnergyAction(magicNumber));
        } else {
            addToTop(new ModifyManaAction(magicNumber));
            addToTop(new GainEnergyAction(magicNumber));
        }
        addToTop(new DrawExptCardAction(getExtraMagic(), c -> !(c instanceof B74)));
        addToTop(new DiscardSpecificCardAction(this));
    }
    
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}