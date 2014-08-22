package ar.edu.itba.command;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;

public class Pairs {

	public static <L, R> Function<? super Pair<L, R>, R> getRight() {
		return new Function<Pair<L, R>, R>() {
			@Override
			public R apply(Pair<L, R> input) {
				return input.getRight();
			}
		};
	}
}
