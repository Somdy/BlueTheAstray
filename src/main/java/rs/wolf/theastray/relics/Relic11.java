package rs.wolf.theastray.relics;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import javassist.*;
import javassist.bytecode.*;
import javassist.convert.Transformer;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.utils.GlobalIDMst;

import static javassist.bytecode.Bytecode.*;

public class Relic11 extends AstrayRelic {
    public Relic11() {
        super(11);
    }
    
    @Override
    public void onEquip() {
        cpr().gainGold(150);
        Leader.SaveData.putValue("eleven", true);
        if (TheEnding.ID.equals(AbstractDungeon.id)) {
            DungeonMap.boss = ImageMaster.loadImage("AstrayAssets/images/ui/map/boss/bluetheboss_icon.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("AstrayAssets/images/ui/map/boss/bluetheboss_outline.png");
        }
    }
    
    @Override
    protected boolean selfCanSpawn() {
        return false;
    }
    
    public static int GetMaxAmount(int defaultValue) {
        if (LMSK.Player().hasRelic(GlobalIDMst.RelicID(11)))
            defaultValue = Integer.MAX_VALUE;
        return defaultValue;
    }
    
    public static class LimitsBreakPatch {
        @SpirePatches2({
                @SpirePatch2(clz = StrengthPower.class, method = "stackPower"),
                @SpirePatch2(clz = DexterityPower.class, method = "stackPower"),
                @SpirePatch2(clz = FocusPower.class, method = "stackPower")
        })
        public static class StrengthDexterityAndFocusStackPowerPatch {
            @SpireRawPatch
            public static void Raw(CtBehavior ctBehavior) throws Exception {
                ctBehavior.instrument(new CodeConverter(){{
                    this.transformers = new Transformer(this.transformers) {
                        @Override
                        public int transform(CtClass ctClass, int index, CodeIterator iterator, ConstPool constPool)
                                throws CannotCompileException, BadBytecode {
                            int pushcode = iterator.byteAt(index);
                            if (pushcode == SIPUSH) {
                                int byte1 = iterator.byteAt(index + 1);
                                int byte2 = iterator.byteAt(index + 2);
                                int icmcode = iterator.byteAt(index + 3);
                                if (((byte1 << 8) | byte2) == 999 && icmcode == IF_ICMPLT) {
                                    Bytecode bc = new Bytecode(constPool);
                                    bc.addInvokestatic(Relic11.class.getName(), "GetMaxAmount", 
                                            Descriptor.ofMethod(CtClass.intType, new CtClass[]{CtClass.intType}));
                                    iterator.insert(index + 3, bc.get());
                                }
                            }
                            return index;
                        }
                    };
                }});
            }
        }
        
        @SpirePatches2({
                @SpirePatch2(clz = StrengthPower.class, method = SpirePatch.CONSTRUCTOR),
                @SpirePatch2(clz = DexterityPower.class, method = SpirePatch.CONSTRUCTOR),
                @SpirePatch2(clz = FocusPower.class, method = SpirePatch.CONSTRUCTOR)
        })
        public static class StrengthDexterityAndFocusConstructorPatch {
            @SpireRawPatch
            public static void Raw(CtBehavior ctBehavior) throws Exception {
                ctBehavior.instrument(new CodeConverter(){{
                    this.transformers = new Transformer(this.transformers) {
                        @Override
                        public int transform(CtClass ctClass, int index, CodeIterator iterator, ConstPool constPool)
                                throws CannotCompileException, BadBytecode {
                            int pushcode = iterator.byteAt(index);
                            if (pushcode == SIPUSH) {
                                int byte1 = iterator.byteAt(index + 1);
                                int byte2 = iterator.byteAt(index + 2);
                                int icmcode = iterator.byteAt(index + 3);
                                if (((byte1 << 8) | byte2) == 999 && icmcode == IF_ICMPLT) {
                                    Bytecode bc = new Bytecode(constPool);
                                    bc.addInvokestatic(Relic11.class.getName(), "GetMaxAmount",
                                            Descriptor.ofMethod(CtClass.intType, new CtClass[]{CtClass.intType}));
                                    iterator.insert(index + 3, bc.get());
                                }
                            }
                            return index;
                        }
                    };
                }});
            }
        }
    
        @SpirePatch2(clz = AbstractCreature.class, method = "addBlock")
        public static class AddBlockPatch {
            @SpireRawPatch
            public static void Raw(CtBehavior ctBehavior) throws Exception {
                ctBehavior.instrument(new CodeConverter(){{
                    this.transformers = new Transformer(this.transformers) {
                        @Override
                        public int transform(CtClass ctClass, int index, CodeIterator iterator, ConstPool constPool)
                                throws CannotCompileException, BadBytecode {
                            int pushcode = iterator.byteAt(index);
                            if (pushcode == SIPUSH) {
                                int byte1 = iterator.byteAt(index + 1);
                                int byte2 = iterator.byteAt(index + 2);
                                int icmcode = iterator.byteAt(index + 3);
                                if (((byte1 << 8) | byte2) == 999 && icmcode == IF_ICMPLE) {
                                    Bytecode bc = new Bytecode(constPool);
                                    bc.addInvokestatic(Relic11.class.getName(), "GetMaxAmount",
                                            Descriptor.ofMethod(CtClass.intType, new CtClass[]{CtClass.intType}));
                                    iterator.insert(index + 3, bc.get());
                                }
                            }
                            return index;
                        }
                    };
                }});
            }
        }
    }
}