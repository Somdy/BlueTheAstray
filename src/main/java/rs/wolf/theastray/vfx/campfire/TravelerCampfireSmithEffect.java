package rs.wolf.theastray.vfx.campfire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.utils.TAUtils;

public class TravelerCampfireSmithEffect extends LMCustomGameEffect implements TAUtils {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CampfireSmithEffect");
    public static final String[] TEXT = uiStrings.TEXT;
    private boolean screenOpened = false;
    private Color screenColor = AbstractDungeon.fadeColor.cpy();
    
    public TravelerCampfireSmithEffect() {
        duration = 1.5F;
        screenColor.a = 0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }
    
    @Override
    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            duration -= Gdx.graphics.getDeltaTime();
            updateBlackScreenColor();
        }
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()
                && AbstractDungeon.gridSelectScreen.forUpgrade) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                CardCrawlGame.metricData.campfire_upgraded++;
                CardCrawlGame.metricData.addCampfireChoiceData("SMITH", card.getMetricID());
                card.upgrade();
                LMSK.Player().bottledCardUpgradeCheck(card);
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            if (currRoom() instanceof RestRoom)
                ((RestRoom) currRoom()).fadeIn();
        }
        if (duration < 1F && !screenOpened) {
            screenOpened = true;
            AbstractDungeon.gridSelectScreen.open(LMSK.Player().masterDeck.getUpgradableCards(), 1, TEXT[0], 
                    true, false, true, false);
            for (AbstractRelic r : LMSK.Player().relics) {
                r.onSmith();
            }
        }
        if (duration < 0F) {
            isDone = true;
            if (CampfireUI.hidden) {
                AbstractRoom.waitTimer = 0F;
                currRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                if (currRoom() instanceof RestRoom)
                    ((RestRoom) currRoom()).cutFireSound();
            }
        }
    }
    
    private void updateBlackScreenColor() {
        if (duration > 1F) {
            screenColor.a = Interpolation.fade.apply(1F, 0F, (duration - 1F) * 2F);
        } else {
            screenColor.a = Interpolation.fade.apply(0F, 1F, duration / 1.5F);
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        
    }
    
    @Override
    public void dispose() {
        
    }
}