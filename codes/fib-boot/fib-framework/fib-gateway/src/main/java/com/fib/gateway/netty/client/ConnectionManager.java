package com.fib.gateway.netty.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

public class ConnectionManager {
	private final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
	private AtomicInteger round = new AtomicInteger(0);
	private CopyOnWriteArrayList<Channel> list = new CopyOnWriteArrayList<>();
	private Map<SocketAddress, Channel> map = new HashMap<>();

	private ConnectionManager() {
	}

	private static class SingletonHolder {
		static final ConnectionManager instance = new ConnectionManager();
	}

	public static ConnectionManager getInstance() {
		return SingletonHolder.instance;
	}

	public Channel chooseChannel() {
		if (list.isEmpty()) {
			return null;
		} else {
			int size = list.size();
			int index = (round.addAndGet(1)) % size;
			return list.get(index);
		}
	}

	public synchronized void updateConnectedServer(NettyClient client, List<String> nodes) {
		if (nodes == null || nodes.isEmpty()) {
			log.error("没有可用的服务器节点, 全部服务节点已关闭!");
			for (Channel channel : list) {
				SocketAddress address = channel.remoteAddress();
				map.get(address).close();
			}

			list.clear();
			map.clear();
			return;
		}

		final Set<SocketAddress> set = new HashSet<>(nodes.size());
		for (String node : nodes) {
			log.info("连接【{}】", node);
			String[] split = node.split(":");
			String host = split[0];
			int port = Integer.parseInt(split[1]);
			InetSocketAddress address = new InetSocketAddress(host, port);
			set.add(address);
		}

		for (SocketAddress address : set) {
			Channel channel = map.get(address);

			if (channel != null && channel.isOpen()) {
				log.info("当前服务节点已存在,无需重新连接.{}", address);
			} else {
				connectServerNode(client, address);
			}
		}

		Iterator<Channel> iterator = list.iterator();
		while (iterator.hasNext()) {
			Channel channel = iterator.next();

			if (!set.contains(channel.remoteAddress())) {
				log.info("删除失效服务节点 {}", channel.remoteAddress());
				channel.close();
				iterator.remove();
				map.remove(channel.remoteAddress());
			}
		}
	}

	private void connectServerNode(NettyClient client, SocketAddress address) {
//		try {
//			Channel channel = client.doChannel(address);
//			addChannel(channel, address);
//		} catch (Exception e) {
//			log.error("未能成功连接到服务器:{}", address, e);
//		}
	}

	public void addChannel(Channel channel, SocketAddress address) {
		log.info("加入Channel到连接管理器.{}", address);
		map.put(address, channel);
		list.add(channel);
	}

	public void removeChannel(SocketAddress address) {
		log.info("从连接管理器中移除失效Channel.{}", address);
		list.remove(map.remove(address));
	}
}
