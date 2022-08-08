package rs.wolf.theastray.data;

import com.megacrit.cardcrawl.cards.AbstractCard;
import org.jetbrains.annotations.NotNull;
import rs.wolf.theastray.utils.TAUtils;

public class CardData {
    private final String ID;
    private final String localname;
    private final AbstractCard.CardType type;
    private final AbstractCard.CardRarity rarity;
    
    private CardData(String ID, String localname, AbstractCard.CardType type, AbstractCard.CardRarity rarity) {
        this.ID = ID;
        this.localname = localname;
        this.type = type;
        this.rarity = rarity;
    }
    
    public String getCardID() {
        return TAUtils.MakeID(ID);
    }
    
    public String getInternalID() {
        return ID;
    }
    
    public String getLocalname() {
        return localname;
    }
    
    public AbstractCard.CardType getType() {
        return type;
    }
    
    public AbstractCard.CardRarity getRarity() {
        return rarity;
    }
    
    @NotNull
    public static CardData Format(@NotNull LocalDataUnit unit) {
        CardData data = new CardData(unit.id, unit.localname, unit.getType(), unit.getRarity());
        return data;
    }
    
    @NotNull
    public static CardData MockingData() {
        return new CardData("MISSING", "MISSING", AbstractCard.CardType.SKILL, AbstractCard.CardRarity.SPECIAL);
    }
}