package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.cards.AstrayProCard;

public class B24 extends AstrayProCard {
    public B24() {
        super(24, 1, CardTarget.ALL_ENEMY);
        setDamageValue(5, true);
        setMagicValue(1, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            int multi = magicNumber;
            int mSize = getAllLivingMstrs().size();
            multi *= mSize;
            for (int i = 0; i < multi; i++) {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    addToTop(DamageAction(m, s, AbstractGameAction.AttackEffect.FIRE));
                }
            }
            updateDescription(DESCRIPTION);
        });
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        int multi = magicNumber;
        int mSize = getAllLivingMstrs().size();
        multi *= mSize;
        updateDescription(DESCRIPTION + String.format(MSG[0], damage, multi));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(3);
    }
}
