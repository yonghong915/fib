package com.fib.netty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class JmhTest {
	private List<String> arrayList;

	@Setup(Level.Iteration)
	public void setUp() {
		arrayList = new ArrayList<>();
	}

	@Benchmark
	public List<String> arrayListAdd() {
		this.arrayList.add("aaaa");
		return arrayList;
	}

	public static void main(String[] args) {
		final Options opts = new OptionsBuilder().include(JmhTest.class.getSimpleName()).forks(1).measurementIterations(10).warmupIterations(10)
				.build();
		try {
			new Runner(opts).run();
		} catch (RunnerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
