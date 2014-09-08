/**
 *  Copyright (C) 2008-2013  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.repository.model;

import java.util.Iterator;
import java.util.LinkedList;

public class InverseJoinColumns implements Iterable<JoinColumn>
{
	LinkedList<JoinColumn> _joinColumns = new LinkedList<JoinColumn>() ;
	
	public InverseJoinColumns() {
		super();
	}

	public InverseJoinColumns( LinkedList<JoinColumn> joinColumns ) {
		super();
		for ( JoinColumn jc : joinColumns ) {
			_joinColumns.add(jc);
		}
	}

	public boolean add(JoinColumn arg0) {
		return _joinColumns.add(arg0);
	}

	public void clear() {
		_joinColumns.clear();
	}

	public boolean isEmpty() {
		return _joinColumns.isEmpty();
	}

	public Iterator<JoinColumn> iterator() {
		return _joinColumns.iterator();
	}

	public JoinColumn get(int i) {
		return _joinColumns.get(i);
	}

	public JoinColumn getFirst() {
		return _joinColumns.getFirst();
	}

	private final static JoinColumn[] VOID_ARRAY = new JoinColumn[0];
	
	public JoinColumn[] getAll() {
		return _joinColumns.toArray( VOID_ARRAY );
	}

	public int size() {
		return _joinColumns.size();
	}
	
}
