package com.wxxr.trading.core.task.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

public class HashRingUtilsTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGenerateHashRingIntArray() {
		Integer[] result = HashRingAlgUtils.generateHashRing(new Integer[]{1,2,3});
		assertNotNull(result);
		System.out.println(Arrays.toString(result));
		assertEquals(18, result.length);
		
		result = HashRingAlgUtils.generateHashRing(new Integer[]{1,2,3,4});
		assertNotNull(result);
		System.out.println(Arrays.toString(result));
		assertEquals(96, result.length);
		
		result = HashRingAlgUtils.generateHashRing(new Integer[]{1,2,3,4,5});
		assertNotNull(result);
		System.out.println(Arrays.toString(result));
		assertEquals(600, result.length);
		
	}
	
	
	public void testFindActiveNode4Hash() {
		Integer[] hashRing = HashRingAlgUtils.generateHashRing(new Integer[]{1,2,3,4,5,6});
		Map<Integer, AtomicInteger> counters = new HashMap<Integer, AtomicInteger>();
		for(int i = 0 ; i < 1000000 ; i++){
			Integer id = HashRingAlgUtils.findAliveNodeId4Hash(hashRing, null, i);
			increaseCounter(counters, id);
		}
		System.out.println("Result1 :"+counters);
		
		counters.clear();
		ArrayList<Integer> downNodes = new ArrayList<Integer>();
		downNodes.add(3);
		for(int i = 0 ; i < 1000000 ; i++){
			Integer id = HashRingAlgUtils.findAliveNodeId4Hash(hashRing, downNodes, i);
			increaseCounter(counters, id);
		}
		System.out.println("Result2 :"+counters);
		
		counters.clear();
		downNodes.add(1);
		for(int i = 0 ; i < 1000000 ; i++){
			Integer id = HashRingAlgUtils.findAliveNodeId4Hash(hashRing, downNodes, i);
			increaseCounter(counters, id);
		}
		System.out.println("Result3 :"+counters);	
		
		counters.clear();
		downNodes.add(5);
		for(int i = 0 ; i < 1000000 ; i++){
			Integer id = HashRingAlgUtils.findAliveNodeId4Hash(hashRing, downNodes, i);
			increaseCounter(counters, id);
		}
		System.out.println("Result4 :"+counters);

	}

	/**
	 * @param counters
	 * @param id
	 */
	protected void increaseCounter(Map<Integer, AtomicInteger> counters,
			Integer id) {
		AtomicInteger cnt = counters.get(id);
		if(cnt == null){
			cnt = new AtomicInteger();
			counters.put(id, cnt);
		}
		cnt.incrementAndGet();
	}

}
