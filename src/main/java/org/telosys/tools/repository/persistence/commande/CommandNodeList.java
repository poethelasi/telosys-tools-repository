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

import java.util.ArrayList;
import java.util.List;

import org.telosys.tools.repository.persistence.util.CommandException;
import org.telosys.tools.repository.persistence.util.ProcessContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CommandNodeList extends AbstractCommand implements ICommandContext {

	public ProcessContext runProcess(final ProcessContext processContext, final ICommandManager iCommandManager) throws CommandException {
		final NodeList nl = processContext.getNodeList();
		
		// Transform NodeList to List
		final List<Object> maListe = new ArrayList<Object>();
		if ((nl != null) && (nl.getLength() > 0)) {
			for (int k = 0; k <= nl.getLength() - 1; k++) {
				final Node noeud = nl.item(k);
				
				final ProcessContext processContextNext = this.chaineNodeProcess(iCommandManager, noeud);

				// Agregate result
				if (processContextNext.getNodeProcessed() != null) {
					maListe.add(processContextNext.getNodeProcessed());
				}
				
			}
		}
		
		// Return result
		final ProcessContext context = new ProcessContext(maListe);
		return context;
	}

	public boolean accept(final ProcessContext processContext) {
		if (processContext.getNodeList() != null) {
			return true;
		} else {
			return false;
		}
	}

}
