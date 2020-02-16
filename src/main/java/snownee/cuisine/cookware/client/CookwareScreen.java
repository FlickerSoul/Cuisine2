package snownee.cuisine.cookware.client;

import java.util.List;
import java.util.stream.IntStream;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.cuisine.cookware.container.CookwareContainer;
import snownee.cuisine.cookware.network.CBeginCookingPacket;

@OnlyIn(Dist.CLIENT)
public class CookwareScreen extends ContainerScreen<CookwareContainer> {

    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/furnace.png");

    public CookwareScreen(CookwareContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        addButton(new Button(88, 32, 28, 23, "Cook", btn -> {
            List<ItemStack> stacks = container.getInventory();
            if (!IntStream.range(0, 9).mapToObj(stacks::get).allMatch(ItemStack::isEmpty)) {
                new CBeginCookingPacket(hasShiftDown()).send();
            }
        }));
    }

    @Override
    public void render(int x, int y, float pTicks) {
        this.renderBackground();
        super.render(x, y, pTicks);
        this.renderHoveredToolTip(x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
        int i = this.guiLeft;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        this.minecraft.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int l = this.container.getCookProgressionScaled();
        if (l > 0) {
            this.blit(i + 89, j + 34, 176, 14, l + 1, 16);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 28.0F, 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, this.ySize - 96 + 2, 4210752);
    }

}
