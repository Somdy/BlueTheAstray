package rs.wolf.theastray.cards;

import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.data.DataMst;

public abstract class AstrayColorlessCard extends AstrayCard {
    public AstrayColorlessCard(int index, int cost, CardTarget target) {
        super(DataMst.GetCardData(index), cost, CardColor.COLORLESS, target);
    }
}