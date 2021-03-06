package fr.cartooncraft.ping;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by thepoon on 23/06/16.
 */
public class PingScoreboards implements CommandExecutor {

	private static Objective pingObjective;

	private Ping plugin;

	public static void addPing(Scoreboard sb) {
		if(!sb.getObjective("Ping").isPresent())
			sb.addObjective(pingObjective);
		sb.updateDisplaySlot(pingObjective, DisplaySlots.LIST);
	}

	public static void removePing(Scoreboard sb) {
		if(sb.getObjective("Ping").isPresent())
			sb.removeObjective(pingObjective);
	}

	public static Objective getObjective() {
		return pingObjective;
	}

	static void addPingToServerScoreboard() {
		addPing(Sponge.getServer().getServerScoreboard().get());
	}

	static void removePingOfServerScoreboard() {
		removePing(Sponge.getServer().getServerScoreboard().get());
	}

	static void setServerScoreboard(boolean state) {
		if(state)
			addPingToServerScoreboard();
		else
			removePingOfServerScoreboard();
	}

	static void refreshPing() {
		for(Score s : pingObjective.getScores().values().toArray(new Score[pingObjective.getScores().size()])) {
			pingObjective.removeScore(s);
		}
		Sponge.getServer().getOnlinePlayers().forEach((player) -> pingObjective.getOrCreateScore(player.getDisplayNameData().displayName().get()).setScore(player.getConnection().getLatency()));
	}

	static void initPingObjective() {
		if(pingObjective != null)
			return;
		if(Sponge.getServer().getServerScoreboard().get().getObjective("Ping").isPresent())
			pingObjective = Sponge.getServer().getServerScoreboard().get().getObjective("Ping").get();
		else
			pingObjective = Objective.builder().objectiveDisplayMode(ObjectiveDisplayModes.INTEGER).criterion(Criteria.DUMMY).name("Ping").displayName(Text.of("Ping")).build();
	}

	public PingScoreboards(Ping plugin) {
		this.plugin = plugin;
	}

	private Text enabledScoreboard = Text.builder("Players latencies are now displayed on scoreboard!").color(TextColors.GREEN).build();
	private Text disabledScoreboard = Text.builder("Players latencies are not displayed on scoreboard anymore!").color(TextColors.GREEN).build();

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		boolean newState = plugin.config.toggleScoreboard();
		if(newState)
			src.sendMessage(enabledScoreboard);
		else
			src.sendMessage(disabledScoreboard);

		PingScoreboards.setServerScoreboard(newState);
		return CommandResult.success();
	}
}