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
import org.telosys.tools.repository.model.SequenceGenerator;
import org.telosys.tools.repository.persistence.util.RepositoryConst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SequenceGeneratorWrapper {
	
	public SequenceGeneratorWrapper() {
		super();
	}

	public SequenceGenerator getSequenceGenerator(final Element elem) {
		final SequenceGenerator sg = new SequenceGenerator();
		sg.setName(elem.getAttribute(RepositoryConst.SEQUENCE_GENERATOR_NAME));
		sg.setSequenceName(elem.getAttribute(RepositoryConst.SEQUENCE_GENERATOR_SEQUENCENAME));
		sg.setAllocationSize(StrUtil.getInt(elem.getAttribute(RepositoryConst.SEQUENCE_GENERATOR_ALLOCATIONSIZE)));
		return sg;
	}

	public Element getXmlDesc(final SequenceGenerator column, final Document doc) {
		final Element sg = doc.createElement(RepositoryConst.SEQUENCE_GENERATOR_ELEMENT);
		sg.setAttribute(RepositoryConst.SEQUENCE_GENERATOR_NAME, column.getName());
		sg.setAttribute(RepositoryConst.SEQUENCE_GENERATOR_SEQUENCENAME, column.getSequenceName());
		sg.setAttribute(RepositoryConst.SEQUENCE_GENERATOR_ALLOCATIONSIZE, Integer.toString(column.getAllocationSize()));
		return sg;
	}

}
