package rs.wolf.theastray.cards.pros;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import rs.wolf.theastray.cards.AstrayProCard;

public class B41 extends AstrayProCard {
    public B41() {
        super(41, -1, CardTarget.ENEMY);
        setDamageValue(8, true);
        setPromosValue(2, true);
        setMagical(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new RemoveAllBlockAction(t, s));
        addToBot(new VFXAction(new ViolentAttackEffect(t.hb.cX, t.hb.cY, Color.ROYAL.cpy())));
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.NONE));
        updateDescription(DESCRIPTION);
    }
    
    @Override
    public void applyPowers() {
        setCurrPromos(getBasePromos() * EnergyPanel.getCurrentEnergy());
        super.applyPowers();
        setPromosModified(getBasePromos() != getPromos());
        updateDescription(UPDATED_DESC[0]);
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradePromos(2);
    }
}