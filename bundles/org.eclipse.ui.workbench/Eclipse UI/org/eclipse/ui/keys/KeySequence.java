/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.keys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.eclipse.ui.internal.util.Util;

/**
 * <p>
 * JAVADOC
 * </p>
 * <p>
 * <em>EXPERIMENTAL</em>
 * </p>
 * 
 * @since 3.0
 */
public final class KeySequence implements Comparable {

	public final static char KEY_STROKE_DELIMITER = ' '; //$NON-NLS-1$
	public final static String KEY_STROKE_DELIMITERS = KEY_STROKE_DELIMITER + "\b\t\n\f\r\u001b\u007F"; //$NON-NLS-1$

	private final static int HASH_FACTOR = 89;
	private final static int HASH_INITIAL = KeySequence.class.getName().hashCode();
	private final static String KEY_STROKE_DELIMITER_KEY = "KEY_STROKE_DELIMITER"; //$NON-NLS-1$
	private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(KeySequence.class.getName());
	
	/**
	 * JAVADOC
	 * 
	 * @return
	 */		
	public static KeySequence getInstance() {
		return new KeySequence(Collections.EMPTY_LIST);
	}

	/**
	 * JAVADOC
	 * 
	 * @param keyStroke
	 * @return
	 */		
	public static KeySequence getInstance(KeyStroke keyStroke) {
		return new KeySequence(Collections.singletonList(keyStroke));
	}

	/**
	 * JAVADOC
	 * 
	 * @param keyStrokes
	 * @return
	 */		
	public static KeySequence getInstance(KeyStroke[] keyStrokes) {
		return new KeySequence(Arrays.asList(keyStrokes));
	}

	/**
	 * JAVADOC
	 * 
	 * @param keyStrokes
	 * @return
	 */		
	public static KeySequence getInstance(List keyStrokes) {
		return new KeySequence(keyStrokes);
	}

	/**
	 * JAVADOC
	 * 
	 * @param string
	 * @return
	 * @throws ParseException
	 */
	public static KeySequence getInstance(String string)
		throws ParseException {
		if (string == null)
			throw new NullPointerException();

		List keyStrokes = new ArrayList();
		StringTokenizer stringTokenizer = new StringTokenizer(string, KEY_STROKE_DELIMITERS);
				
		while (stringTokenizer.hasMoreTokens())
			keyStrokes.add(KeyStroke.getInstance(stringTokenizer.nextToken()));
			
		return new KeySequence(keyStrokes);
	}

	private List keyStrokes;
	
	private KeySequence(List keyStrokes) {
		this.keyStrokes = Util.safeCopy(keyStrokes, KeyStroke.class);
	}

	public int compareTo(Object object) {
		KeySequence keySequence = (KeySequence) object;
		int compareTo = Util.compare(keyStrokes, keySequence.keyStrokes);
		return compareTo;
	}

	public boolean equals(Object object) {
		if (!(object instanceof KeySequence))
			return false;

		KeySequence keySequence = (KeySequence) object;
		return keyStrokes.equals(keySequence.keyStrokes);
	}

	/**
	 * JAVADOC
	 * 
	 * @return
	 */
	public String format() {
		return format(true);
	}

	/**
	 * JAVADOC
	 * 
	 * @return
	 */
	public List getKeyStrokes() {
		return keyStrokes;
	}

	public int hashCode() {
		int result = HASH_INITIAL;
		result = result * HASH_FACTOR + keyStrokes.hashCode();
		return result;
	}

	/**
	 * JAVADOC
	 * 
	 * @param keySequence
	 * @param equals
	 * @return
	 */
	public boolean isChildOf(KeySequence keySequence, boolean equals) {
		if (keySequence == null)
			throw new NullPointerException();
		
		return Util.isChildOf(keyStrokes, keySequence.keyStrokes, equals);
	}

	public String toString() {
		return format(false);
	}

	private String format(boolean localize) {
		int i = 0;
		Iterator iterator = keyStrokes.iterator();
		StringBuffer stringBuffer = new StringBuffer();
			
		while (iterator.hasNext()) {
			if (i != 0) {			
				if (localize)
					stringBuffer.append(Util.getString(RESOURCE_BUNDLE, KEY_STROKE_DELIMITER_KEY));
				else
					stringBuffer.append(KEY_STROKE_DELIMITER);
			}
	
			stringBuffer.append(((KeyStroke) iterator.next()).format());
			i++;
		}
	
		return stringBuffer.toString();
	}
}
