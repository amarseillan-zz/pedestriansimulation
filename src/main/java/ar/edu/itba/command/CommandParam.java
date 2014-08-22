package ar.edu.itba.command;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class CommandParam {

	private String _key;
	private boolean _required = false;
	private List<String> _contraints = Lists.newLinkedList();
	private Optional<String> defaultValue = Optional.absent();
	private Optional<String> _message = Optional.absent();

	public CommandParam(String key) {
		_key = key;
	}

	public String getKey() {
		return _key;
	}

	public CommandParam required() {
		_required = true;
		return this;
	}

	public boolean isRequired() {
		return _required;
	}

	public CommandParam constrained(String... values) {
		_contraints.addAll(Arrays.asList(values));
		return this;
	}

	public List<String> getContraints() {
		return _contraints;
	}

	public CommandParam defaultValue(String value) {
		defaultValue = Optional.of(value);
		return this;
	}

	public Optional<String> getDefaultValue() {
		return defaultValue;
	}

	public CommandParam message(String message) {
		_message = Optional.of(message);
		return this;
	}

	public Optional<String> getMessage() {
		return _message;
	}
}
