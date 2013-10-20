package ar.edu.itba.pedestriansim.parser;

public class ParserUtil {

	public static final float[] parseArray(String stringArray) {
		String[] valuesString = stringArray.substring(1, stringArray.length() - 1).split(" ");
		float[] values = new float[valuesString.length];
		int i = 0;
		for (String s : valuesString) {
			values[i++] = Float.parseFloat(s);
		}
		return values;
	}
}
