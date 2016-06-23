package fr.cartooncraft.ping;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * Created by thepoon on 14/06/16.
 */
@Plugin(id = "ping", name = "Ping", version = "0.1")
public class Ping {

	private Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfig;

	PingConfig config;

	public Logger getLogger() {
		return logger;
	}
	@Inject
	private void setLogger(Logger logger) {
		this.logger = logger;
	}

	private CommandSpec scoreboardToggleSpec = CommandSpec.builder()
			.description(Text.of("Toggle latency display in tablist"))
			.permission("ping.scoreboard.toggle")
			.executor(new PingScoreboards(this))
			.build();

	private CommandSpec pingCommandSpec = CommandSpec.builder()
			.description(Text.of("Get your/a player's ping"))
			.executor(new PingCommand())
			.arguments(GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))))
			.child(scoreboardToggleSpec, "scoreboard", "toggle", "toggleScoreboard")
			.build();

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		Sponge.getCommandManager().register(this, pingCommandSpec, "ping", "latency");
		config = new PingConfig(defaultConfig);
		PingScoreboards.initPingObjective();
		Sponge.getScheduler().createTaskBuilder().execute(PingScoreboards::refreshPing).async().interval(1, TimeUnit.SECONDS).name("Ping - Refresh Scoreboard").submit(this);
		onReload();
		getLogger().info("Rush plugin initialized.");
	}

	@Listener
	public void onReload(GameReloadEvent event) {
		onReload();
	}

	@Listener
	public void onStop(GameStoppedServerEvent event) {
		config.saveConfig();
	}

	private void onReload() {
		config.reload();
		if(config.addToScoreboard())
			PingScoreboards.addPingToServerScoreboard();
		else
			PingScoreboards.removePingOfServerScoreboard();
	}

}
