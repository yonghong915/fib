package com.fib.ctrler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.SampleTime) // 测试完成时间
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS) // 预热 1 轮，每次 1s
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS) // 测试 5 轮，每次 3s
@Fork(1) // fork 1 个线程
@State(Scope.Thread)
@Threads(10) // 开启 10 个并发线程
public class TestMoney {
    int len = 10000;
	@Benchmark
	public void testBigDecimal() {
		BigDecimal total = BigDecimal.ZERO;
		for (int i = 0; i < len; i++) {
			BigDecimal a = new BigDecimal("1.1");
			total = total.add(a);
		}
		total = total.setScale(2, RoundingMode.HALF_UP);
		//System.out.println("testBigDecimal=" + total);
	}

	@Benchmark
	public void testLong() {
		long total = 0;
		for (int i = 0; i < len; i++) {
			long a = 110l;
			total += a;
		}
		DecimalFormat decimalFormat = new DecimalFormat("0.00"); // 创建DecimalFormat对象，并指定保留2位小数的格式
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
		String formattedResult = decimalFormat.format(total / 100);
		//System.out.println("testLong=" + formattedResult);
	}

	public static void main(String[] args) throws RunnerException {
		final Options options = new OptionsBuilder().include(TestMoney.class.getSimpleName()).build();
		new Runner(options).run();
	}
}