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

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.repository.model.Link;
import org.telosys.tools.repository.persistence.util.RepositoryConst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LinkWrapper {

	public LinkWrapper() {
		super();
	}

	public Link getLink(Element element) 
	{
		Link link = new Link();
		
		link.setId(element.getAttribute(RepositoryConst.LINK_ID));
		
		link.setCascade(element.getAttribute(RepositoryConst.LINK_CASCADE));
		link.setFetch(element.getAttribute(RepositoryConst.LINK_FETCH));
		link.setInverseSideOf(element.getAttribute(RepositoryConst.LINK_INVERSE_SIDE_OF));
		link.setJavaFieldName(element.getAttribute(RepositoryConst.LINK_JAVA_NAME));
		link.setJavaFieldType(element.getAttribute(RepositoryConst.LINK_JAVA_TYPE));
		link.setMappedBy(element.getAttribute(RepositoryConst.LINK_MAPPED_BY));
		link.setTargetEntityJavaType(element.getAttribute(RepositoryConst.LINK_TARGET_ENTITY));
		
		link.setSourceTableName(element.getAttribute(RepositoryConst.LINK_SOURCE_TABLE_NAME));
		link.setTargetTableName(element.getAttribute(RepositoryConst.LINK_TARGET_TABLE_NAME));
		link.setCardinality(element.getAttribute(RepositoryConst.LINK_CARDINALITY));

		if (StrUtil.nullOrVoid(element.getAttribute(RepositoryConst.LINK_FOREIGN_KEY_NAME)) == false) {
			link.setForeignKeyName( element.getAttribute(RepositoryConst.LINK_FOREIGN_KEY_NAME) );
		}
		if (StrUtil.nullOrVoid(element.getAttribute(RepositoryConst.LINK_JOIN_TABLE_NAME)) == false) {
			link.setJoinTableName( element.getAttribute(RepositoryConst.LINK_JOIN_TABLE_NAME) );
		}
		if (StrUtil.nullOrVoid(element.getAttribute(RepositoryConst.LINK_OWNING_SIDE)) == false) {
			link.setOwningSide(StrUtil.getBoolean(element.getAttribute(RepositoryConst.LINK_OWNING_SIDE)));
		}
//		if (StrUtil.nullOrVoid(element.getAttribute(RepositoryConst.LINK_OPTIONAL)) == false) {
//			link.setOptional(StrUtil.getBoolean(element.getAttribute(RepositoryConst.LINK_OPTIONAL)));
//		}
		link.setOptional(element.getAttribute(RepositoryConst.LINK_OPTIONAL));

		if (StrUtil.nullOrVoid(element.getAttribute(RepositoryConst.LINK_USED)) == false) {
			link.setUsed(StrUtil.getBoolean(element.getAttribute(RepositoryConst.LINK_USED)));
		}
		return link;
	}

	public Element getXmlDesc(Link link, Document doc) 
	{
		Element element = doc.createElement(RepositoryConst.LINK);
		
		element.setAttribute(RepositoryConst.LINK_ID, link.getId());
		
		element.setAttribute(RepositoryConst.LINK_CASCADE, link.getCascade());
		element.setAttribute(RepositoryConst.LINK_FETCH, link.getFetch());
		element.setAttribute(RepositoryConst.LINK_INVERSE_SIDE_OF, link.getInverseSideOf());
		element.setAttribute(RepositoryConst.LINK_JAVA_NAME, link.getJavaFieldName());
		element.setAttribute(RepositoryConst.LINK_JAVA_TYPE, link.getJavaFieldType());
		element.setAttribute(RepositoryConst.LINK_MAPPED_BY, link.getMappedBy());
		element.setAttribute(RepositoryConst.LINK_TARGET_ENTITY, link.getTargetEntityJavaType());
		element.setAttribute(RepositoryConst.LINK_TARGET_TABLE_NAME, link.getTargetTableName());
		
		element.setAttribute(RepositoryConst.LINK_FOREIGN_KEY_NAME, link.getForeignKeyName());
		element.setAttribute(RepositoryConst.LINK_JOIN_TABLE_NAME, link.getJoinTableName());
		
		element.setAttribute(RepositoryConst.LINK_SOURCE_TABLE_NAME, link.getSourceTableName());
		element.setAttribute(RepositoryConst.LINK_CARDINALITY, link.getCardinality());

		element.setAttribute(RepositoryConst.LINK_OWNING_SIDE, Boolean.toString(link.isOwningSide()));
		
		//element.setAttribute(RepositoryConst.LINK_OPTIONAL, Boolean.toString(link.isOptional()));
		element.setAttribute(RepositoryConst.LINK_OPTIONAL, link.getOptional() );

		element.setAttribute(RepositoryConst.LINK_USED, Boolean.toString(link.isUsed()));
		
		return element;
	}

}
