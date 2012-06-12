package com.azimuth.dao;

import com.azimuth.ode.Function;
import com.azimuth.ode.InitialValueProblem;

/**
 * This class creates an instance for DAO. An instance of DAO is a delay
 * differential equation problem.
 * 
 * @author marco
 * 
 */
public class DAOModelFactory {
	
	/**
	 * Global warming may influence the periodicity and amplitude of ENSO.
	 * In this model global warming is modelled via a constant heating on the Pacific DAO systems.
	 * @param delta
	 * @param alpha
	 * @return
	 */
	public static InitialValueProblem createGlobalWarmingModel(
			final double delta, final double alpha) {

		return new InitialValueProblem() {

			private final double beta = 0.009;
			private Function[] derivatives = { new Function() {

				/**
				 * values[0], the current value values[1], the delayed value
				 */
				public double value(double t, double[] values) {
					assert (values.length == 2);
					double currentY = values[0];
					double delayedY = values[1];
					return currentY - Math.pow(currentY, 3) - alpha
							* delayedY + beta;
				}

			}

			};

			@Override
			public Function[] getDerivatives() {
				return derivatives;
			}

		};

	}

	public static InitialValueProblem createAnnualForcingModel(
			final double delta, final double alpha) {

		return new InitialValueProblem() {

			private final static double DELTA = 349;
			private final double k = 3.14;
			private final double b = 1.09;
			private final double a = alpha*k;

			private Function[] derivatives = { new Function() {

				/**
				 * values[0], the current value values[1], the delayed value
				 */
				public double value(double t, double[] values) {
					assert (values.length == 2);
					double currentY = values[0];
					double delayedY = values[1];
					return k * currentY - b * Math.pow(currentY, 3) - a
							* delayedY + getAnnualForcingDerivative(t);
				}
				private double getAnnualForcingDerivative(double time){
					// see Note 27, sinusoidal term for annual forcing
					return -Math.cos(time);
				}

			}

			};

			@Override
			public Function[] getDerivatives() {
				return derivatives;
			}

		};

	}

	public static InitialValueProblem createDAOModel(final double delta,
			final double alpha) {
		return new InitialValueProblem() {

			private Function[] derivatives = { new Function() {

				/**
				 * values[0], the current value values[1], the delayed value
				 */
				public double value(double t, double[] values) {
					assert (values.length == 2);
					double currentY = values[0];
					double delayedY = values[1];
					return currentY - Math.pow(currentY, 3) - alpha * delayedY;
				}
			} };

			@Override
			public Function[] getDerivatives() {
				return derivatives;
			}

		};

	}

}
