package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.interfaces.cards.BranchableUpgradeCard;
import rs.lazymankits.interfaces.cards.RUM;
import rs.wolf.theastray.abstracts.AstrayRelic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Relic8 extends AstrayRelic {
    private static final AbstractCard.CardRarity[] RARITIES = {AbstractCard.CardRarity.BASIC, AbstractCard.CardRarity.SPECIAL, 
            AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardRarity.RARE};
    
    public Relic8() {
        super(8);
    }
    
    @Override
    public void onObtainCard(AbstractCard card) {
        if (!cpr().masterDeck.isEmpty()) {
            List<AbstractCard> cards = new ArrayList<>();
            int rarity = 0;
            while (cards.isEmpty()) {
                collectCards(cards, rarity);
                rarity++;
                if (rarity >= RARITIES.length) break;
            }
            if (!cards.isEmpty()) {
                flash();
                getRandom(cards, cardRandomRng())
                        .ifPresent(c -> {
                            if (c instanceof BranchableUpgradeCard && ((BranchableUpgradeCard) c).canBranch()) {
                                int branch = ((BranchableUpgradeCard) c).getBranchForRandomUpgrading(RUM.LESSONS_LEARNT);
                                ((BranchableUpgradeCard) c).setChosenBranch(branch);
                            }
                            c.upgrade();
                            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH * 0.25F, Settings.HEIGHT / 2F));
                            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(),
                                    Settings.WIDTH * 0.25F, Settings.HEIGHT  / 2F));
                        });
            }
        }
    }
    
    private void collectCards(@NotNull List<AbstractCard> cards, int index) {
        cards.addAll(cpr().masterDeck.group.stream()
                .filter(c -> isCardRarityOf(c, RARITIES[index]) && c.canUpgrade())
                .collect(Collectors.toList()));
    }
    
    @Override
    protected boolean selfCanSpawn() {
        return AbstractDungeon.actNum > 1;
    }
}