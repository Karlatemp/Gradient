package io.github.karlatemp.gradient;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GradientBukkit extends JavaPlugin {
    private static final List<Vector> standColors =
            Stream.of(
                    new Color(0x66ccff),
                    new Color(0xDA8DEE),
                    new Color(0x8E5B1C),
                    new Color(0xCF0EFD),
                    new Color(0xFFFFFF),
                    new Color(0x680484),
                    new Color(0x04C6B1),
                    new Color(0xCDA8D7),
                    new Color(0xDA8DEE),
                    new Color(0xFFBE24)
            ).map(GradientBukkit::toVector).collect(Collectors.toList());

    @NotNull
    @Contract(pure = true, value = "_ -> !null")
    public static Vector toVector(@NotNull Color color) {
        return new Vector(
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        );
    }

    @NotNull
    @Contract(pure = true, value = "_ -> !null")
    public static Color toColor(@NotNull Vector vector) {
        return new Color(
                vector.getBlockX(),
                vector.getBlockY(),
                vector.getBlockZ()
        );
    }

    @Contract(pure = true, value = "_ -> !null;")
    @NotNull
    public static Vector fromBukkit(@NotNull org.bukkit.util.Vector vector) {
        return new Vector(
                vector.getX(), vector.getY(), vector.getZ()
        );
    }

    @Contract(pure = true, value = "_ -> !null;")
    @NotNull
    public static org.bukkit.util.Vector toBukkit(@NotNull Vector vector) {
        return new org.bukkit.util.Vector(
                vector.getX(), vector.getY(), vector.getZ()
        );
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {
        if (sender instanceof Player) {
            Player senderAsPlayer = (Player) sender;
            if (args.length == 0) return true;
            String msg = String.join(" ", args);
            LinkedList<Vector> vectors = new LinkedList<>();
            final int step = 10;
            List<String> chars = msg.chars()
                    .mapToObj(v -> String.valueOf((char) v))
                    .collect(Collectors.toList());
            {
                int s = standColors.size() - 1;
                for (int i = 0; i < s; i++) {
                    Gradient.step(standColors.get(i), standColors.get(i + 1), step, true, vectors::add);
                }
                Gradient.step(standColors.get(s), standColors.get(0), step, true, vectors::add);
            }
            List<Color> colors = vectors.stream().map(GradientBukkit::toColor).collect(Collectors.toList());
            int fc = colors.size();
            for (int i = 0; i < fc; i++) {
                final int index = i;
                getServer().getScheduler().runTaskLater(
                        this, () -> {
                            ComponentBuilder builder = new ComponentBuilder();
                            int start = index;
                            for (String s : chars) {
                                if (start >= colors.size()) {
                                    start -= colors.size();
                                }
                                builder.append(s).color(
                                        ChatColor.of(colors.get(start))
                                );
                                start++;
                            }
                            final BaseComponent[] components = builder.create();
                            String msg0 = new TextComponent(components).toLegacyText();
                            senderAsPlayer.sendTitle(
                                    msg0,
                                    "",
                                    0,
                                    5,
                                    0
                            );
                            senderAsPlayer.spigot().sendMessage(components);
                        },
                        i * 2
                );
            }
        }
        return true;
    }
}
