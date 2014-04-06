package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;

public interface Metric {

	void onIterationStart();
	
	void onIterationEnd();
	
	void appendResults(FileWriter writer) throws IOException;
	
}
