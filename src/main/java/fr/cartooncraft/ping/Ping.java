package fr.cartooncraft.ping;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

/**
 * Created by thepoon on 14/06/16.
 */
@Plugin(id = "ping", name = "Ping", version = "0.1")
public class Ping {

	private CommandSpec pingCommandSpec = CommandSpec.builder()
			.description(Text.of("Get your/a player's ping"))
			.executor(new PingCommand())
			.arguments(GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))))
			.build();

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		Sponge.getCommandManager().register(this, pingCommandSpec, "ping");
	}

}
