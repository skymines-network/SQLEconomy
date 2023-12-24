package be.renaud11232.plugins.sqleconomy.discord;

import org.bukkit.OfflinePlayer;
import java.awt.*;

public class SQLEconomyWebhook {
    public void create(OfflinePlayer player, String amount, double newBalance){
        new Thread(() -> {
            DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1115127248081657916/NyFZuGYLDI3LjcILotEwj2AZNNQgLQKv_4g4643JnnhwKdY3uMCd3LdBua4EP9CKiu3s");
            webhook.setContent("Any message!");
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Economy Audited")
                    .setColor(Color.BLUE)
                    .addField("Player: ", player.getName(), true)
                    .addField("UUID: ", player.getUniqueId().toString(), true)
                    .addField("Amount changed: ", amount, true)
                    .addField("New Balance: ", String.valueOf(newBalance), true));
            try {
                webhook.execute(); // Handle exception
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
