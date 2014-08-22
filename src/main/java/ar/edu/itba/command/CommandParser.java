package ar.edu.itba.command;

import static com.google.common.collect.Iterables.tryFind;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CommandParser {

	private Map<String, CommandParam> _params = Maps.newHashMap();

	public CommandParser param(CommandParam param) {
		_params.put(param.getKey(), param);
		return this;
	}

	public ParsedCommand parse(String[] args) {
		List<Pair<String, String>> keyAndValues = asKeyAndValue(args);
		Map<CommandParam, String> errorParams = Maps.newHashMap();
		Map<String, String> values = Maps.newHashMap();
		for (CommandParam param : _params.values()) {
			Optional<Pair<String, String>> matchedPair = tryFind(keyAndValues, new StartsWith(param.getKey()));
			Optional<String> match = matchedPair.transform(Pairs.<String, String>getRight());
			Optional<String> error = validateMatchvalue(param, match);
			if (error.isPresent()) {
				errorParams.put(param, error.get());
			} else {
				values.put(param.getKey(), match.or(param.getDefaultValue()).orNull());
			}
		}
		return new ParsedCommand(errorParams, values);
	}

	private List<Pair<String, String>> asKeyAndValue(String[] args) {
		List<Pair<String, String>> keyAndValues = Lists.newLinkedList();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-") && i + 1 < args.length) {
				keyAndValues.add(Pair.of(args[i], args[i + 1]));
				i++;
			}
		}
		return keyAndValues;
	}

	private Optional<String> validateMatchvalue(CommandParam param, Optional<String> match) {
		if (param.isRequired() && !match.isPresent()) {
			return Optional.of("El valor es requerido");
		}
		if (!param.getContraints().isEmpty() && match.isPresent() && !param.getContraints().contains(match.get())) {
			return Optional.of("El valor tiene ser uno de los siguientes: " + param.getContraints());
		}
		return Optional.absent();
	}

	public String getHelp() {
		List<String> messages = Lists.newArrayList();
		for (CommandParam param : _params.values()) {
			String message = param.getKey() + ": ";
			message += param.isRequired() ? "{Req}" : "{Opt}";
			message += " " + param.getMessage().or("");
			if (!param.getContraints().isEmpty()) {
				message += ". Valores: " + param.getContraints();
			}
			if (param.getDefaultValue().isPresent()) {
				message += ". Default: " + param.getDefaultValue().get(); 
			}
			messages.add(message);
		}
		return StringUtils.join(messages, "\n");
	}

	private static final class StartsWith implements Predicate<Pair<String, String>> {
		private final String _prefix;

		public StartsWith(String prefix) {
			_prefix = prefix;
		}

		@Override
		public boolean apply(Pair<String, String> input) {
			return input.getLeft().startsWith(_prefix);
		}
	}

}
