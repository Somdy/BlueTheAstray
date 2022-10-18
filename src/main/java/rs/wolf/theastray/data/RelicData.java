package rs.wolf.theastray.data;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import rs.wolf.theastray.utils.TAUtils;

public class RelicData {
    private final String ID;
    private final String localname;
    private final AbstractRelic.RelicTier tier;
    private final AbstractRelic.LandingSound sfx;
    
    private RelicData(String id, String localname, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        ID = id;
        this.localname = localname;
        this.tier = tier;
        this.sfx = sfx;
    }
    
    public String getRelicID() {
        return TAUtils.MakeID(ID);
    }
    
    public String getInternalID() {
        return ID;
    }
    
    public String getLocalname() {
        return localname;
    }
    
    public AbstractRelic.RelicTier getTier() {
        return tier;
    }
    
    public AbstractRelic.LandingSound getSfx() {
        return sfx;
    }
    
    @NotNull
    public static RelicData Format(@NotNull LocalRelicDataUnit unit) {
        RelicData data = new RelicData(unit.id, unit.localname, unit.getTier(), unit.getSFX());
        return data;
    }
    
    @NotNull
    public static RelicData MockingData(String ID) {
        return new RelicData(ID, ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }
}