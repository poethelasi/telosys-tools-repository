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
package org.telosys.tools.repository.persistence.util;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProcessContext {

	private final NodeList nodeList;
	private final List<?> list;
	private final Node node;
	private final Element element;
	private final Object nodeProcessed;
	
	public ProcessContext(NodeList nodeList) {
		super();
		this.nodeList = nodeList;
		this.list = null;
		this.node = null;
		this.element = null;
		this.nodeProcessed = null;
	}

	public ProcessContext(List<?> list) {
		super();
		this.nodeList = null;
		this.list = list;
		this.node = null;
		this.element = null;
		this.nodeProcessed = null;
	}

	public ProcessContext(Node node) {
		super();
		this.nodeList = null;
		this.list = null;
		this.node = node;
		this.element = null;
		this.nodeProcessed = null;
	}

	public ProcessContext(Element element) {
		super();
		this.nodeList = null;
		this.list = null;
		this.node = null;
		this.element = element;
		this.nodeProcessed = null;
	}

	public ProcessContext(Object nodeProcessed) {
		super();
		this.nodeList = null;
		this.list = null;
		this.node = null;
		this.element = null;
		this.nodeProcessed = nodeProcessed;
	}

	public NodeList getNodeList() {
		return nodeList;
	}

	public List<?> getList() {
		return list;
	}

	public Node getNode() {
		return node;
	}

	public Element getElement() {
		return element;
	}

	public Object getNodeProcessed() {
		return nodeProcessed;
	}

}
