package pedestrian.test.metrics;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.pedestriansim.metric.component.CollitionCountPerInstant;

public class CollitionPerInstanceMetricTest {
	
	private CollitionCountPerInstant collitionCount;
	
	@Before
	public void setup() {
		collitionCount = new CollitionCountPerInstant();
	}

	@Test
	public void count1Collition(){
		//first iteration
		collitionCount.onIterationStart();
		collitionCount.onCollition(0, 1, 2);
		collitionCount.onIterationEnd();
		
		Assert.assertTrue(collitionCount.getCount() == 1);
	}

	@Test
	public void count2CollitionForProlongedCollition(){
		//first iteration
		collitionCount.onIterationStart();
		collitionCount.onCollition(0, 1, 2);
		collitionCount.onIterationEnd();
		
		//second iteration
		collitionCount.onIterationStart();
		collitionCount.onCollition(0, 1, 2);
		collitionCount.onIterationEnd();
		
		Assert.assertTrue(collitionCount.getCount() == 2);
	}
	
	@Test
	public void count2Collitions(){
		//first iteration
		collitionCount.onIterationStart();
		collitionCount.onCollition(0, 1, 2);
		collitionCount.onIterationEnd();

		//second iteration
		collitionCount.onIterationStart();
		collitionCount.onIterationEnd();
		
		//third iteration
		collitionCount.onIterationStart();
		collitionCount.onCollition(0, 1, 2);
		collitionCount.onIterationEnd();
		
		Assert.assertTrue(collitionCount.getCount() == 2);
	}
	
	@Test
	public void count3CollitionsForDifferentPedestrians(){
		//first iteration
		collitionCount.onIterationStart();
		collitionCount.onCollition(0, 1, 2);
		collitionCount.onIterationEnd();

		//second iteration
		collitionCount.onIterationStart();
		collitionCount.onCollition(0, 1, 3);
		collitionCount.onIterationEnd();
		
		//third iteration
		collitionCount.onIterationStart();
		collitionCount.onCollition(0, 3, 2);
		collitionCount.onIterationEnd();
		
		Assert.assertTrue(collitionCount.getCount() == 3);
	}
	
}
