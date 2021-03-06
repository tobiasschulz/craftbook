package com.sk89q.craftbook.circuits.gates.world.sensors;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.StorageMinecart;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.bukkit.util.BukkitUtil;
import com.sk89q.craftbook.circuits.ic.AbstractIC;
import com.sk89q.craftbook.circuits.ic.AbstractICFactory;
import com.sk89q.craftbook.circuits.ic.ChipState;
import com.sk89q.craftbook.circuits.ic.IC;
import com.sk89q.craftbook.circuits.ic.ICFactory;
import com.sk89q.craftbook.circuits.ic.ICUtil;
import com.sk89q.craftbook.circuits.ic.ICVerificationException;
import com.sk89q.craftbook.util.EnumUtil;
import com.sk89q.craftbook.util.LocationUtil;
import com.sk89q.craftbook.util.RegexUtil;
import com.sk89q.craftbook.util.SignUtil;

/**
 * @author Silthus
 */
public class EntitySensor extends AbstractIC {

    public enum Type {
        PLAYER('P'), ITEM('I'), MOB_HOSTILE('H'), MOB_PEACEFUL('A'), MOB_ANY('M'), ANY('L'), CART('C'),
        CART_STORAGE('S'), CART_POWERED('E');

        public boolean is(Entity entity) {

            switch (this) {
                case PLAYER:
                    return entity instanceof Player;
                case ITEM:
                    return entity instanceof Item;
                case MOB_HOSTILE:
                    return entity instanceof Monster && !(entity instanceof Player);
                case MOB_PEACEFUL:
                    return entity instanceof Animals;
                case MOB_ANY:
                    return entity instanceof Creature;
                case CART:
                    return entity instanceof Minecart;
                case CART_STORAGE:
                    return entity instanceof StorageMinecart;
                case CART_POWERED:
                    return entity instanceof PoweredMinecart;
                case ANY:
                    return true;
            }
            return false;
        }

        private final char shortName;

        private Type(char shortName) {

            this.shortName = shortName;
        }

        public char getCharName() {

            return shortName;
        }

        public static Set<Type> getDetected(String line) {

            Set<Type> types = EnumSet.noneOf(Type.class);

            if (line.trim().isEmpty()) {
                types.add(ANY);
                return types;
            }

            Type type = EnumUtil.getEnumFromString(Type.class, line);
            if (type != null) {
                types.add(type);
            } else {
                for (char aChar : line.toCharArray()) {
                    for (Type aType : Type.values()) {
                        if (aType.getCharName() == aChar) {
                            types.add(aType);
                        }
                    }
                }
            }

            if (types.isEmpty()) {
                types.add(ANY);
            }

            return types;
        }
    }

    private Set<Type> types;

    private Block center;
    private int radius;

    public EntitySensor(Server server, ChangedSign block, ICFactory factory) {

        super(server, block, factory);
    }

    @Override
    public void load() {

        getSign().setLine(3, getSign().getLine(3).toUpperCase());

        // lets get the types to detect first
        types = Type.getDetected(getSign().getLine(3).trim());

        // if the line contains a = the offset is given
        // the given string should look something like that:
        // radius=x:y:z or radius, e.g. 1=-2:5:11
        radius = ICUtil.parseRadius(getSign());
        if (getSign().getLine(2).contains("=")) {
            getSign().setLine(2, radius + "=" + RegexUtil.EQUALS_PATTERN.split(getSign().getLine(2))[1]);
            center = ICUtil.parseBlockLocation(getSign());
        } else {
            getSign().setLine(2, String.valueOf(radius));
            center = SignUtil.getBackBlock(BukkitUtil.toSign(getSign()).getBlock());
        }
        getSign().update(false);
    }

    @Override
    public String getTitle() {

        return "Entity Sensor";
    }

    @Override
    public String getSignTitle() {

        return "ENTITY SENSOR";
    }

    @Override
    public void trigger(ChipState chip) {

        if (chip.getInput(0)) {
            chip.setOutput(0, isDetected());
        }
    }

    protected boolean isDetected() {

        for (Entity entity : LocationUtil.getNearbyEntities(center.getLocation(), radius)) {
            if (entity.isValid()) {
                for (Type type : types)
                    // Check Type
                {
                    if (type.is(entity)) {
                        // Check Radius
                        if (LocationUtil.isWithinRadius(center.getLocation(), entity.getLocation(), radius))
                            return true;
                        break;
                    }
                }
            }
        }
        return false;
    }

    public static class Factory extends AbstractICFactory {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(ChangedSign sign) {

            return new EntitySensor(getServer(), sign, this);
        }

        @Override
        public void verify(ChangedSign sign) throws ICVerificationException {

            ICUtil.verifySignSyntax(sign);
        }

        @Override
        public String getShortDescription() {

            return "Detects specific entity types in a given radius.";
        }

        @Override
        public String[] getLineHelp() {

            String[] lines = new String[] {"radius=x:y:z offset", "Entity Types"};
            return lines;
        }
    }
}
