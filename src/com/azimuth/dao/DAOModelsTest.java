package com.azimuth.dao;

import org.junit.Test;

import com.azimuth.ode.DelaySolver;
import com.azimuth.ode.InitialValueProblem;
import com.azimuth.ode.RungeKuttaSolver;
import com.azimuth.ode.Solver;
import com.azimuth.ode.InitialValueProblem.Solution;
import com.azimuth.plot.Plot2D;
import com.azimuth.plot.Plot2D.Series;


public class DAOModelsTest {

	private final static int MESH_POINT = 10000;
	private final static double START_TIME = 0;
	private final static double END_TIME = 25;
	private final static double[] INITIAL_VALUE = { 0.55 };
	
	@Test
	public void createAnnualForcingPlots(){
		// solve the problem using the Step Method
		Solver solver = new DelaySolver(3, MESH_POINT, new RungeKuttaSolver(0.001));
		// create an intance of a DAO model
		InitialValueProblem problem = DAOModelFactory.createDAOModel(3, 0.7);
		// set initial value conditions
		// sqrt( k-a/b  ) + 0.15
		problem.setInitialValue(new double[]{ Math.sqrt((3.14*(1-0.7))/1.09)+0.15  });
		// set interval
		problem.setLowerBound(START_TIME);
		problem.setUpperBound(END_TIME);
		// solve the problem numerically
		Solution solution = solver.solve(problem);
		// get the solution
		double[] temperature = solution.getValues()[0];
		// get time steps
		double[] timesteps = solution.getTimesteps();
		
		// create plottable data
		// temperature vs time
		Series s = new Series();
		s.setDescription("temperature");
		s.setTitle("temperature");
		s.setXValues(timesteps);
		s.setYValues(temperature);
		s.setXLabel("Time");
		s.setYLabel("Temperature");
		
		// plot results
		Plot2D plot = new Plot2D();
		plot.addSeries(s);
		plot.setTitle("DAO model solved by RK4");
		plot.setXLabel("time");
		plot.setYLabel("temperature");
		plot.render( "./output/DAO_annual_forcing"  );
		
	}
	
	
}
