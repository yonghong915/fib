package com.fib.msgconverter.lottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Lottery {

	public long genRandomNum(int minNum, int maxNum) {
		int range = maxNum = minNum;
		double rand = Math.random();
		return (minNum + Math.round(rand * range));
	}

	public static void main(String[] args) {
		List<Prize> Prizes = new ArrayList<Prize>();

		// 序号==物品Id==物品名称==概率

		Prizes.add(new Prize(1, "P1", "物品1", 0.2d));

		Prizes.add(new Prize(2, "P2", "物品2", 0.2d));

		Prizes.add(new Prize(3, "P3", "物品3", 0.4d));

		Prizes.add(new Prize(4, "P4", "物品4", 0.3d));

		Prizes.add(new Prize(5, "P5", "物品5", 0.13d));

		Prizes.add(new Prize(6, "P6", "物品6", 0.1d));

		Prizes.add(new Prize(7, "P7", "物品7", 0.008d));

		List<Double> orignalRates = new ArrayList<Double>(Prizes.size());

		for (Prize Prize : Prizes) {

			double probability = Prize.getProbability();

			if (probability < 0) {

				probability = 0;

			}

			orignalRates.add(probability);

		}

		// statistics

		Map<Integer, Integer> count = new HashMap<Integer, Integer>();

		double num = 1000000;

		for (int i = 0; i < num; i++) {

			int orignalIndex = LotteryUtil.lottery(orignalRates);

			Integer value = count.get(orignalIndex);

			count.put(orignalIndex, value == null ? 1 : value + 1);

		}

		for (Entry<Integer, Integer> entry : count.entrySet()) {

			System.out.println(Prizes.get(entry.getKey()) + ", count=" + entry.getValue() + ", probability="
					+ entry.getValue() / num);

		}
	}
}
