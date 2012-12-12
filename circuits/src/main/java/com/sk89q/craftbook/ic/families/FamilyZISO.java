package com.sk89q.craftbook.ic.families;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.bukkit.BukkitUtil;
import com.sk89q.craftbook.ic.AbstractChipState;
import com.sk89q.craftbook.ic.AbstractICFamily;
import com.sk89q.craftbook.ic.ChipState;
import com.sk89q.craftbook.util.SignUtil;
import com.sk89q.worldedit.BlockWorldVector;

public class FamilyZISO extends AbstractICFamily {

    @Override
    public ChipState detect(BlockWorldVector source, ChangedSign sign) {

        return new ChipStateZISO(source, sign);
    }

    @Override
    public ChipState detectSelfTriggered(BlockWorldVector source, ChangedSign sign) {

        return new ChipStateZISO(source, sign, true);
    }

    @Override
    public String getSuffix() {

        return "S";
    }

    public static class ChipStateZISO extends AbstractChipState {

        public ChipStateZISO(BlockWorldVector source, ChangedSign sign) {

            super(source, sign, false);
        }

        public ChipStateZISO(BlockWorldVector source, ChangedSign sign, boolean selfTriggered) {
            super(source, sign, selfTriggered);
        }

        @Override
        protected Block getBlock(int pin) {

            switch (pin) {
                case 0:
                    return SignUtil.getFrontBlock(BukkitUtil.toSign(sign).getBlock());
                case 1:
                    BlockFace face = SignUtil.getBack(BukkitUtil.toSign(sign).getBlock());
                    return BukkitUtil.toSign(sign).getBlock().getRelative(face).getRelative(face);
                default:
                    return null;
            }

        }

        @Override
        public boolean getInput(int inputIndex) {

            for (int i = 0; i < getInputCount(); i++)
                if (isValid(i)) if (get(i)) return true;
            return false;
        }

        @Override
        public boolean getOutput(int outputIndex) {

            return get(outputIndex + 1);
        }

        @Override
        public void setOutput(int outputIndex, boolean value) {

            set(outputIndex + 1, value);
        }

        @Override
        public int getInputCount() {

            return 1;
        }

        @Override
        public int getOutputCount() {

            return 1;
        }
    }
}