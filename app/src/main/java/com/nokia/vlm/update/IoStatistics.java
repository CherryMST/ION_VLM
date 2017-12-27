package com.nokia.vlm.update;

import java.util.LinkedList;

public class IoStatistics {
	
	// 用来统计速度的块数。0表示取全部平均速度
	private int EXEC_SPEED_COUNT = 300;
	
	private LinkedList<Item> items = new LinkedList<Item>();
	
	public IoStatistics(){
	}
	
	public void add(Item item){
		int size = items.size();
		if(size > 0){
			Item last = items.getLast();
			item.totalLength = last.totalLength + item.length;
		}else{
			item.totalLength = item.length;
		}
		items.add(item);
	}
	
	public double getSpeed() {
		int size = items.size();
		if (size == 0) {
			return 0;
		}
		Item firstItem = null;
		if(EXEC_SPEED_COUNT > 0 && size >= EXEC_SPEED_COUNT){
			firstItem = items.get(size - EXEC_SPEED_COUNT);
		}else{
			firstItem = items.get(0);
		}
		Item lastItem = items.get(size - 1);
		if(lastItem != null){
			long length = lastItem.totalLength - firstItem.totalLength;
			long time = lastItem.timestamp - firstItem.timestamp;
			if (length > 0 && time > 0) {
				return (length * 1000) / (double) (time * 1024);
			}
		}
		return 0;
	}
	
	static class Item {

		long length;

		long timestamp;
		
		long totalLength;

		public Item(long length, long timestamp) {
			this.length = length;
			this.timestamp = timestamp;
		}

	}

}
