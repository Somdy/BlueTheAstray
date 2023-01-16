package rs.wolf.theastray.cards.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.cards.AstrayColorlessCard;

public class C87 extends AstrayColorlessCard {
    public C87() {
        super(87, -2, CardTarget.NONE);
        isEthereal = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {}
    
    @Override
    public void selfUpgrade() {}
}