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
import org.w3c.dom.NodeList;

public abstract class AbstractCommand {

	public AbstractCommand() {
		super();
	}

	protected ProcessContext chaineNodeProcess(final ICommandManager iCommandManager, Node noeud) throws CommandException {
		// On construit le contexte pour le noeud
		final ProcessContext processContextChild = new ProcessContext(noeud);
		
		// On cherche le traitement capable de le traiter
		final ICommandContext iccNext = iCommandManager.searchCommand(processContextChild);
		
		// On lance l'execution
		final ProcessContext processContextNext = iccNext.runProcess(processContextChild, iCommandManager);
		return processContextNext;
	}

	protected ProcessContext chaineElementProcess(final ICommandManager iCommandManager, final Element element) throws CommandException {
		// On construit le contexte pour le noeud
		final ProcessContext processContextChild = new ProcessContext(element);
		
		// On cherche le traitement capable de le traiter
		final ICommandContext iccNext = iCommandManager.searchCommand(processContextChild);
		
		// On lance l'execution
		final ProcessContext processContextNext = iccNext.runProcess(processContextChild, iCommandManager);
		return processContextNext;
	}

	protected boolean genericAccept(final ProcessContext processContext, String nameElement) {
		if ((processContext.getElement() != null) && (nameElement.equals(processContext.getElement().getNodeName()))) {
			return true;
		} else {
			return false;
		}
	}

	protected ProcessContext genericChildProcess(final ICommandManager iCommandManager, final Element elem) throws CommandException {
		NodeList nodeList = elem.getChildNodes();
		final ProcessContext processContextChild = new ProcessContext(nodeList);
		
		// On cherche le traitement capable de le traiter
		final ICommandContext iccNext = iCommandManager.searchCommand(processContextChild);
		
		// On lance l'execution
		final ProcessContext processContextNext = iccNext.runProcess(processContextChild, iCommandManager);
		return processContextNext;
	}

}