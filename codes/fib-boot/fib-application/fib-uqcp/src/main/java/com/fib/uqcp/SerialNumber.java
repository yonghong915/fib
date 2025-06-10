package com.fib.uqcp;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;

public class SerialNumber {
	private long workerId = 0;
	private long datacenterId = 1;
	private Snowflake snowFlake = IdUtil.getSnowflake(workerId, datacenterId);

	public void init() {
		try {
			// 将网络ip转换成long
			workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取雪花ID
	 * 
	 * @return
	 */
	public synchronized long snowflakeId() {
		return this.snowFlake.nextId();
	}

	public synchronized long snowflakeId(long workerId, long datacenterId) {
		Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
		return snowflake.nextId();
	}

	public static void main(String[] args) {
		SerialNumber snowFlakeDemo = new SerialNumber();
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				System.out.println(snowFlakeDemo.snowflakeId());
			}, String.valueOf(i)).start();
		}
	}
}
