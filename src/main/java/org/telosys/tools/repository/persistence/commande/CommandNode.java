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
package org.telosys.tools.repository.persistence.commande;

import org.telosys.tools.repository.persistence.util.CommandException;
import org.telosys.tools.repository.persistence.util.ProcessContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CommandNode extends AbstractCommand implements ICommandContext {

	public ProcessContext runProcess(final ProcessContext processContext, final ICommandManager iCommandManager) throws CommandException {
		final Node noeud = processContext.getNode();

		if ((noeud != null) && (noeud.getNodeType() == Node.ELEMENT_NODE)) {
			// Transform Node to Element 
			final Element element = (Element) noeud;
			
			// Result analysis
			final ProcessContext processContextNext = chaineElementProcess(iCommandManager, element);

			// Return result
			final ProcessContext context = new ProcessContext(processContextNext.getNodeProcessed());
			return context;
		} else {
			final ProcessContext context = new ProcessContext((Object) null);
			return context;
		}
	}

	public boolean accept(final ProcessContext processContext) {
		if (processContext.getNode() != null) {
			return true;
		} else {
			return false;
		}
	}

}
