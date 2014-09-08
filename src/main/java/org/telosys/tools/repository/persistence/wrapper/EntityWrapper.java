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
package org.telosys.tools.repository.persistence.wrapper;

import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.persistence.util.RepositoryConst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EntityWrapper {
	
	public EntityWrapper() {
		super();
	}

	public Entity getEntity(final Element table) {
		final Entity entity = new Entity();
		entity.setBeanJavaClass(table.getAttribute(RepositoryConst.TABLE_JAVA_BEAN));
//		entity.setConverterJavaClass(table.getAttribute(RepositoryConst.TABLE_JAVA_BEAN_CONV));  // removed in v 2.0.7
//		entity.setDaoJavaClass(table.getAttribute(RepositoryConst.TABLE_JAVA_BEAN_DAO));  // removed in v 2.0.7
//		entity.setListJavaClass(table.getAttribute(RepositoryConst.TABLE_JAVA_BEAN_LIST));  // removed in v 2.0.7
		entity.setName(table.getAttribute(RepositoryConst.TABLE_NAME));
		entity.setCatalog(table.getAttribute(RepositoryConst.TABLE_CATALOG)); // v 1.0 #LGU
		entity.setSchema(table.getAttribute(RepositoryConst.TABLE_SCHEMA)); // v 1.0 #LGU
		
		entity.setDatabaseType(table.getAttribute(RepositoryConst.TABLE_DATABASE_TYPE)); // added in v 2.0.7
		return entity;
	}

	public Element getXmlDesc(final Entity entity, final Document doc) {
		final Element table = doc.createElement(RepositoryConst.TABLE);
		
		table.setAttribute(RepositoryConst.TABLE_JAVA_BEAN, entity.getBeanJavaClass());
//		table.setAttribute(RepositoryConst.TABLE_JAVA_BEAN_CONV, entity.getConverterJavaClass());  // removed in v 2.0.7
//		table.setAttribute(RepositoryConst.TABLE_JAVA_BEAN_DAO, entity.getDaoJavaClass());  // removed in v 2.0.7
//		table.setAttribute(RepositoryConst.TABLE_JAVA_BEAN_LIST, entity.getListJavaClass());  // removed in v 2.0.7
		
		table.setAttribute(RepositoryConst.TABLE_NAME, entity.getName());		
		table.setAttribute(RepositoryConst.TABLE_CATALOG, entity.getCatalog()); // v 1.0 #LGU	
		table.setAttribute(RepositoryConst.TABLE_SCHEMA, entity.getSchema()); // v 1.0 #LGU
		
		table.setAttribute(RepositoryConst.TABLE_DATABASE_TYPE, entity.getDatabaseType()); // added in v 2.0.7
		return table;
	}

}
