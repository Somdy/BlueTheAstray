package rs.wolf.theastray.data;

import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.wolf.theastray.utils.TAUtils;

public class LocalCardDataUnit {
    protected String id;
    protected String localname;
    protected String type;
    protected String rarity;
    
    protected AbstractCard.CardType getType() {
        switch (type.toLowerCase()) {
            case "attack":
                return AbstractCard.CardType.ATTACK;
            case "skill":
                return AbstractCard.CardType.SKILL;
            case "power":
                return AbstractCard.CardType.POWER;
            case "status":
                return AbstractCard.CardType.STATUS;
            case "curse":
                return AbstractCard.CardType.CURSE;
            default:
                TAUtils.Log("UNDEFINED CARD TYPE: [" + type + "]");
                return AbstractCard.CardType.SKILL;
        }
    }
    
    protected AbstractCard.CardRarity getRarity() {
        switch (rarity.toLowerCase()) {
            case "basic":
                return AbstractCard.CardRarity.BASIC;
            case "common":
                return AbstractCard.CardRarity.COMMON;
            case "uncommon":
                return AbstractCard.CardRarity.UNCOMMON;
            case "rare":
                return AbstractCard.CardRarity.RARE;
            case "special":
                return AbstractCard.CardRarity.SPECIAL;
            case "curse":
                return AbstractCard.CardRarity.CURSE;
            default:
                TAUtils.Log("UNDEFINED CARD RARITY: [" + rarity + "]");
                return AbstractCard.CardRarity.SPECIAL;
        }
    }
}