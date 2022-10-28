package rs.wolf.theastray.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.data.DataMst;

public abstract class AstrayCurseCard extends AstrayCard {
    public AstrayCurseCard(int index, int cost, CardTarget target) {
        super(DataMst.GetCardData(index), cost, CardColor.CURSE, target);
    }
    
    public AstrayCurseCard(int index) {
        this(index, -2, CardTarget.NONE);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {}
    
    @Override
    public void selfUpgrade() {}
}