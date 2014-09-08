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
package org.telosys.tools.repository ;

/**
 * @author Laurent GUERIN
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpdateLogWriter
{
    private final static int                LF              = 1;

    private final static int                CRLF            = 2;

    //--- Type de fin de ligne
    private int                             _iEndOfLine     = LF;
	
	private FileOutputStream _fos = null ;
	
    //-----------------------------------------------------------------------------
    //--- CONSTRUCTORS
    //-----------------------------------------------------------------------------
    public UpdateLogWriter(File file)
    {
        if (file != null)
        {
	    	init(file);
        }
        else
        {
        	throw new RuntimeException("LogWriter constructor : file parameter is null");
        	//MsgBox.error("LogWriter constructor : file parameter is null");
        }
    }
    
    private void init(File file)
    {
        try
        {
            _fos = new FileOutputStream(file);
        } catch (FileNotFoundException ex) // Cannot create file
        {
        	throw new RuntimeException("LogWriter : Cannot create file '" + file.getAbsolutePath() + "'");
        	//MsgBox.error("LogWriter : Cannot create file '" + file.getAbsolutePath() + "'");
        }
    }
    
    //-----------------------------------------------------------------------------
    synchronized public void println( String msg )
    {
    	System.out.println("LogWritter.println (" + msg + ")");
        if (_fos != null)
        {
            try
            {
                _fos.write(msg.getBytes());
                if (_iEndOfLine == CRLF)
                {
                	_fos.write('\r');
                	_fos.write('\n');
                }
                else
                {
                	_fos.write('\n');
                }
                _fos.flush();
            } catch (IOException ex)
            {
            	throw new RuntimeException("LogWriter : cannot write (IOException)");
	        	//MsgBox.error("LogWriter : cannot write (IOException)");
            }
        }
    }
    //-----------------------------------------------------------------------------
    synchronized public void close()
    {
        if (_fos != null)
        {
        	try {
				_fos.close();
			} catch (IOException e) {
				throw new RuntimeException("LogWriter : cannot close (IOException)");
	        	//MsgBox.error("LogWriter : cannot close (IOException)");
			}
        }
    }
}