package siongsng.rpmtwupdatemod.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

@Config(modid = RpmtwUpdateMod.MOD_ID, name = RpmtwUpdateMod.MOD_NAME, category = "rpmtw")
public class RPMTWConfig {
    @Config.Name("是否啟用物品翻譯界面")
    @Config.Comment("是否啟用點擊指定快捷鍵後開起物品翻譯界面，並顯示相關資訊。")
    public static boolean crowdin = true;
    @Config.Name("是否啟用遊戲內更新翻譯包")
    @Config.Comment("是否啟用使用快捷鍵更新RPMTW翻譯包。")
    public static boolean ReloadPack = true;
    @Config.Name("是否啟用公告系統")
    @Config.Comment("進入世界時，是否自動發送公告。(此變更須重啟遊戲後生效)")
    public static boolean notice = true;
    @Config.Name("是否自動切換語言")
    @Config.Comment("預設進入遊戲時，是否自動切換語言為 繁體中文。")
    public static boolean isChinese = true;
    @Config.Name("是否使用宇宙通訊系統")
    @Config.Comment("宇宙通訊系統是本模組提供的一個文字聊天系統，將會與RPMTW官方Discord群組進行串連，可以使用本系統在遊戲內外進行交流。")
    public static boolean isChat = true;
    public static String Token = ""; //儲存Crowdin登入權杖
    public static boolean isCheck = false; //儲存登入權杖是否有效
    public static boolean isEULA = false; //使用者是否同意宇宙通訊EULA
    @Config.Name("是否使用機器翻譯")
    public static boolean isTranslate = true;

    // 儲存設定檔案
    @Mod.EventBusSubscriber(modid = RpmtwUpdateMod.MOD_ID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(RpmtwUpdateMod.MOD_ID)) {
                ConfigManager.sync(RpmtwUpdateMod.MOD_ID, Config.Type.INSTANCE);
                RpmtwUpdateMod.LOGGER.info("已儲存設定檔案");
            }
        }
    }
}
