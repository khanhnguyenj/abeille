/*
 * Copyright (c) 2002-2004 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jeta.jgoodies.forms.factories;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * An interface that defines the factory methods as used by the
 * {@link com.jeta.jgoodies.forms.builder.PanelBuilder} and its subclasses.
 * <p>
 * 
 * The texts used in methods <code>#createLabel(String)</code> and
 * <code>#createTitle(String)</code> can contain an optional mnemonic marker.
 * The mnemonic and mnemonic index are indicated by a single ampersand (<tt>&amp;</tt>).
 * For example <tt>&quot;&amp;Save&quot</tt>, or
 * <tt>&quot;Save&nbsp;&amp;as&quot</tt>. To use the ampersand itself
 * duplicate it, for example <tt>&quot;Look&amp;&amp;Feel&quot</tt>.
 * 
 * @author Karsten Lentzsch
 * @version $Revision$
 * 
 * @see DefaultComponentFactory
 * @see com.jeta.jgoodies.forms.builder.PanelBuilder
 */

public interface ComponentFactory {

	/**
	 * Creates and returns a label with an optional mnemonic.
	 * <p>
	 * 
	 * <pre>
	 * createLabel(&quot;Name&quot;); // No mnemonic
	 * createLabel(&quot;N&amp;ame&quot;); // Mnemonic is 'a'
	 * createLabel(&quot;Save &amp;as&quot;); // Mnemonic is the second 'a'
	 * createLabel(&quot;Look&amp;&amp;Feel&quot;); // No mnemonic, text is Look&amp;Feel
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the label's text - may contain a mnemonic
	 * @return an label with optional mnemonic
	 */
	public JLabel createLabel(String textWithMnemonic);

	/**
	 * Creates and returns a label that uses the foreground color and font of a
	 * <code>TitledBorder</code>.
	 * <p>
	 * 
	 * <pre>
	 * createTitle(&quot;Name&quot;); // No mnemonic
	 * createTitle(&quot;N&amp;ame&quot;); // Mnemonic is 'a'
	 * createTitle(&quot;Save &amp;as&quot;); // Mnemonic is the second 'a'
	 * createTitle(&quot;Look&amp;&amp;Feel&quot;); // No mnemonic, text is Look&amp;Feel
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the title's text - may contain a mnemonic
	 * @return an emphasized title label
	 */
	public JLabel createTitle(String textWithMnemonic);

	/**
	 * Creates and returns a labeled separator. Useful to separate paragraphs in
	 * a panel, which is often a better choice than a <code>TitledBorder</code>.
	 * 
	 * @param text
	 *            the title's text
	 * @param alignment
	 *            text alignment: left, center, right
	 * @return a title label with separator on the side
	 */
	public JComponent createSeparator(String text, int alignment);

}