package rs.wolf.theastray.vfx.campfire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lazymankits.interfaces.cards.SwappableUpgBranchCard;
import rs.lazymankits.patches.branchupgrades.SwappableLogicPatch;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.ui.campfire.EnlightenOption;
import rs.wolf.theastray.utils.TAUtils;

import java.util.List;

public class SwapEnlightenBranchEffect extends LMCustomGameEffect implements TAUtils {
    private final String msg;
    private boolean screenOpened;
    private final Color screenColor = AbstractDungeon.fadeColor.cpy();
    private final CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private final EnlightenOption option;
    private List<AbstractCard> sourceList;
    
    public SwapEnlightenBranchEffect(EnlightenOption option, @NotNull List<AbstractCard> cards, String msg) {
        this.option = option;
        if (!cards.isEmpty()) {
            for (AbstractCard c : cards) {
                if (!(c instanceof SwappableUpgBranchCard) || !((SwappableUpgBranchCard) c).swappable())
                    continue;
                cardGroup.addToTop(c);
            }
            sourceList = cards;
        }
        this.msg = msg;
        screenOpened = false;
        duration = 1.5F;
        screenColor.a = 0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }
    
    @Override
    public void update() {
        if (cardGroup.isEmpty() || duration < 0F) {
            isDone = true;
            if (CampfireUI.hidden) {
                AbstractRoom.waitTimer = 0F;
                currRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                CampfireUI.hidden = false;
                ((RestRoom) currRoom()).campfireUI.somethingSelected = false;
            }
        }
        if (!AbstractDungeon.isScreenUp) {
            duration -= Gdx.graphics.getDeltaTime();
            updateBlackScreenColor();
        }
        if (!screenOpened && duration < 1F && !cardGroup.isEmpty()) {
            screenOpened = true;
            SwappableLogicPatch.OpenGridCardSelectScreen(cardGroup, 1, msg,
                    true, false, true, false);
        }
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()
                && AbstractDungeon.gridSelectScreen.forUpgrade) {
            AbstractCard chosenCard = null;
            AbstractCard chosenBranch = null;
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                chosenCard = card;
                chosenBranch = SwappableLogicPatch.GetChosenBranch(card);
                if (chosenBranch != null) {
                    AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                    AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(chosenBranch.makeStatEquivalentCopy()));
                }
            }
            if (chosenBranch != null && chosenCard != null && sourceList.contains(chosenCard)) {
                bottleCheck(chosenCard, chosenBranch);
                sourceList.set(sourceList.indexOf(chosenCard), chosenBranch);
                option.usable = false;
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            ((RestRoom) currRoom()).fadeIn();
        }
    }
    
    private void bottleCheck(@NotNull AbstractCard source, @NotNull AbstractCard target) {
        AbstractPlayer p = LMSK.Player();
        target.inBottleFlame = source.inBottleFlame;
        target.inBottleLightning = source.inBottleLightning;
        target.inBottleTornado = source.inBottleTornado;
        if (target.inBottleFlame && p.hasRelic(BottledFlame.ID)) {
            BottledFlame flame = ((BottledFlame) p.getRelic(BottledFlame.ID));
            flame.card = target;
            flame.setDescriptionAfterLoading();
        }
        if (target.inBottleLightning && p.hasRelic(BottledLightning.ID)) {
            BottledLightning lightning = ((BottledLightning) p.getRelic(BottledLightning.ID));
            lightning.card = target;
            lightning.setDescriptionAfterLoading();
        }
        if (target.inBottleTornado && p.hasRelic(BottledTornado.ID)) {
            BottledTornado tornado = ((BottledTornado) p.getRelic(BottledTornado.ID));
            tornado.card = target;
            tornado.setDescriptionAfterLoading();
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
    public void render(@NotNull SpriteBatch sb) {
        sb.setColor(screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0F, 0F, Settings.WIDTH, Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
            AbstractDungeon.gridSelectScreen.render(sb);
    }
    
    @Override
    public void dispose() {
        
    }
}
