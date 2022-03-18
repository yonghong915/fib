package com.fib.commons.disruptor;

import com.fib.commons.disruptor.base.Executor;
import com.lmax.disruptor.WorkHandler;

/**
 * 处理器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-18 11:52:42
 */
public class MsgHandler implements WorkHandler<MsgEvent> {

	@Override
	public void onEvent(MsgEvent event) throws Exception {
		try {
			Executor executor = event.getExecutor();
			if (null != executor) {
				try {
					executor.onExecute(event.getMsg());
				} finally {
					executor.release();
				}
			}
		} finally {
			event.clearValues();
		}
	}
}