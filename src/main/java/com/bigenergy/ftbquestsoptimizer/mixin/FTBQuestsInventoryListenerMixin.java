package com.bigenergy.ftbquestsoptimizer.mixin;

import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.util.FTBQuestsInventoryListener;
import dev.ftb.mods.ftbteams.FTBTeamsAPI;
import me.shedaniel.architectury.hooks.PlayerHooks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = FTBQuestsInventoryListener.class, remap = false)
public class FTBQuestsInventoryListenerMixin {

    @Shadow
    public final ServerPlayerEntity player;

    private int ticksSkipped;

    public FTBQuestsInventoryListenerMixin(ServerPlayerEntity player) {
        this.player = player;
    }

    private boolean tryTick()
    {
        int skipTicksAmount = 5;
        if (skipTicksAmount <= 0)
            return true;

        this.ticksSkipped++;
        if (this.ticksSkipped > skipTicksAmount)
        {
            this.ticksSkipped = 0;
            return true;
        }

        return false;
    }

    @Shadow
    public static void detect(ServerPlayerEntity player, ItemStack craftedItem, long sourceTask) {
        ServerQuestFile file = ServerQuestFile.INSTANCE;
        if (file != null && !PlayerHooks.isFake(player)) {
            TeamData data = file.getNullableTeamData(FTBTeamsAPI.getPlayerTeamID(player.getUUID()));
            if (data != null && !data.isLocked()) {
                file.currentPlayer = player;

                for (Task task : file.getSubmitTasks()) {
                    if (task.id != sourceTask && data.canStartTasks(task.quest)) {
                        task.submitTask(data, player, craftedItem);
                    }
                }

                file.currentPlayer = null;
            }
        }
    }


    /**
     * @author Big_Energy
     * @reason skip ticks optimization
     */
    @Overwrite
    public void refreshContainer(Container container, NonNullList<ItemStack> itemsList) {
        if (!this.tryTick())
            return;

        detect(player, ItemStack.EMPTY, 0);
    }

    /**
     * @author Big_Energy
     * @reason skip ticks optimization
     */
    @Overwrite
    public void slotChanged(Container container, int index, ItemStack stack) {
        if (!this.tryTick())
            return;
        if (!stack.isEmpty() && container.getSlot(index).container == this.player.inventory) {
            detect(this.player, ItemStack.EMPTY, 0L);
        }

    }

}
