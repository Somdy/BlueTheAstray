package rs.wolf.theastray.cards;

import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.data.DataMst;
import rs.wolf.theastray.patches.TACardEnums;

public abstract class AstrayExtCard extends AstrayCard {
    public AstrayExtCard(int index, int cost, int extNeed, CardTarget target) {
        super(DataMst.Get(index), cost, TACardEnums.TAE_CardColor, target);
        setExtension(true, extNeed);
    }
    
    public AstrayExtCard(String localname, int cost, int extNeed, CardTarget target) {
        super(DataMst.Get(localname), cost, TACardEnums.TAE_CardColor, target);
        setExtension(true, extNeed);
    }
}