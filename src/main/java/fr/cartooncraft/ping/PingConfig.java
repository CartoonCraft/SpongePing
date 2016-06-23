package fr.cartooncraft.ping;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by thepoon on 23/06/16.
 */
public class PingConfig {

	ConfigurationLoader<CommentedConfigurationNode> configManager;
	ConfigurationNode rootNode;
	Path configPath;

	public PingConfig(Path configPath) {
		this.configPath = configPath;
	}

	public void reload() {
		configManager = HoconConfigurationLoader.builder().setPath(configPath).build();
		try {
			rootNode = configManager.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean addToScoreboard() {
		return rootNode.getNode("scoreboard", "refresh", "enabled").getBoolean(true);
	}

	public boolean toggleScoreboard() {
		if(addToScoreboard())
			rootNode.getNode("scoreboard", "refresh", "enabled").setValue(false);
		else
			rootNode.getNode("scoreboard", "refresh", "enabled").setValue(true);

		try {
			configManager.save(rootNode);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return addToScoreboard();
		}
	}

	void saveConfig() {
		try {
			configManager.save(rootNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
