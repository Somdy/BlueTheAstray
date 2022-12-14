package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayRelic;

public class Relic10 extends AstrayRelic {
    public Relic10() {
        super(10);
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public void onEnterRestRoom() {
        if (!cpr().masterDeck.isEmpty()) {
            getExptRandomCard(cardRandomRng(), c -> c.canUpgrade() && (!(c instanceof AstrayCard)
                    || !((AstrayCard) c).isEnlightenCard()), cpr().masterDeck.group)
                    .ifPresent(c -> {
                        flash();
                        c.upgrade();
                        AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                    });
        }
    }
}
