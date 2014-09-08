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

public class CommandManager implements ICommandManager {

	private static ICommandManager manager;
	static {
		// Instanciation parser
		//List<AbstractCommand> cmds = new ArrayList<AbstractCommand>();
		List<ICommandContext> cmds = new ArrayList<ICommandContext>();
		
		cmds.add(new CommandElementColumn());
		
		cmds.add(new CommandElementForeignKey());
		cmds.add(new CommandElementForeignKeyColumn());
		
		cmds.add(new CommandElementGeneratedValue());

		cmds.add(new CommandElementLink());
		
//		cmds.add(new CommandElementJoinFK());
		cmds.add(new CommandElementJoinTable());
		cmds.add(new CommandElementJoinColumns() );
		cmds.add(new CommandElementInverseJoinColumns() );
		cmds.add(new CommandElementJoinColumn() );
		
		cmds.add(new CommandElementSequenceGenerator());
		cmds.add(new CommandElementTable());
		cmds.add(new CommandElementTableGenerator());
		cmds.add(new CommandNode());
		cmds.add(new CommandNodeList());
		manager = new CommandManager(cmds);
	}	
	
	public static ICommandManager getCommandManager() {
		return manager;
	}
	
	//private final List liste;
	private final List<ICommandContext> liste;
	
	
//	private CommandManager(List liste) {
	private CommandManager(List<ICommandContext> liste) {
		super();
		this.liste = liste;
	}

	public ICommandContext searchCommand(final ProcessContext processContext) throws CommandException {
		ICommandContext iCommandContextFound = null;
//		for (Iterator iterator = liste.iterator(); iterator.hasNext();) {
//			ICommandContext iCommandContext = (ICommandContext) iterator.next();
		for ( ICommandContext iCommandContext : liste ) {
			if (iCommandContext.accept(processContext)) {
				iCommandContextFound = iCommandContext;
				break;
			}
		}
		
		if (iCommandContextFound != null) {
			return iCommandContextFound;
		} else {
			throw new CommandException();
		}
	}


//	public List getListe() {
//		return liste;
//	}

}
