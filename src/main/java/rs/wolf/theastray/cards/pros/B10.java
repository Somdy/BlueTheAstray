package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.wolf.theastray.cards.AstrayProCard;

public class B10 extends AstrayProCard {
    public B10() {
        super(10, 1, CardTarget.SELF);
        setMagicValue(2, true);
        exhaust = true;
        addTags(CardTags.HEALING);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        int regen = timesUpgraded + magicNumber;
        addToBot(ApplyPower(s, s, new RegenPower(s, regen)));
        updateDescription(DESCRIPTION);
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        int regen = timesUpgraded + magicNumber;
        updateDescription(DESCRIPTION + String.format(MSG[0], regen));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBaseCost(0);
    }
    
    @Override
    public boolean canUpgrade() {
        return outOfDungeon() || currRoom().phase != AbstractRoom.RoomPhase.COMBAT;
    }
}