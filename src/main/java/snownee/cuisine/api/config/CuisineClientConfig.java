package snownee.cuisine.api.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

@OnlyIn(Dist.CLIENT)
public final class CuisineClientConfig {

    public static final ForgeConfigSpec spec;

    static {
        spec = new ForgeConfigSpec.Builder().configure(CuisineClientConfig::new).getRight();
    }

    private CuisineClientConfig(ForgeConfigSpec.Builder builder) {

    }

    public static void refresh() {

    }

    @SubscribeEvent
    public static void onFileChange(ModConfig.Reloading event) {
        ((CommentedFileConfig) event.getConfig().getConfigData()).load();
        refresh();
    }
}
