package com.wxxr.trading.core.task.common;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Neil Lin
 *
 */
public abstract class HashRingAlgUtils {

	public static Integer[] generateHashRing(Integer[] nodes){
		LinkedList<Integer[]> result = new LinkedList<Integer[]>();
		generateHashRing(result, null, nodes);
		Integer[] ring = new Integer[result.size()*nodes.length];
		int idx = 0;
		for (Integer[] vals : result) {
			for (int id : vals) {
				ring[idx++] = id;
			}
		}
		return ring;
	}
	
	private static void generateHashRing(LinkedList<Integer[]> result, LinkedList<Integer> prefix, Integer[] nodes){
		if((prefix != null)&&(prefix.size() == nodes.length)){
			result.add(prefix.toArray(new Integer[0]));
			return;
		}
		if(prefix == null){
			prefix = new LinkedList<Integer>();
		}
		for (int id : nodes) {
			if(!prefix.contains(id)){
				LinkedList<Integer> list =  new LinkedList<Integer>(prefix);
				list.addLast(id);
				generateHashRing(result, list, nodes);
			}
		}
		
	}
	
	public static Integer findAliveNodeId4Hash(Integer[] hashRing, List<Integer> downNodes,long hash) {
		int mod = hashRing.length;
		int idx = (int)((hash % mod)&0x00ffffffffL);
		Integer node = hashRing[idx];
		while(downNodes != null && downNodes.contains(node)){
			idx = (idx+1) % mod;
			node = hashRing[idx];
		}
		return node;
	}


}
