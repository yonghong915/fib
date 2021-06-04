//package com.fib.upp;
//
//import java.net.InetAddress;
//import java.util.ArrayList;
//import java.util.List;
//
//import net.lvsq.jgossip.core.GossipService;
//import net.lvsq.jgossip.core.GossipSettings;
//import net.lvsq.jgossip.model.SeedMember;
//
//public class JgossipTest {
//	public static void main(String[] args) {
//		GossipService gossipService = null;
//		int gossip_port = 60001;
//		String cluster = "gossip_cluster";
//
//		GossipSettings settings = new GossipSettings();
//		settings.setGossipInterval(1000);
//
//		try {
//			String myIpAddress = InetAddress.getLocalHost().getHostAddress();
//			List<SeedMember> seedNodes = new ArrayList<>();
//			SeedMember seed = new SeedMember();
//			seed.setCluster(cluster);
//			seed.setIpAddress(myIpAddress);
//			seed.setPort(60001);
//			seedNodes.add(seed);
//			
//			
//			SeedMember seed1 = new SeedMember();
//			seed1.setCluster(cluster);
//			seed1.setIpAddress(myIpAddress);
//			seed1.setPort(60002);
//			seedNodes.add(seed1);
//
//			gossipService = new GossipService(cluster, myIpAddress, gossip_port, null, seedNodes, settings,
//					(member, state) -> System.out.println("member:" + member + "  state: " + state));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		gossipService.start();
//		
//		
//	}
//}
