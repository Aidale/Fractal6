package fractal;

import java.util.HashMap;
/**
 * This class is a helper class that allows the programmer to declare a "stopwatch" for any task to 
 * measure how long that task takes. Before termination of the program, {@code printReport()}
 * should be called to show the average and total time each task. 
 * 
 * @author aidan
 *
 */
public class Benchmark
{
	// this stores the name and associated time of each task
	private static HashMap<String, Benchmark.UpdateTimer> bench = new HashMap<String, Benchmark.UpdateTimer>();

	/**
	 * For every task we wish to measure, we create a name for that task and pass them all into
	 * this method. This method then takes all of those strings and creates an associated 
	 * {@code UpdateTimer} for each of them.
	 * @param strings
	 */
	public static void init(String... strings)
	{
		System.out.println();
		for (String s : strings)
		{
			bench.put(s, new UpdateTimer());
		}
	}

	/**
	 * Logs the time it took task "s" to complete
	 * 
	 * @param s
	 * @param time
	 */
	private static void addTime(String s, long time)
	{
		UpdateTimer timer = bench.get(s);
		timer.updates++;
		timer.totalTime += time;
	}

	/**
	 * Given the name of the task, "s", this method find the average time that that task took to
	 * complete, and returns that time in nanoseconds.
	 * 
	 * @param s
	 * @return the average time (in nanoseconds) for the task of name "s"
	 */
	private static int getAverageTime(String s)
	{
		UpdateTimer timer = bench.get(s);
		if (timer.updates == 0)
			return -1;
		return (int) (timer.totalTime / timer.updates);
	}

	/**
	 * Formats the long value passed in, commas every third value for readability. 
	 * 
	 * @param value
	 * @return the formatted String of the long value
	 */
	private static String format(long value)
	{
		String rev = reverse(Long.toString(value));
		int start = rev.indexOf(".") + 1;

		for (int i = start; i < rev.length(); i++)
		{
			if (i % 4 == 2 && i != rev.length() - 1)
			{
				rev = rev.substring(0, i + 1) + "," + rev.substring(i + 1);
				i++;
			}
		}
		return reverse(rev);
	}

	/**
	 * Reverses str
	 * 
	 * @param str
	 * @return the reverse of str
	 */
	private static String reverse(String str)
	{
		String reverse = "";
		for (int i = 0; i < str.length(); i++)
		{
			reverse += str.charAt(str.length() - i - 1);
		}
		return reverse;
	}

	/**
	 * Prints out the average and total time for all the operations that have a total time not equal
	 * to zero.
	 */
	public static void printReport()
	{
		System.out.println("Report: nanoseconds per operation: (average time)/(total time)");
		for (String s : bench.keySet())
		{
			if (bench.get(s).totalTime != 0)
			{
				System.out.println(s + ": " + format(getAverageTime(s)) + " / " + format((bench.get(s).totalTime)));
			}
		}
	}

	/**
	 * Starts the timer for the task of name "s".
	 * Warning: calling this method while another task's timer is active will interfere with that timer
	 * and will not give authentic results as to the actual speed of that task.
	 * @param s
	 */
	public static void start(String s)
	{
		UpdateTimer timer = bench.get(s);
		timer.timing = true;
		timer.start = System.nanoTime();
	}

	/**
	 * Stops the timer for the task of name "s"
	 * @param s
	 */
	public static void stop(String s)
	{
		long after = System.nanoTime();
		UpdateTimer timer = bench.get(s);
		if (timer.timing)
		{
			timer.timing = false;
			addTime(s, after - timer.start);
		}
	}

	/**
	 * An information holder class that bundles multiple related values together of different types
	 * @author aidan
	 *
	 */
	private static class UpdateTimer
	{
		int updates = 0;
		long totalTime = 0;
		long start = 0;
		boolean timing = false;
	}
}
