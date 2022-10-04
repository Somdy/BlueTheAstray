package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;

public class B30 extends AstrayProCard {
    public B30() {
        super(30, 1, CardTarget.ENEMY);
        setDamageValue(14, true);
        setMagicValue(2, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ModifyManaAction(-getManaOnUse()));
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(ApplyPower(t, s, new WeakPower(t, magicNumber, false)));
    }
    
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && hasEnoughMana();
    }
    
    @Override
    protected int selfModifyManaOnUse(int manaCost) {
        return 2;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(4);
    }
}