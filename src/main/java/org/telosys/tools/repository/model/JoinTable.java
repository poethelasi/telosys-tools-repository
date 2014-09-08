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
 * "JoinTable" model class <br>
 *
 * @author slabbe
 */
public class JoinTable // extends JoinFKCollection 
{

	private String name ;

	private String schema ;

	private String catalog ;
	
	private JoinColumns        joinColums = null ;

	private InverseJoinColumns inverseJoinColums = null ;
	
	
	//--------------------------------------------------------------------------
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//--------------------------------------------------------------------------
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	//--------------------------------------------------------------------------
	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	//--------------------------------------------------------------------------
	public void setJoinColumns( JoinColumns joinColumns )
	{
		joinColums = joinColumns ;
	}
	public JoinColumns getJoinColumns()
	{
		return joinColums ;
	}
	
	//--------------------------------------------------------------------------
	public void setInverseJoinColumns( InverseJoinColumns joinColumns )
	{
		inverseJoinColums = joinColumns ;
	}
	public InverseJoinColumns getInverseJoinColumns()
	{
		return inverseJoinColums ;
	}
	
	//--------------------------------------------------------------------------
	public String getCheckSum() {
		return name;
	}

	/**
	 * Control functionnal equality of link
	 * @param otherLink
	 * @return if param link is equal to current link
	 */
	public boolean equals(final JoinTable joinTable) {
		// considers that the comparison of the name of the join table is sufficient to establish the equity of JoinTable
		if (joinTable != null) {
			if (joinTable.getCheckSum().equals(this.getCheckSum())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
}
