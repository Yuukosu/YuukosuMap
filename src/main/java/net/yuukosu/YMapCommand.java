package net.yuukosu;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;

public class YMapCommand extends Command {

    public YMapCommand() {
        super(
                "ymap",
                "YuukosuMap コマンド",
                "/ymap (arguments...)",
                new ArrayList<String>() {{
                    this.add("ゆうこすマップ");
                    this.add("map");
                    this.add("マップ");
                }}
        );
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                this.showHelp(player);
                return true;
            } else if (args[0].equalsIgnoreCase("add")) {
                if (args.length > 1) {
                    String name = args[1];

                    if (YuukosuMap.addLocation(name, player.getLocation())) {
                        YuukosuMap.saveLocations();
                        player.sendMessage(ChatColor.GREEN + name + " という場所を地図に追加したよ！");
                    } else {
                        player.sendMessage(ChatColor.RED + name + " という場所はすでに地図に載っているよ！");
                    }

                    return true;
                }

                player.sendMessage(ChatColor.RED + "使い方: /ymap add (新しい場所の名前)");
                return true;
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (args.length > 1) {
                    String name = args[1];

                    if (YuukosuMap.deleteLocation(name)) {
                        YuukosuMap.saveLocations();
                        player.sendMessage(ChatColor.GREEN + name + " という場所を地図から消去したよ！");
                    } else {
                        player.sendMessage(ChatColor.RED + name + " という場所は存在しないよ！");
                    }

                    return true;
                }

                player.sendMessage(ChatColor.RED + "使い方: /ymap delete (場所の名前)");
                return true;
            } else if (args[0].equalsIgnoreCase("show")) {
                if (args.length > 1) {
                    String name = args[1];

                    if (YuukosuMap.getLocations().containsKey(name)) {
                        Location loc = YuukosuMap.getLocations().get(name);

                        player.sendMessage("場所の名前: " + ChatColor.GREEN + name);
                        player.sendMessage("   ワールド名: " + ChatColor.GREEN + loc.getWorld().getName());
                        player.sendMessage("   バイオーム: " + ChatColor.GREEN + loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockZ()).name());
                        player.sendMessage("   X: " + ChatColor.GREEN + YuukosuMap.getLocations().get(name).getBlockX());
                        player.sendMessage("   Y: " + ChatColor.GREEN + YuukosuMap.getLocations().get(name).getBlockY());
                        player.sendMessage("   Z: " + ChatColor.GREEN + YuukosuMap.getLocations().get(name).getBlockZ());
                    } else {
                        player.sendMessage(ChatColor.RED + name + " という場所は存在しないよ！");
                    }

                    return true;
                }

                player.sendMessage(ChatColor.RED + "使い方: /ymap show (場所の名前)");
                return true;
            } else if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage(ChatColor.YELLOW + "[" + YuukosuMap.locations.size() + " 箇所の座標]");
                for (Map.Entry<String, Location> entry : YuukosuMap.locations.entrySet()) {
                    player.sendMessage("場所の名前: " + ChatColor.GREEN + entry.getKey());
                    player.sendMessage("   ワールド名: " + ChatColor.GREEN + entry.getValue().getWorld().getName());
                    player.sendMessage("   バイオーム: " + ChatColor.GREEN + entry.getValue().getWorld().getBiome(entry.getValue().getBlockX(), entry.getValue().getBlockZ()).name());
                    player.sendMessage("   X: " + ChatColor.GREEN + entry.getValue().getBlockX());
                    player.sendMessage("   Y: " + ChatColor.GREEN + entry.getValue().getBlockY());
                    player.sendMessage("   Z: " + ChatColor.GREEN + entry.getValue().getBlockZ());
                    player.sendMessage("");
                }

                return true;
            } else if (args[0].equalsIgnoreCase("tracking")) {
                if (args.length > 1) {
                    String name = args[1];

                    if (YuukosuMap.getLocations().containsKey(name)) {
                        if (player.getWorld() == YuukosuMap.getLocations().get(name).getWorld()) {
                            PlayerManager playerManager = YuukosuMap.getPlayerManager(player);

                            if (playerManager != null) {
                                playerManager.tracking(name);
                                player.sendMessage(ChatColor.GREEN + name + " をトラッキングしました！");
                            } else {
                                player.kickPlayer(ChatColor.RED + "プレイヤーデータが読み込まれてませんでした。\nサーバーに入り直してください！");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + name + " をトラッキングすには、" + YuukosuMap.getLocations().get(name).getWorld().getName() + " というワールドにいる必要があります。");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + name + " という場所は存在しないよ！");
                    }

                    return true;
                }

                player.sendMessage(ChatColor.RED + "使い方: /ymap tracking (場所の名前)");
                return true;
            } else if (args[0].equalsIgnoreCase("untracking")) {
                PlayerManager playerManager = YuukosuMap.getPlayerManager(player);

                if (playerManager != null) {
                    playerManager.untracking();
                }

                player.sendMessage(ChatColor.GREEN + "トラッキングを解除しました！");
                return true;
            }
        }

        this.showHelp(player);
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "[ゆうこすマップの使い方]");
        sender.sendMessage(ChatColor.GREEN + " - /ymap help | 使い方を表示するよ！");
        sender.sendMessage(ChatColor.GREEN + " - /ymap add (新しい場所の名前) | 新しく座標を地図に追加するよ！");
        sender.sendMessage(ChatColor.GREEN + " - /ymap delete (登録済みの場所の名前) | 登録されている場所を地図から消去するよ！");
        sender.sendMessage(ChatColor.GREEN + " - /ymap show (登録済みの場所の名前) | 登録されている場所の座標を表示するよ！");
        sender.sendMessage(ChatColor.GREEN + " - /ymap list | 登録されているすべての場所の座標を表示するよ！");
        sender.sendMessage(ChatColor.GREEN + " - /ymap tracking (登録済みの場所の名前) | 登録されている場所をトラッキングするよ！");
        sender.sendMessage(ChatColor.GREEN + " - /ymap untracking | トラッキングを解除するよ！");
    }
}
