package com.sk89q.craftbook.circuits.gates.world.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.PistonBaseMaterial;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.bukkit.CircuitCore;
import com.sk89q.craftbook.bukkit.CraftBookPlugin;
import com.sk89q.craftbook.bukkit.util.BukkitUtil;
import com.sk89q.craftbook.circuits.ic.AbstractIC;
import com.sk89q.craftbook.circuits.ic.AbstractICFactory;
import com.sk89q.craftbook.circuits.ic.ChipState;
import com.sk89q.craftbook.circuits.ic.IC;
import com.sk89q.craftbook.circuits.ic.ICFactory;
import com.sk89q.craftbook.util.RegexUtil;
import com.sk89q.craftbook.util.SignUtil;
import com.sk89q.craftbook.util.VerifyUtil;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.blocks.ItemID;

public class CombineHarvester extends AbstractIC {

    public CombineHarvester(Server server, ChangedSign sign, ICFactory factory) {

        super(server, sign, factory);
    }

    Vector offset;
    int radius;

    Block target;
    Block onBlock;

    @Override
    public void load() {

        onBlock = SignUtil.getBackBlock(BukkitUtil.toSign(getSign()).getBlock());

        try {
            radius = Integer.parseInt(RegexUtil.EQUALS_PATTERN.split(getSign().getLine(3))[0]);
            try {
                String[] loc = RegexUtil.COLON_PATTERN.split(RegexUtil.EQUALS_PATTERN.split(getSign().getLine(3))[1]);
                offset = new Vector(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]));
                if (offset.getX() > 16) offset.setX(16);
                if (offset.getY() > 16) offset.setY(16);
                if (offset.getZ() > 16) offset.setZ(16);

                if (offset.getX() < -16) offset.setX(-16);
                if (offset.getY() < -16) offset.setY(-16);
                if (offset.getZ() < -16) offset.setZ(-16);
            } catch (Exception e) {
                offset = new Vector(0, 2, 0);
            }

        } catch (Exception e) {
            radius = 10;
            offset = new Vector(0, 2, 0);
        }

        radius = VerifyUtil.verifyRadius(radius, 15);

        target = onBlock.getRelative(offset.getBlockX(), offset.getBlockY(), offset.getBlockZ());
    }

    @Override
    public String getTitle() {

        return "Combine Harvester";
    }

    @Override
    public String getSignTitle() {

        return "HARVEST";
    }

    @Override
    public void trigger(ChipState chip) {

        if (chip.getInput(0)) chip.setOutput(0, harvest());
    }

    public boolean harvest() {

        for (int x = -radius + 1; x < radius; x++) {
            for (int y = -radius + 1; y < radius; y++) {
                for (int z = -radius + 1; z < radius; z++) {
                    int rx = target.getX() - x;
                    int ry = target.getY() - y;
                    int rz = target.getZ() - z;
                    Block b = BukkitUtil.toSign(getSign()).getWorld().getBlockAt(rx, ry, rz);

                    if (harvestable(b)) {

                        collectDrops(getDrops(b));
                        b.setTypeId(0);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void collectDrops(ItemStack[] drops) {

        BlockFace back = SignUtil.getBack(BukkitUtil.toSign(getSign()).getBlock());
        Block pipe = SignUtil.getBackBlock(BukkitUtil.toSign(getSign()).getBlock()).getRelative(back);
        if (pipe.getTypeId() == BlockID.PISTON_STICKY_BASE) {

            PistonBaseMaterial p = (PistonBaseMaterial) pipe.getState().getData();
            Block fac = pipe.getRelative(p.getFacing());
            if (fac.getLocation().equals(BukkitUtil.toSign(getSign()).getBlock().getRelative(back).getLocation())) {

                List<ItemStack> items = new ArrayList<ItemStack>();
                Collections.addAll(items, drops);
                if (CircuitCore.inst().getPipeFactory() != null)
                    if (CircuitCore.inst().getPipeFactory().detect(BukkitUtil.toWorldVector(pipe), items) != null) {
                        return;
                    }
            }
        }
        if (onBlock.getRelative(0, 1, 0).getTypeId() == BlockID.CHEST) {

            Chest c = (Chest) onBlock.getRelative(0, 1, 0).getState();
            HashMap<Integer, ItemStack> leftovers = c.getInventory().addItem(drops);
            for (ItemStack item : leftovers.values()) {

                onBlock.getWorld().dropItemNaturally(BukkitUtil.toSign(getSign()).getLocation().add(0.5, 0, 0.5), item);
            }
        } else {
            for (ItemStack item : drops) {

                onBlock.getWorld().dropItemNaturally(BukkitUtil.toSign(getSign()).getLocation().add(0.5, 0, 0.5), item);
            }
        }
    }

    public boolean harvestable(Block block) {

        // TODO add a list of things that can be harvestable, and in what circumstance.
        if((block.getTypeId() == BlockID.CROPS || block.getTypeId() == BlockID.CARROTS || block.getTypeId() == BlockID.POTATOES) && block.getData() >= 0x7)
            return true;

        if(block.getTypeId() == BlockID.CACTUS && block.getRelative(0, -1, 0).getTypeId() == BlockID.CACTUS && block.getRelative(0, 1, 0).getTypeId() != BlockID.CACTUS)
            return true;

        if(block.getTypeId() == BlockID.REED && block.getRelative(0, -1, 0).getTypeId() == BlockID.REED && block.getRelative(0, 1, 0).getTypeId() != BlockID.REED)
            return true;

        if(block.getTypeId() == BlockID.VINE && block.getRelative(0, 1, 0).getTypeId() == BlockID.VINE && block.getRelative(0, -1, 0).getTypeId() != BlockID.VINE)
            return true;

        if(block.getTypeId() == BlockID.COCOA_PLANT && ((block.getData() & 0x8) == 0x8 || (block.getData() & 0xC) == 0xC))
            return true;

        if(block.getTypeId() == BlockID.NETHER_WART && block.getData() >= 0x3)
            return true;

        return false;
    }

    public ItemStack[] getDrops(Block b) {

        List<ItemStack> drops = new ArrayList<ItemStack>();

        if (b.getTypeId() == BlockID.CROPS) {

            drops.add(new ItemStack(ItemID.WHEAT, 1));
            int amount = CraftBookPlugin.inst().getRandom().nextInt(4);
            if(amount > 0)
                drops.add(new ItemStack(ItemID.SEEDS, amount));
        } else if (b.getTypeId() == BlockID.CARROTS) {

            drops.add(new ItemStack(ItemID.CARROT, 1 + CraftBookPlugin.inst().getRandom().nextInt(4)));
        } else if (b.getTypeId() == BlockID.POTATOES) {

            drops.add(new ItemStack(ItemID.POTATO, 1 + CraftBookPlugin.inst().getRandom().nextInt(4)));
            if(CraftBookPlugin.inst().getRandom().nextInt(50) == 0)
                drops.add(new ItemStack(ItemID.POISONOUS_POTATO, 1));
        }

        return drops.toArray(new ItemStack[drops.size()]);
    }

    public static class Factory extends AbstractICFactory {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(ChangedSign sign) {

            return new CombineHarvester(getServer(), sign, this);
        }

        @Override
        public String getShortDescription() {

            return "Harvests nearby crops.";
        }

        @Override
        public String[] getLineHelp() {

            String[] lines = new String[] {"radius=x:y:z offset", null};
            return lines;
        }
    }
}