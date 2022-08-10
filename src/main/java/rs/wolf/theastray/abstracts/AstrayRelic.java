package rs.wolf.theastray.abstracts;

import org.jetbrains.annotations.NotNull;
import rs.lazymankits.abstracts.LMCustomRelic;
import rs.wolf.theastray.data.DataMst;
import rs.wolf.theastray.data.RelicData;
import rs.wolf.theastray.utils.TAUtils;

public abstract class AstrayRelic extends LMCustomRelic {
    private AstrayRelic(@NotNull RelicData data) {
        super(data.getRelicID(), TAUtils.RelicImage(1), TAUtils.RelicOutlineImage(1), data.getTier(), data.getSfx());
    }
    
    public AstrayRelic(int index) {
        this(DataMst.GetRelicData(index));
    }
    
    public AstrayRelic(String localname) {
        this(DataMst.GetRelicData(localname));
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}