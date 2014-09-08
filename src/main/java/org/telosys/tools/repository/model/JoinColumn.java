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

/**
 * "joinColumn" model class <br>
 * <joinColumn name="EMPLOYEE_ID" table="" referencedColumnName="ID" unique="true|false" nullable="true|false" updatable="" insertable="" />
 * 
 * @author slabbe
 * 
 */
public class JoinColumn {

	private String name;

	private String referencedColumnName;

	private boolean unique = false;

	private boolean nullable = true;

	private boolean updatable  = false; 

	private boolean insertable = false; 

	public JoinColumn() {
		
	}
	
	public String getCheckSum() {
		return name + "#" + referencedColumnName;
	}

	/**
	 * Control functionnal equality of JoinFK
	 * @param otherJoinFK
	 * @return if param link is equal to current link
	 */
	public boolean equals(final JoinColumn otherJoinColumn) {
		if (this.getCheckSum().equals(otherJoinColumn.getCheckSum())) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReferencedColumnName() {
		return referencedColumnName;
	}

	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

}
