package rs.wolf.theastray.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class TAImageMst {
    public static Texture BADGE;
    public static Texture MANA;
    public static Texture CAMPFIRE_ENLIGHTEN_BTN;
    public static Texture TINY_STAR;
    public static Texture TINY_STAR_GLOW_MASK;
    // Cursor
    public static Texture CURSOR;
    public static Texture CURSOR_GLOW;
    public static Texture CURSOR_BADGE;
    public static Texture[] CURSOR_TRAILS;
    // Atlas Region
    public static TextureAtlas.AtlasRegion MAGIC_DAMAGE_STAR;
    public static TextureAtlas.AtlasRegion ASH_EXPLOSION;
    
    public static void Initialize() {
        BADGE = load("AstrayAssets/images/badge.png");
        MANA = load("AstrayAssets/images/ui/manalayout/mana.png");
        CAMPFIRE_ENLIGHTEN_BTN = load("AstrayAssets/images/ui/campfire/meditate.png");
        TINY_STAR = load("AstrayAssets/images/vfx/tinyStar.png");
        TINY_STAR_GLOW_MASK = load("AstrayAssets/images/vfx/tinyStar_glowMask.png");
        
        CURSOR = load("AstrayAssets/images/ui/cursor/body.png");
        CURSOR_GLOW = load("AstrayAssets/images/ui/cursor/glow.png");
        CURSOR_BADGE = load("AstrayAssets/images/ui/cursor/badge.png");
        CURSOR_TRAILS = new Texture[3];
        for (int i = 0; i < CURSOR_TRAILS.length; i++) {
            String path = String.format("AstrayAssets/images/ui/cursor/trail_%d.png", i);
            CURSOR_TRAILS[i] = load(path);
        }
        
        MAGIC_DAMAGE_STAR = loadRegion("AstrayAssets/images/vfx/magic_damage_star.png", 36, 36);
        ASH_EXPLOSION = loadRegion("AstrayAssets/images/vfx/ash_explosion_effect.png", 512, 512);
    }
    
    private static Texture load(String path) {
        return ImageMaster.loadImage(path);
    }
    
    private static TextureAtlas.AtlasRegion loadRegion(String path, int w, int h) {
        return new TextureAtlas.AtlasRegion(load(path), 0, 0, w, h);
    }
}