package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E102 extends AstrayExtCard {
    public E102() {
        super(102, 0, 6, CardTarget.NONE);
        setMagicValue(4, true);
        setMagicalDerivative(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (EnergyPanel.getCurrentEnergy() <= 0) {
            addToBot(new DrawCardAction(s, magicNumber));
        }
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(1);
    }
    
    @Override
    public void triggerOnGlowCheck() {
        glowColor = EnergyPanel.getCurrentEnergy() <= 0 ? GOLD_BORDER_GLOW_COLOR.cpy() : BLUE_BORDER_GLOW_COLOR.cpy();
    }
}