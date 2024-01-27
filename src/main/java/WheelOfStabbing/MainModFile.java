package WheelOfStabbing;

import WheelOfStabbing.util.TextureLoader;
import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

@SpireInitializer
public class MainModFile implements
        EditStringsSubscriber,
        PostInitializeSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(MainModFile.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static SpireConfig WoSConfig;
    public static String FILE_NAME = "WheelOfStabbingConfig";

    public static final String ENABLE_MOD = "enableMod";
    public static boolean modEnabled = true;

    //This is for the in-game mod settings panel.
    public static UIStrings uiStrings;
    public static String[] TEXT;
    public static String[] EXTRA_TEXT;
    private static final String MODNAME = "Wheel of Stabbing";
    private static final String AUTHOR = "Mistress Autumn";
    private static final String DESCRIPTION = "Makes Gremlin Wheel more stabby.";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "WheelOfStabbingResources/images/Badge.png";
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public MainModFile() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
      
        setModID("WheelOfStabbing");
        
        logger.info("Done subscribing");
        
        logger.info("Adding mod settings");

        Properties INDefaultSettings = new Properties();
        INDefaultSettings.setProperty(ENABLE_MOD, Boolean.toString(modEnabled));
        try {
            WoSConfig = new SpireConfig(modID, FILE_NAME, INDefaultSettings);
            modEnabled = WoSConfig.getBool(ENABLE_MOD);
        } catch (IOException e) {
            logger.error("SpireConfig initialization failed:");
            e.printStackTrace();
        }

        logger.info("Done adding mod settings");
        
    }

    public static void setModID(String ID) {
        modID = ID;
    }
    
    public static String getModID() {
        return modID;
    }
    
    public static void initialize() {
        logger.info("========================= Initializing Wheel of Stabbing. =========================");
        MainModFile mainModFile = new MainModFile();
        logger.info("========================= /Wheel of Stabbing Initialized/ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        //Grab the strings
        uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ModConfigs"));
        EXTRA_TEXT = uiStrings.EXTRA_TEXT;
        TEXT = uiStrings.TEXT;
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        float currentYposition = 740f;
        float spacingY = 55f;
        
        // Create the on/off button:
        ModLabeledToggleButton enableModsButton = new ModLabeledToggleButton(TEXT[0],400.0f - 40f, currentYposition - 10f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                WoSConfig.getBool(ENABLE_MOD), settingsPanel, (label) -> {}, (button) -> {
            WoSConfig.setBool(ENABLE_MOD, button.enabled);
            modEnabled = button.enabled;
            try {
                WoSConfig.save();} catch (IOException e) {e.printStackTrace();}
        });
        currentYposition -= spacingY;

        settingsPanel.addUIElement(enableModsButton);
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================

    // ================ LOAD THE LOCALIZATION ===================

    private String loadLocalizationIfAvailable(String fileName) {
        if (!Gdx.files.internal(getModID() + "Resources/localization/" + Settings.language.toString().toLowerCase()+ "/" + fileName).exists()) {
            logger.info("Language: " + Settings.language.toString().toLowerCase() + ", not currently supported for " +fileName+".");
            return "eng" + "/" + fileName;
        } else {
            logger.info("Loaded Language: "+ Settings.language.toString().toLowerCase() + ", for "+fileName+".");
            return Settings.language.toString().toLowerCase() + "/" + fileName;
        }
    }

    // ================ /LOAD THE LOCALIZATION/ ===================

    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        // UIStrings
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("UI-Strings.json"));

        logger.info("Done editing strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
