/**
 * SolidAttachment element template for TABuddy-Desktop.
 *
 * Copyright (c) 2013 Alexey Aksenov ezh@ezh.msk.ru
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Global License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED
 * BY Limited Liability Company «MEZHGALAKTICHESKIJ TORGOVYJ ALIANS»,
 * Limited Liability Company «MEZHGALAKTICHESKIJ TORGOVYJ ALIANS» DISCLAIMS
 * THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Global License for more details.
 * You should have received a copy of the GNU Affero General Global License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://www.gnu.org/licenses/agpl.html
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Global License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Global License,
 * you must retain the producer line in every report, form or document
 * that is created or manipulated using TABuddy.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the TABuddy software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers,
 * serving files in a web or/and network application,
 * shipping TABuddy with a closed source product.
 *
 * For more information, please contact Digimead Team at this
 * address: ezh@ezh.msk.ru
 */

package org.digimead.tabuddy.desktop.logic.payload.template.attachment.solid;

import java.lang.reflect.Field;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.digimead.tabuddy.desktop.logic.payload.template.attachment.solid.messages"; //$NON-NLS-1$

	public static String application_text;
	public static String attachmentName_text;
	public static String author_text;
	public static String author_title_text;
	public static String created_text;
	public static String delete_text;
	public static String dialogMessageEmpty_text;
	public static String dialogMessage_text;
	public static String dialogShell_text;
	public static String dialogTitle_text;
	public static String dndZone_text;
	public static String documentTitle_text;
	public static String edit_text;
	public static String exportFileTitle_text;
	public static String export_text;
	public static String format_text;
	public static String general_text;
	public static String importFileDialogTitle_text;
	public static String importFileTitle_text;
	public static String import_text;
	public static String modified_text;
	public static String noPreview_text;
	public static String replaceFileDialogTitle_text;
	public static String replaceFileTitle_text;
	public static String resetName_text;
	public static String size_text;
	public static String summary_text;
	public static String tooLong;
	public static String tooShort;

	// //////////////////////////////////////////////////////////////////////////
	//
	// Class initialization
	//
	// //////////////////////////////////////////////////////////////////////////
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
		final Field[] fieldArray = Messages.class.getDeclaredFields();
		final int len = fieldArray.length;
		for (int i = 0; i < len; i++) {
			final Field field = (Field) fieldArray[i];
			if (field.getType() == java.lang.String.class) {
				if (!field.isAccessible())
					field.setAccessible(true);
				try {
					final String rawValue = (String) field.get(null);
					field.set(
							null,
							new String(rawValue.getBytes("ISO-8859-1"), "UTF-8")
									.replaceAll("\\\\n", "\n").replaceAll(
											"\\\\t", "\t"));
				} catch (Exception e) {
					// skip field modification
				}
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	// //////////////////////////////////////////////////////////////////////////
	private Messages() {
		// do not instantiate
	}
}
