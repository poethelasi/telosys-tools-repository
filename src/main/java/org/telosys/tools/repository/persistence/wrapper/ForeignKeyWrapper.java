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

import org.telosys.tools.repository.model.ForeignKey;
import org.telosys.tools.repository.persistence.util.RepositoryConst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ForeignKeyWrapper {

	public ForeignKey getForeignKey(final Element xmlElement) {
		final ForeignKey foreignKey = new ForeignKey();
		foreignKey.setName(xmlElement.getAttribute(RepositoryConst.FK_NAME) );
		return foreignKey;
	}

	public Element getXmlDesc(final ForeignKey foreignKey, final Document doc) {
		final Element xmlElement = doc.createElement(RepositoryConst.FK);
		xmlElement.setAttribute(RepositoryConst.FK_NAME, foreignKey.getName());
		return xmlElement;
	}
}
