package rs.wolf.theastray.data;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import kotlin.math.MathKt;
import org.jetbrains.annotations.NotNull;
import rs.wolf.theastray.utils.TAUtils;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardData data = (CardData) o;
        return ID.equals(data.ID) && localname.equals(data.localname);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ID, localname);
    }
    
    @NotNull
    public static CardData Format(@NotNull LocalCardDataUnit unit) {
        return new CardData(unit.id, unit.localname, unit.getType(), unit.getRarity());
    }
    
    @NotNull
    public static CardData MockingData() {
        return new CardData("MISSING", "MISSING", AbstractCard.CardType.SKILL, AbstractCard.CardRarity.SPECIAL);
    }
}