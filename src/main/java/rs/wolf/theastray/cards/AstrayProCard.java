package rs.wolf.theastray.cards;

import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.data.DataMst;
import rs.wolf.theastray.patches.TACardEnums;

/**
 * 角色职业牌继承该抽象类
 */
public abstract class AstrayProCard extends AstrayCard {
    public AstrayProCard(int index, int cost, CardTarget target) {
        super(DataMst.GetCardData(index), cost, TACardEnums.TA_CardColor, target);
    }
    
    public AstrayProCard(String localname, int cost, CardTarget target) {
        super(DataMst.GetCardData(localname), cost, TACardEnums.TA_CardColor, target);
    }
}