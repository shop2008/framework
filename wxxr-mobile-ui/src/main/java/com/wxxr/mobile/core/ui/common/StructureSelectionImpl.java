/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wxxr.mobile.core.ui.api.IStructureSelection;

/**
 * @author neillin
 *
 */
public class StructureSelectionImpl implements IStructureSelection {

	private LinkedList<Object> selected;
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.selected == null || this.selected.isEmpty();
	}


	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Object> iterator() {
		return this.selected != null ? this.selected.iterator() : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IStructureSelection#size()
	 */
	@Override
	public int size() {
		return this.selected != null ? this.selected.size() : 0;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IStructureSelection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return this.selected != null ? this.selected.toArray() : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IStructureSelection#toList()
	 */
	@Override
	public List<Object> toList() {
		return this.selected != null ? Collections.unmodifiableList(this.selected) : null;
	}

	public StructureSelectionImpl addSelection(Object val){
		if(this.selected == null){
			this.selected = new LinkedList<Object>();
		}
		if(!this.selected.contains(val)){
			this.selected.add(val);
		}
		return this;
	}
	
	public StructureSelectionImpl removeSelection(Object val){
		if(this.selected != null){
			this.selected.remove(val);
		}
		return this;
	}
	
	public StructureSelectionImpl clear() {
		if(this.selected != null){
			this.selected.clear();
		}
		return this;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((selected == null) ? 0 : selected.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StructureSelectionImpl other = (StructureSelectionImpl) obj;
		if (selected == null) {
			if (other.selected != null)
				return false;
		} else if (!selected.equals(other.selected))
			return false;
		return true;
	}
}
