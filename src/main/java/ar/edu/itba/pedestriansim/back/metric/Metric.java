package ar.edu.itba.pedestriansim.back.metric;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public interface Metric {

	void onIterationStart();
	
	void update(float miliseconds, Serializable p1, Serializable p2);
	
	void onIterationEnd();
	
	void appendResults(FileWriter writer) throws IOException;
}
