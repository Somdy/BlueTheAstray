package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.cards.AstrayProCard;

public class B24 extends AstrayProCard {
    public B24() {
        super(24, 1, CardTarget.ALL_ENEMY);
        setMagicValue(5, true);
        setExtraMagicValue(1, true);
        setMagical(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            int multi = 1;
            int mSize = getAllLivingMstrs().size() + 1;
            multi *= mSize;
            for (int i = 0; i < multi; i++) {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    addToTop(ApplyPower(m, s, burntPower(m, s, magicNumber)));
                }
            }
//            updateDescription(DESCRIPTION);
        });
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
//        int multi = getExtraMagic();
//        int mSize = getAllLivingMstrs().size();
//        multi *= mSize;
//        updateDescription(DESCRIPTION + String.format(MSG[0], magicNumber, multi));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(2);
    }
}