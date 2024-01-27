package WheelOfStabbing.patches;

import WheelOfStabbing.MainModFile;
import WheelOfStabbing.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;

public class WheelPatches {
    @SpirePatch2(clz = GremlinWheelGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class NewWheelImg {
        @SpirePostfixPatch
        public static void plz(@ByRef Texture[] ___wheelImg) {
            if (MainModFile.modEnabled) {
                ___wheelImg[0] = TextureLoader.getTexture("WheelOfStabbingResources/images/wheel.png");
            }
        }
    }

    @SpirePatch2(clz = GremlinWheelGame.class, method = "preApplyResult")
    public static class AlwaysStab {
        @SpirePrefixPatch
        public static void plz(@ByRef int[] ___result) {
            if (MainModFile.modEnabled) {
                ___result[0] = 5;
            }
        }
    }
}
