/** 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation; either version 2 of the License, or 
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.leo.list;

import org.apache.commons.collections4.list.TreeList;

import java.util.*;

/**
 * CombinedList
 * <pre>
 * Internaly combine implementations of ArrayList, HashSet and TreeList
 * <pre>
 * @author Leo Lewis
 */
public class CombinedList<E> extends AbstractList<E> {

	/** Serial version UID */
	private static final long serialVersionUID = -1801575433602378342L;

	/** HashSet */
	private HashSet<E> hashSet;
	/** ArrayList */
	private ArrayList<E> arrayList;
	/** TreeList */
	private TreeList<E> treeList;

	/** If hashSet is up-to-date */
	private boolean isHashSetUpToDate = true;
	/** If arrayList is up-to-date */
	private boolean isArrayListUpToDate = true;
	/** If treeList is up-to-date */
	private boolean isTreeListUpToDate = true;

	/**
	 * Constructor
	 */
	public CombinedList() {
		super();
		init();
	}

	/**
	 * Constructor
	 * 
	 * @param c
	 */
	public CombinedList(Collection<? extends E> c) {
		this();
		addAll(c);
	}

	/**
	 * Initialize custom structures
	 */
	private void init() {
		hashSet = new HashSet<E>();
		arrayList = new ArrayList<E>();
		treeList = new TreeList<E>();
	}

	/**
	 * Update the arrayList if out of date
	 */
	private void updateArrayListIfOutOfDate() {
		// TreeList is necessarily up-to-date
		if (!isArrayListUpToDate) {
			arrayList.clear();
			arrayList.addAll(treeList);
			isArrayListUpToDate = true;
		}
	}

	/**
	 * Update the treeList if out of date
	 */
	private void updateTreeListIfOutOfDate() {
		// arrayList is necessarily up-to-date
		if (!isTreeListUpToDate) {
			treeList.clear();
			treeList.addAll(arrayList);
			isTreeListUpToDate = true;
		}
	}

	/**
	 * Update the hashSet if out of date
	 */
	private void updateHashSetIfOutOfDate() {
		// one of the two collection arrayList or TreeList is necessarily
		// up-to-date
		if (!isHashSetUpToDate) {
			hashSet.clear();
			if (isArrayListUpToDate) {
				hashSet.addAll(arrayList);
			} else {
				hashSet.addAll(treeList);
			}
			isHashSetUpToDate = true;
		}
	}

	/**
	 * Set collection other than ArrayList out-of-date
	 */
	private void setOtherThanArrayListOutOfDate() {
		isHashSetUpToDate = false;
		isTreeListUpToDate = false;
	}

	/**
	 * Set collection other than TreeList out-of-date
	 */
	private void setOtherThanTreeListOutOfDate() {
		isHashSetUpToDate = false;
		isArrayListUpToDate = false;
	}

	/**
	 * @see Collection#contains(Object)
	 */
	@Override
	public boolean contains(Object o) {
		// most efficient is HashSet
		updateHashSetIfOutOfDate();
		return hashSet.contains(o);
	}

	/**
	 * @see Collection#containsAll(Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		// most efficient is HashSet
		updateHashSetIfOutOfDate();
		return hashSet.containsAll(c);
	}

	/**
	 * @see List#indexOf(Object)
	 */
	@Override
	public int indexOf(Object o) {
		// most efficient is ArrayList
		updateArrayListIfOutOfDate();
		return arrayList.indexOf(o);
	}

	/**
	 * @see List#lastIndexOf(Object)
	 */
	@Override
	public int lastIndexOf(Object o) {
		// most efficient is ArrayList
		updateArrayListIfOutOfDate();
		return arrayList.lastIndexOf(o);
	}

	/**
	 * @see List#get(int)
	 */
	@Override
	public E get(int index) {
		// most efficient is ArrayList
		updateArrayListIfOutOfDate();
		return arrayList.get(index);
	}

	/**
	 * @see List#set(int, Object)
	 */
	@Override
	public E set(int index, E element) {
		modCount++;
		// most efficient is ArrayList
		updateArrayListIfOutOfDate();
		E old = arrayList.set(index, element);
		setOtherThanArrayListOutOfDate();
		return old;
	}

	/**
	 * @see Collection#add(Object)
	 */
	@Override
	public boolean add(E element) {
		modCount++;
		// most efficient is ArrayList
		updateArrayListIfOutOfDate();
		boolean added = arrayList.add(element);
		if (added) {
			setOtherThanArrayListOutOfDate();
		}
		return added;
	}

	/**
	 * @see List#add(int, Object)
	 */
	@Override
	public void add(int index, E element) {
		modCount++;
		// most efficient is TreeList
		updateTreeListIfOutOfDate();
		treeList.add(index, element);
		setOtherThanTreeListOutOfDate();
	}

	/**
	 * @see Collection#addAll(Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> col) {
		modCount++;
		// most efficient is ArrayList
		updateArrayListIfOutOfDate();
		boolean addedAll = arrayList.addAll(col);
		if (addedAll) {
			setOtherThanArrayListOutOfDate();
		}
		return addedAll;
	}

	/**
	 * @see List#addAll(int, Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> col) {
		modCount++;
		// most efficient is TreeList
		updateTreeListIfOutOfDate();
		boolean addedAll = treeList.addAll(index, col);
		if (addedAll) {
			setOtherThanTreeListOutOfDate();
		}
		return addedAll;
	}

	/**
	 * @see List#remove(int)
	 */
	@Override
	public E remove(int index) {
		modCount++;
		// most efficient is TreeList
		updateTreeListIfOutOfDate();
		E removed = (E) treeList.remove(index);
		if (removed != null) {
			setOtherThanTreeListOutOfDate();
		}
		return removed;
	}

	/**
	 * @see Collection#remove(Object)
	 */
	@Override
	public boolean remove(Object o) {
		modCount++;
		// most efficient is TreeList
		updateTreeListIfOutOfDate();
		boolean removed = treeList.remove(o);
		if (removed) {
			setOtherThanTreeListOutOfDate();
		}
		return removed;
	}

	/**
	 * @see Collection#removeAll(Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> col) {
		modCount++;
		// most efficient is TreeList
		updateTreeListIfOutOfDate();
		boolean removedAll = treeList.removeAll(col);
		if (removedAll) {
			setOtherThanTreeListOutOfDate();
		}
		return removedAll;
	}

	/**
	 * @see Collection#retainAll(Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> col) {
		modCount++;
		// most efficient is TreeList
		updateTreeListIfOutOfDate();
		// performances issues on the retainAll method
		boolean retainedAll = treeList.removeAll(new HashSet<>(col));
		if (retainedAll) {
			setOtherThanTreeListOutOfDate();
		}
		return retainedAll;
	}

	/**
	 * @see List#subList(int, int)
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		// most efficient is TreeList
		updateTreeListIfOutOfDate();
		return treeList.subList(fromIndex, toIndex);
	}

	/**
	 * @see Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		// most efficient is ArrayList
		updateArrayListIfOutOfDate();
		return arrayList.toArray();
	}

	/**
	 * @see Collection#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		// most efficient is ArrayList
		updateArrayListIfOutOfDate();
		return arrayList.toArray(a);
	}

	/**
	 * @see Collection#size()
	 */
	@Override
	public int size() {
		if (isArrayListUpToDate) {
			return arrayList.size();
		} else {
			return treeList.size();
		}
	}

	/**
	 * @see Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		if (isArrayListUpToDate) {
			return arrayList.isEmpty();
		} else {
			return treeList.isEmpty();
		}
	}

	/**
	 * @see Collection#clear()
	 */
	@Override
	public void clear() {
		modCount++;
		hashSet.clear();
		arrayList.clear();
		treeList.clear();
		isHashSetUpToDate = true;
		isArrayListUpToDate = true;
		isTreeListUpToDate = true;
	}

	/**
	 * @see Object#clone()
	 */
	@Override
	public Object clone() {
		return new CombinedList<E>(this);
	}
	
	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		if (isArrayListUpToDate) {
			return arrayList.toString();
		} else {
			return treeList.toString();
		}
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (isArrayListUpToDate) {
			return arrayList.equals(obj);
		} else {
			return treeList.equals(obj);
		}
	}
}
