package com.fib.msgconverter.commgateway.module.impl;

import com.cmbc.corebank.exception.BusinessContext;
import com.cmbc.corebank.exception.socket.SocketCall;
import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.module.Module;

public class ExceptionMonitor extends Module {
	public static final String DEFAULT_TRANSACTION_ID = "commgateway";

	@Override
	public void initialize() {
		String rootPath = (String) getParameters().get("root-path");
		if (null != rootPath) {
			BusinessContext.setDumpPath(rootPath);
		}
		CommGateway.setExceptionMonitorSupport(true);
	}

	@Override
	public void shutdown() {
		BusinessContext.clearContext();

	}

	public static void dump(String transcationId, Exception e) {
		BusinessContext context = null;
		try {
			context = BusinessContext.currentContext(transcationId);
			context.push(e);
			context.dump();
		} finally {
			if (null != context) {
				context.clear();
			}
		}
	}

	public static void dump(String transcationId, String ip, int port,
			String request, String response, Exception e) {
		BusinessContext context = null;
		try {
			context = BusinessContext.currentContext(transcationId);
			SocketCall call = context.push(ip, port);
			if (null != request) {
				call.setRequestChars(request);
			}
			if (null != response) {
				call.setResponseChars(response);
			}

			context.push(e);
			context.dump();
		} finally {
			if (null != context) {
				context.clear();
			}
		}
	}

	public static void dump(String transcationId, String ip, int port,
			byte[] request, byte[] response, Exception e) {
		BusinessContext context = null;
		try {
			context = BusinessContext.currentContext(transcationId);
			SocketCall call = context.push(ip, port);
			if (null != request) {
				call.setRequestBytes(request);
			}
			if (null != response) {
				call.setResponseBytes(response);
			}
			context.push(e);
			context.dump();
		} finally {
			if (null != context) {
				context.clear();
			}
		}
	}

}
