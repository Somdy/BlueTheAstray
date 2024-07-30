package rs.wolf.theastray.utils;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.jetbrains.annotations.Nullable;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.patches.TACardEnums;

import java.util.*;

public class GlobalCursorMst {
    private static GameCursor DefaultCursor;
    private static final Map<AbstractPlayer.PlayerClass, GameCursor> cursorMap = new HashMap<>();
    private static final Set<AbstractPlayer.PlayerClass> exclusiveCharacters = new HashSet<>();
    private static final Set<String> exclusiveModIDs = new HashSet<>();
    
    public static void Initialize(GameCursor defaultCursor) {
        DefaultCursor = defaultCursor;
    }
    
    public static GameCursor GetDefaultCursor() {
        return DefaultCursor;
    }
    
    public static void AddCursor(AbstractPlayer.PlayerClass playerClass, GameCursor cursor, boolean exclusive) {
        cursorMap.put(playerClass, cursor);
        if (exclusive) {
            AddExclusiveCharacter(playerClass);
        }
    }
    
    public static void AddCursor(AbstractPlayer.PlayerClass playerClass, GameCursor cursor) {
        AddCursor(playerClass, cursor, true);
    }
    
    public static void AddExclusiveCharacter(AbstractPlayer.PlayerClass playerClass) {
        exclusiveCharacters.add(playerClass);
    }
    
    public static void AddExclusiveMod(String modID) {
        exclusiveModIDs.add(modID);
    }
    
    public static boolean IsExclusiveCharacter(AbstractPlayer.PlayerClass playerClass) {
        return exclusiveCharacters.contains(playerClass);
    }
    
    public static boolean HasAnyExclusiveModLoaded() {
        for (String modID : exclusiveModIDs) {
            return Loader.isModLoadedOrSideloaded(modID);
        }
        return false;
    }
    
    @Nullable
    public static GameCursor GetCursor(AbstractPlayer.PlayerClass playerClass) {
        return cursorMap.getOrDefault(playerClass, null);
    }
    
    public static class Patches {
        @SpirePatch2(clz = CardCrawlGame.class, method = "render")
        public static class ChangeCursorStylePatch {
            @SpirePrefixPatch
            public static void PrefixCheck() {
                if (GlobalCursorMst.HasAnyExclusiveModLoaded())
                    return;
                if (TAUtils.RoomAvailable() && AbstractDungeon.player != null) {
                    AbstractPlayer p = AbstractDungeon.player;
                    AbstractPlayer.PlayerClass playerClz = p.chosenClass;
                    if (Leader.ASTRAY_CURSOR_AS_GLOBAL && !GlobalCursorMst.IsExclusiveCharacter(playerClz)) {
                        playerClz = TACardEnums.BlueTheAstray;
                    }
                    GameCursor cursor = GlobalCursorMst.GetCursor(playerClz);
                    // if an exclusive character uses other cursor thing, do nothing
                    if (cursor == null && GlobalCursorMst.IsExclusiveCharacter(playerClz))
                        return;
                    if (cursor != null) {
                        CardCrawlGame.cursor = cursor;
                    } else {
                        CardCrawlGame.cursor = GlobalCursorMst.GetDefaultCursor();
                    }
                }
            }
        }
        @SpirePatch2(clz = CardCrawlGame.class, method = "update")
        public static class ChangeCursorStyleForUpdatePatch {
            @SpirePrefixPatch
            public static void PrefixCheck() {
                if (GlobalCursorMst.HasAnyExclusiveModLoaded())
                    return;
                if (TAUtils.RoomAvailable() && AbstractDungeon.player != null) {
                    AbstractPlayer p = AbstractDungeon.player;
                    AbstractPlayer.PlayerClass playerClz = p.chosenClass;
                    if (Leader.ASTRAY_CURSOR_AS_GLOBAL && !GlobalCursorMst.IsExclusiveCharacter(playerClz)) {
                        playerClz = TACardEnums.BlueTheAstray;
                    }
                    GameCursor cursor = GlobalCursorMst.GetCursor(playerClz);
                    // if an exclusive character uses other cursor thing, do nothing
                    if (cursor == null && GlobalCursorMst.IsExclusiveCharacter(playerClz))
                        return;
                    if (cursor != null) {
                        CardCrawlGame.cursor = cursor;
                    } else {
                        CardCrawlGame.cursor = GlobalCursorMst.GetDefaultCursor();
                    }
                }
            }
        }
    }
}