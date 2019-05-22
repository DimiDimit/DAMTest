package net.dimitrodam.forgetest;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings({"WeakerAccess", "unused"})
@Config(modid = DAMTest.MODID)
//@Config.LangKey(DTConfig.PREFIX + "title")
public class DTConfig {
    @Config.Ignore
    public static final String PREFIX = DAMTest.MODID + ".config.";

    @Config.LangKey(PREFIX + "matter_config")
    public static final MatterConfig matterConfig = new MatterConfig();
    @SuppressWarnings({"unused"})
    public static class MatterConfig {
        @Config.RequiresMcRestart
        @Config.LangKey(PREFIX + "matter_config.raw_oredict")
        public String[] rawOredict = new String[]{};
        @Config.RequiresMcRestart
        @Config.LangKey(PREFIX + "matter_config.rb_oredict")
        public String[] rbOredict = new String[]{"coal=1", "charcoal=1"};
        @Config.RequiresMcRestart
        @Config.LangKey(PREFIX + "matter_config.ndiggb_oredict")
        public String[] ndiggbOredict = new String[]{"redstone=2", "lapis=2", "iron=3", "gold=5", "diamond=15", "emerald=20", "rainbow=576"};
    }

    @Mod.EventBusSubscriber(modid = DAMTest.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(DAMTest.MODID)) {
                ConfigManager.sync(DAMTest.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
