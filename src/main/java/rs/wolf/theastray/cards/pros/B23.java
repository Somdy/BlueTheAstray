package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;

public class B23 extends AstrayProCard {
    public B23() {
        super(23, 2, CardTarget.SELF_AND_ENEMY);
        setDamageValue(24, true);
        setMagicValue(1, true);
        setPromosValue(2, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new VFXAction(new SmallLaserEffect(t.hb.cX, t.hb.cY, s.hb.cX, s.hb.cY), 0.1F));
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.NONE));
        addToBot(ApplyPower(s, s, new MagicPower(s, -magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(4);
    }
}