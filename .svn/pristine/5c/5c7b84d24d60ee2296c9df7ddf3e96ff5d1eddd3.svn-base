package com.wcs.common.controller.helper;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

/**
 * 
 * 公用排序类
 * 
 * */
public class LazySorter<T extends IdModel> implements Comparator<T> {

	private String sortField;// 要排序的字段

	private SortOrder sortOrder;

	public LazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public int compare(T t1, T t2) {
		try {
			Object value1 = t1.getClass().getField(this.sortField).get(t1);
			Object value2 = t2.getClass().getField(this.sortField).get(t2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
