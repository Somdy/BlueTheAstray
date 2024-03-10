package rs.wolf.theastray.utils;

import basemod.BaseMod;
import basemod.abstracts.CustomUnlockBundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.LMDebug;
import rs.lazymankits.abstracts.DamageInfoTag;
import rs.lazymankits.utils.LMGameGeneralUtils;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.patches.TACardEnums;

public interface TAUtils extends LMGameGeneralUtils {
    
    DamageInfoTag MAGICAL_DAMAGE = new DamageInfoTag(MakeID("MagicalDamage"));
    
    default String getSupLang() {
        return getSupportedLanguage(Settings.language);
    }
    
    default CardGroup findCardGroupWhereCardIs(AbstractCard target) {
        AbstractPlayer p = LMSK.Player();
        if (p.drawPile.contains(target))
            return p.drawPile;
        if (p.hand.contains(target))
            return p.hand;
        if (p.discardPile.contains(target))
            return p.discardPile;
        if (p.exhaustPile.contains(target))
            return p.exhaustPile;
        return null;
    }
    
    static void AddUnlockBundle(AbstractUnlock.UnlockType type, AbstractPlayer.PlayerClass c, int level, @NotNull String... unlocks) {
        BaseMod.addUnlockBundle(new CustomUnlockBundle(type, unlocks[0], unlocks[1], unlocks[2]), c, level);
        if (type == AbstractUnlock.UnlockType.CARD) {
            for (String unlock : unlocks) {
                UnlockTracker.addCard(unlock);
            }
        }
        if (UnlockTracker.unlockProgress.getInteger(c.toString() + "UnlockLevel") > level + 1) {
            if (type == AbstractUnlock.UnlockType.CARD) {
                for (String unlock : unlocks) {
                    UnlockTracker.unlockCard(unlock);
                }
            } else if (type == AbstractUnlock.UnlockType.RELIC) {
                for (String unlock : unlocks) {
                    UnlockTracker.lockedRelics.remove(unlock);
                    UnlockTracker.markRelicAsSeen(unlock);
                }
            } else {
                TAUtils.Log("UNDEFINED UNLOCK TYPE: [" + type + "]");
            }
        }
    }
    
    static void AddUnlockBundle(AbstractPlayer.PlayerClass c, int level, @NotNull String... unlocks) {
        AddUnlockBundle(AbstractUnlock.UnlockType.CARD, c, level, unlocks);
    }
    
    @NotNull
    static String SupLang() {
        switch(Settings.language) {
            case ZHS:
                return "zhs";
            case ZHT:
                return "zht";
            default:
                return "eng";
        }
    }
    
    static void Log(String what) {
        Leader.Log(what);
    }
    
    static void Log(Object who, Object what) {
        LMDebug.deLog(who, "THE ASTRAY-[LOG]> " + what);
    }
    
    @NotNull
    static String GetPrefix() {
        return Leader.PREFIX + ":";
    }
    
    @NotNull
    static String MakeID(String id) {
        return GetPrefix() + id;
    }
    
    @NotNull
    static String RmPrefix(@NotNull String idWithPrefix) {
        if (idWithPrefix.startsWith(GetPrefix()))
            return idWithPrefix.replaceFirst(GetPrefix(), "");
        return idWithPrefix;
    }
    
    static boolean RoomChecker(Class<? extends AbstractRoom> clz, AbstractRoom.RoomPhase phase) {
        return RoomAvailable() && clz.isInstance(AbstractDungeon.getCurrRoom()) && AbstractDungeon.getCurrRoom().phase == phase;
    }
    
    static boolean RoomChecker(Class<? extends AbstractRoom> clz) {
        return RoomAvailable() && clz.isInstance(AbstractDungeon.getCurrRoom());
    }
    
    static boolean RoomChecker(AbstractRoom.RoomPhase phase) {
        return RoomAvailable() && AbstractDungeon.getCurrRoom().phase == phase;
    }
    
    static boolean RoomAvailable() {
        return AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null;
    }
    
    static boolean IsTrueMagical(@NotNull AbstractCard card) {
        return card.hasTag(TACardEnums.MAGICAL) && !card.hasTag(TACardEnums.DE_MAGICAL);
    }
    
    static boolean IsMagical(@NotNull AbstractCard card) {
        return card.hasTag(TACardEnums.MAGICAL) || card.hasTag(TACardEnums.DE_MAGICAL);
    }
    
    static boolean IsDeMagical(@NotNull AbstractCard card) {
        return card.hasTag(TACardEnums.DE_MAGICAL);
    }
    
    @NotNull
    static String CardImage(String index) {
        return "AstrayAssets/images/cardports/" + index + ".png";
    }
    
    static Texture RelicImage(String index) {
        if (Gdx.files.internal("AstrayAssets/images/relics/" + index + ".png").exists())
            return ImageMaster.loadImage("AstrayAssets/images/relics/" + index + ".png");
        return ImageMaster.loadImage("AstrayAssets/images/relics/test.png");
    }
    
    static Texture RelicOutlineImage(String index) {
        if (Gdx.files.internal("AstrayAssets/images/relics/outline/" + index + ".png").exists())
            return ImageMaster.loadImage("AstrayAssets/images/relics/outline/" + index + ".png");
        return ImageMaster.loadImage("AstrayAssets/images/relics/test.png");
    }
    
    static UIStrings UIStrings(@NotNull String id) {
        if (!id.startsWith(GetPrefix()))
            id = MakeID(id);
        return CardCrawlGame.languagePack.getUIString(id);
    }
    
    static PowerStrings PowerStrings(@NotNull String id) {
        if (!id.startsWith(GetPrefix()))
            id = MakeID(id);
        return CardCrawlGame.languagePack.getPowerStrings(id);
    }
    
    static MonsterStrings MonsterStrings(@NotNull String id) {
        if (!id.startsWith(GetPrefix()))
            id = MakeID(id);
        return CardCrawlGame.languagePack.getMonsterStrings(id);
    }
}