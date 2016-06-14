package fr.cartooncraft.ping;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import static org.spongepowered.api.text.TextTemplate.arg;

/**
 * Created by thepoon on 14/06/16.
 */
public class PingCommand implements CommandExecutor {

	TextTemplate pingTemplate = TextTemplate.of(TextColors.GREEN, arg("player").style(TextStyles.BOLD), "'s latency is ", arg("ping").style(TextStyles.BOLD));

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Player p = null;
		if(args.getOne("player").isPresent()) {
			p = (Player) args.getOne("player").get();
		}
		else if(src instanceof Player) {
			p = (Player)src;
		}
		else {
			return CommandResult.empty();
		}

		src.sendMessage(pingTemplate.apply(ImmutableMap.of("player", p.getName(), "ping", p.getConnection().getLatency())).build());
		return CommandResult.success();
	}
}
