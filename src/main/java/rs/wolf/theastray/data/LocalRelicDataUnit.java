package rs.wolf.theastray.data;

import com.megacrit.cardcrawl.relics.AbstractRelic;

public class LocalRelicDataUnit {
    protected String id;
    protected String localname;
    protected String tier;
    protected String sfx;
    
    public AbstractRelic.RelicTier getTier() {
        switch (tier.toLowerCase()) {
            case "common":
                return AbstractRelic.RelicTier.COMMON;
            case "uncommon":
                return AbstractRelic.RelicTier.UNCOMMON;
            case "rare":
                return AbstractRelic.RelicTier.RARE;
            case "boss":
                return AbstractRelic.RelicTier.BOSS;
            case "shop":
                return AbstractRelic.RelicTier.SHOP;
            case "starter":
                return AbstractRelic.RelicTier.STARTER;
            default:
                return AbstractRelic.RelicTier.SPECIAL;
        }
    }
    
    public AbstractRelic.LandingSound getSFX() {
        switch (sfx.toLowerCase()) {
            case "clink":
                return AbstractRelic.LandingSound.CLINK;
            case "heavy":
                return AbstractRelic.LandingSound.HEAVY;
            case "solid":
                return AbstractRelic.LandingSound.SOLID;
            case "magical":
                return AbstractRelic.LandingSound.MAGICAL;
            default:
                return AbstractRelic.LandingSound.FLAT;
        }
    }
}