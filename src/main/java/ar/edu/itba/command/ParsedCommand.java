package ar.edu.itba.command;

import static com.google.common.collect.Collections2.transform;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class ParsedCommand {

	private final Map<CommandParam, String> _errorMessages = Maps.newHashMap();
	private final Map<String, String> _values = Maps.newHashMap();

	ParsedCommand(Map<CommandParam, String> errorMessages, Map<String, String> values) {
		_errorMessages.putAll(errorMessages);
		_values.putAll(values);
	}

	public boolean hasErrors() {
		return !_errorMessages.isEmpty();
	}

	public String getErrorString() {
		Preconditions.checkState(!_errorMessages.isEmpty());
		return StringUtils.join(transform(_errorMessages.entrySet(), getErrorMessage()), "\n");
	}

	public String param(String key) {
		return Preconditions.checkNotNull(paramOpt(key));
	}

	public String paramOpt(String key) {
		return _values.get(key);
	}

	public boolean hasParam(String key) {
		return paramOpt(key) != null;
	}

	private Function<Entry<CommandParam, String>, String> getErrorMessage() {
		return new Function<Map.Entry<CommandParam, String>, String>() {
			@Override
			public String apply(Entry<CommandParam, String> input) {
				return "[ERROR] " + input.getKey().getKey() + ": " + input.getValue();
			}
		};
	}
}
