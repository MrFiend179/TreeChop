package mc.treechop.treechop;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class TreeChop extends JavaPlugin implements Listener {
    private boolean isEnabled = true;
    private String worldName = "";
    private int rewardAmount = 50;
    private final Map<Location, Integer> pillarCounters = new HashMap<>();
    private final Map<Location, Set<Location>> fallenLeavesMap = new HashMap<>();
    private final Map<Location, Set<Location>> fallenLogsMap = new HashMap<>();


    private FileConfiguration config;

    @Override
    public void onEnable() {
        getLogger().info("###############################################");
        getLogger().info("#                                             #");
        getLogger().info("#             Tree Chopper v1.0.1             #");
        getLogger().info("#               Status: Started               #");
        getLogger().info("#                Made by Fiend                #");
        getLogger().info("#                                             #");
        getLogger().info("###############################################");
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
        getCommand("treechopper").setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("###############################################");
        getLogger().info("#                                             #");
        getLogger().info("#             Tree Chopper v1.0.1             #");
        getLogger().info("#               Status: Stopped               #");
        getLogger().info("#                Made by Fiend                #");
        getLogger().info("#                                             #");
        getLogger().info("###############################################");
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("treechopper")) {
            if (args.length == 0) {
                sender.sendMessage("Usage: /treechopper <enable|disable|worldname|reward>");
                return true;
            }

            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "enable":
                    isEnabled = true;
                    sender.sendMessage("§a§l| TreeChopper enabled.");
                    saveConfig();
                    break;
                case "disable":
                    isEnabled = false;
                    sender.sendMessage("§c§l| TreeChopper disabled.");
                    saveConfig();
                    break;
                case "forcestop":
                    setEnabled(false);
                    sender.sendMessage("§c§l| TreeChopper Stopped Forcefully.");
                    break;
                case "worldname":
                    if (args.length >= 2) {
                        worldName = args[1];
                        sender.sendMessage("§a§l| §aTreeChopper world name set to: §6§l" + worldName);
                        saveConfig();
                    } else {
                        sender.sendMessage("Usage: /treechopper worldname <name>");
                    }
                    break;
                case "reward":
                    if (args.length >= 2) {
                        try {
                            rewardAmount = Integer.parseInt(args[1]);
                            sender.sendMessage("§a§l| §aTreeChopper reward amount set to: §6§l" + rewardAmount);
                            saveConfig();
                        } catch (NumberFormatException e) {
                            sender.sendMessage("Please provide a valid number for the reward amount.");
                        }
                    } else {
                        sender.sendMessage("Usage: /treechopper reward <amount>");
                    }
                    break;
                default:
                    sender.sendMessage("Usage: /treechopper <enable|disable|worldname|reward>");
            }
            return true;
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("treechopper")) {
            if (args.length == 1) {
                List<String> suggestions = new ArrayList<>();
                String input = args[0].toLowerCase();

                // Add subcommand suggestions based on input
                List<String> subCommands = Arrays.asList("enable", "disable", "forcestop", "worldname", "reward");
                for (String subCommand : subCommands) {
                    if (subCommand.startsWith(input)) {
                        suggestions.add(subCommand);
                    }
                }
                return suggestions;
            }
        }
        return null;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!isEnabled) {
            System.out.println("Plugin Disabled!");
        } else {
            World world = event.getBlock().getWorld();
            String eventName = worldName.equalsIgnoreCase(world.getName()) ? worldName : "default";

            if(!event.getPlayer().hasPermission("TreeChopper.ChopRewards") && isLog(block.getType()) && world.getName().equalsIgnoreCase(eventName)){
                event.getPlayer().sendMessage("§c§l| You don't have permission to do this.");
                return;
            }
            if (isLog(block.getType()) && world.getName().equalsIgnoreCase(eventName) && event.getPlayer().hasPermission("TreeChopper.ChopRewards")) {
                event.setDropItems(false);

                final Location location = block.getLocation();
                int aboveCount = countLogsAbove(location, block.getType());
                int belowCount = countLogsBelow(location, block.getType());
                int pillarHeight = aboveCount + belowCount + 2;
                Location bottomLocation = getBottomLocation(location);


                int counter = pillarCounters.getOrDefault(bottomLocation, pillarHeight);
                if (counter > 0) {
                    counter--;
                    pillarCounters.put(bottomLocation, counter);
                    getServer().getScheduler().runTaskLater(this, () -> {
                        event.getBlock().setType(Material.SPRUCE_LOG);
                    }, 2);
                    if (counter == 0) {
                        String Player = event.getPlayer().getName();
                        int rewardamountplr = rewardAmount;
                        getServer().dispatchCommand(getServer().getConsoleSender(), "eco give " + Player + " " + rewardamountplr);
//                        getServer().dispatchCommand(getServer().getConsoleSender(), "eco give " + Player + " 60");
                        pillarCounters.remove(bottomLocation);


                        getServer().getScheduler().runTaskLater(this, () -> {
                            event.getBlock().setType(Material.SPRUCE_LOG);
                        }, 2);

                        getServer().getScheduler().runTaskLater(this, () -> {


                            float xRangeMin = -0.2f;
                            float xRangeMax = 0.2f;
                            float yRangeMin = 0.5f;
                            float yRangeMax = 1.0f;
                            float zRangeMin = -0.2f;
                            float zRangeMax = 0.2f;
                            int radius = 4;
                            int height = 20;
                            for (int xOffset = -radius; xOffset <= radius; xOffset++) {
                                for (int yOffset = -height; yOffset <= height; yOffset++) {
                                    for (int zOffset = -radius; zOffset <= radius; zOffset++) {
                                        Block relativeBlock = block.getRelative(xOffset, yOffset, zOffset);
                                        if (relativeBlock.getType() == Material.SPRUCE_LEAVES) {

                                            float x = xRangeMin + (float) Math.random() * (xRangeMax - xRangeMin);
                                            float y = yRangeMin + (float) Math.random() * (yRangeMax - yRangeMin);
                                            float z = zRangeMin + (float) Math.random() * (zRangeMax - zRangeMin);

                                            FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(relativeBlock.getLocation(), relativeBlock.getBlockData());
                                            fallingBlock.setDropItem(false);
                                            fallingBlock.setVelocity(new Vector(x, y, z));

                                            relativeBlock.setType(Material.AIR);
                                        }
                                    }
                                }
                            }

                            int radius1 = 2;
                            int height1 = 10;
                            for (int xOffset = -radius1; xOffset <= radius1; xOffset++) {
                                for (int yOffset = -height1; yOffset <= height1; yOffset++) {
                                    for (int zOffset = -radius1; zOffset <= radius1; zOffset++) {
                                        Block relativeBlock = block.getRelative(xOffset, yOffset, zOffset);
                                        if (relativeBlock.getType() == Material.SPRUCE_LOG) {
                                            event.setDropItems(false);
                                            float x = xRangeMin + (float) Math.random() * (xRangeMax - xRangeMin);
                                            float y = yRangeMin + (float) Math.random() * (yRangeMax - yRangeMin);
                                            float z = zRangeMin + (float) Math.random() * (zRangeMax - zRangeMin);

                                            FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(relativeBlock.getLocation(), relativeBlock.getBlockData());
                                            fallingBlock.setDropItem(false);
                                            fallingBlock.setVelocity(new Vector(x, y, z));

                                            relativeBlock.setType(Material.AIR);
                                        }
                                    }
                                }
                            }
                            int radiusclr = 5;
                            getServer().getScheduler().runTaskLater(this, () -> {
                                for (int xOffset = -radiusclr; xOffset <= radiusclr; xOffset++) {
                                    for (int yOffset = -height; yOffset <= height; yOffset++) {
                                        for (int zOffset = -radiusclr; zOffset <= radiusclr; zOffset++) {
                                            Block relativeBlock = block.getRelative(xOffset, yOffset, zOffset);
                                            if (relativeBlock.getType() == Material.SPRUCE_LOG || relativeBlock.getType() == Material.SPRUCE_LEAVES) {
                                                relativeBlock.setType(Material.AIR);
                                            }
                                        }
                                    }
                                }
                            }, 60);
                        }, 3);
                        makeLeavesFall(location);
                        makeLogsFall(location);
                        resetLeaves(location);
                        resetLogs(location);

                    } else {
                        event.getPlayer().sendMessage("§6§l| §fBreak block §6§l" + counter + " §6more time(s) §fto cut down a tree.");
                    }
                }

            }
        }

    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        World world = event.getBlock().getWorld();
        String eventName = worldName.equalsIgnoreCase(world.getName()) ? worldName : "default";

        if (block.getType() == Material.SPRUCE_LEAVES && world.getName().equalsIgnoreCase(eventName)) {
            // Check if the block below is solid
            Block blockBelow = block.getLocation().subtract(0, 1, 0).getBlock();
            if (blockBelow.getType().isSolid() && blockBelow.getType() != Material.SPRUCE_LEAVES && blockBelow.getType() != Material.SPRUCE_LOG) {
                event.setCancelled(true);
                block.setType(Material.AIR);
            }
        }
    }



    private void makeLeavesFall(Location location) {
        Set<Location> fallenLeaves = fallenLeavesMap.getOrDefault(location, new HashSet<>());
        for (int xOffset = -4; xOffset <= 4; xOffset++) {
            for (int yOffset = -20; yOffset <= 20; yOffset++) {
                for (int zOffset = -4; zOffset <= 4; zOffset++) {
                    Location checkLocation = location.clone().add(xOffset, yOffset, zOffset);
                    Block block = checkLocation.getBlock();
                    if (block.getType() == Material.SPRUCE_LEAVES) {
                        fallenLeaves.add(checkLocation);
                    }
                }
            }
        }
        fallenLeavesMap.put(location, fallenLeaves);
    }

    private void makeLogsFall(Location location) {
        Set<Location> fallenLogs = fallenLogsMap.getOrDefault(location, new HashSet<>());
        for (int xOffset = -4; xOffset <= 4; xOffset++) {
            for (int yOffset = -20; yOffset <= 20; yOffset++) {
                for (int zOffset = -4; zOffset <= 4; zOffset++) {
                    Location checkLocation = location.clone().add(xOffset, yOffset, zOffset);
                    Block block = checkLocation.getBlock();
                    if (block.getType() == Material.SPRUCE_LOG) {
                        fallenLogs.add(checkLocation);
                    }
                }
            }
        }
        fallenLogsMap.put(location, fallenLogs);
    }

    private void resetLeaves(Location location) {
        Set<Location> fallenLeaves = fallenLeavesMap.get(location);
        if (fallenLeaves != null) {
            getServer().getScheduler().runTaskLater(this, () -> {
                for (Location leafLocation : fallenLeaves) {
                    leafLocation.getBlock().setType(Material.SPRUCE_LEAVES);
                }
                fallenLeavesMap.remove(location);
            }, 100);
        }
    }

    private void resetLogs(Location location) {
        Set<Location> fallenLogs = fallenLogsMap.get(location);
        if (fallenLogs != null) {
            getServer().getScheduler().runTaskLater(this, () -> {
                for (Location logLocation : fallenLogs) {
                    logLocation.getBlock().setType(Material.SPRUCE_LOG);
                }
                fallenLogsMap.remove(location);
            }, 100);
        }
    }

    private Location getBottomLocation(Location location) {
        while (location.getBlock().getType() == Material.SPRUCE_LOG) {
            location.subtract(0, 1, 0);
        }
        location.add(0, 1, 0);
        return location;
    }
    private int countLogsAbove(Location location, Material logType) {
        int count = 0;
        Location checkLocation = location.clone().add(0, 1, 0); // Start checking one block above

        while (checkLocation.getBlock().getType() == logType) {
            count++;
            checkLocation.add(0, 1, 0); // Move up one block
        }

        return count;
    }

    private int countLogsBelow(Location location, Material logType) {
        int count = 0;
        Location checkLocation = location.clone().subtract(0, 1, 0); // Start checking one block below

        while (checkLocation.getBlock().getType() == logType) {
            count++;
            checkLocation.subtract(0, 1, 0); // Move down one block
        }

        return count;
    }

    private boolean isLog(Material material) {
        return material == Material.SPRUCE_LOG;
    }

    private void loadConfig() {
        // Create the plugin's data folder if it doesn't exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Create the config file if it doesn't exist
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        // Load config from file
        FileConfiguration config = getConfig();
        isEnabled = config.getBoolean("enabled", true);
        worldName = config.getString("worldName", "lobby");
        rewardAmount = config.getInt("rewardAmount", 50); // Load reward amount with default value
    }

    public void saveConfig() {
        FileConfiguration config = getConfig();
        config.set("enabled", isEnabled);
        config.set("worldName", worldName);
        config.set("rewardAmount", rewardAmount);

        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


