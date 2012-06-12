package com.azimuth.dao;


import com.azimuth.Settings;
import com.azimuth.ode.DelaySolver;
import com.azimuth.ode.Function;
import com.azimuth.ode.InitialValueProblem;
import com.azimuth.ode.InitialValueProblem.Solution;
import com.azimuth.ode.RungeKuttaSolver;
import com.azimuth.ode.Solver;
import com.azimuth.plot.Plot2D;
import com.azimuth.plot.Plot2D.Series;

public class DAOTest {

	public static final String OUTPUT_DIR = "./output";
	public static final String RESOURCE_DIR = "./resources";
	public static final String TEST_NAME = "dao";

	// private static double h = 0.025;
	private static double alpha = 0.75;
	private static double[] y0 = {0.55};
	private static double t0 = 0;

	private static int n = 10000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// read parameters
		// args[0] = property file name
		if ( args!=null && args.length > 0 ){
			// get properties filename
			String propertiesFilename = args[0];
			Settings.load(propertiesFilename);
		}
		

		// test parameters
		// delays
		double[] delta = { 1., 1.6, 2., 4. };
		double[] tn = { 25, 50, 50, 100 };
		
		// double[] delta = { 1.6 };
		// double[] tn = { 25 };


		

		// iterate through different values for delta and tn
		for (int i = 0; i < delta.length; i++) {
			// solve the problem using the Step Method
			Solver solver = new DelaySolver(delta[i], n, new RungeKuttaSolver(0.001));
			// create an intance of a DAO model
			InitialValueProblem problem = DAOModelFactory.createDAOModel(delta[i],alpha);
			// set initial value conditions
			problem.setInitialValue(y0);
			// set interval
			problem.setLowerBound(t0);
			problem.setUpperBound(tn[i]);
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
			plot.render( "./output/DAO_timeVStemperature_delta" + delta[i]  );
			
			
			// derivative vs temperature
			Function derivative = problem.getDerivatives()[0];
			double delay = delta[i];
			double[] values = new double[ temperature.length ];
			for (int j=0; j<values.length; j++){
				double t = timesteps[j];
				double ynow = temperature[j];
				double ydelay;
				if ( t-delay<=t0 ){
					ydelay = problem.getInitialValue()[0];
				} else {
					// (int) Math.floor( n*(1+(t/tau)) )
					double h = delay/n;
					ydelay = temperature[ (int)Math.floor( (t-delay)/h )  ];
				}
				values[j] = derivative.value(t, new double[]{ynow, ydelay});
			}
			
			Series phase = new Series();
			phase.setDescription("Phase protrait for DAO model");
			phase.setTitle("derivVStemp");
			phase.setXValues(temperature);
			phase.setYValues(values);
			phase.setXLabel("Temperature");
			phase.setYLabel("dT/dt");
			
			// plot
			Plot2D phasePlot = new Plot2D();
			phasePlot.addSeries(phase);
			phasePlot.setTitle("DAO model solved by RK4");
			phasePlot.setXLabel("temperature");
			phasePlot.setYLabel("dT/dt");
			phasePlot.render( "./output/DAO_tempetatureVSderivative_delta" + delta[i]  );
			

			
			
		}

	}

}
