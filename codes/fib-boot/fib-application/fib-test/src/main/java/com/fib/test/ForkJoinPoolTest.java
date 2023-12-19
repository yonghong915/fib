package com.fib.test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolTest {

	public static void main(String[] args) {
		try (ForkJoinPool forkJoinPool = new ForkJoinPool(2);) {
			MyTask myTask = new ForkJoinPoolTest().new MyTask(1, 40);
			int result = forkJoinPool.invoke(myTask);
			System.out.println(result);
			forkJoinPool.shutdown();
		}

	}

	class MyTask extends RecursiveTask<Integer> {
		private int limit = 4;

		private int firstIndex;

		private int lastIndex;

		public MyTask(int firstIndex, int lastIndex) {
			this.firstIndex = firstIndex;
			this.lastIndex = lastIndex;
		}

		@Override
		protected Integer compute() {
			int result = 0;

			int gap = lastIndex - firstIndex;

			boolean flag = gap <= limit;

			if (flag) {
				System.out.println(Thread.currentThread().getName());

				for (int i = firstIndex; i <= lastIndex; i++) {
					result += i;
				}
			} else {
				int middleIndex = (firstIndex + lastIndex) / 2;

				MyTask leftTask = new MyTask(firstIndex, middleIndex);
				MyTask rightTask = new MyTask(middleIndex + 1, lastIndex);

				invokeAll(leftTask, rightTask);

				int leftTaskResult = leftTask.join();
				int rightTaskResult = rightTask.join();

				result = leftTaskResult + rightTaskResult;
			}

			return result;
		}
	}
}
