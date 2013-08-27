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

package org.digimead.tabuddy.desktop.logic.payload.template.attachment.solid

import scala.reflect.runtime.universe

import org.digimead.digi.lib.log.api.Loggable
import org.digimead.tabuddy.desktop.{ Messages => CoreMessages }
import org.digimead.tabuddy.desktop.logic.payload.PropertyType
import org.digimead.tabuddy.desktop.logic.payload.template.StringType
import org.digimead.tabuddy.desktop.support.App
import org.digimead.tabuddy.desktop.support.App.app2implementation
import org.digimead.tabuddy.desktop.support.Validator
import org.digimead.tabuddy.desktop.support.WritableValue
import org.digimead.tabuddy.desktop.support.WritableValue.wrapper2underlying
import org.digimead.tabuddy.model.dsl.attachment.solid.SolidAttachment
import org.digimead.tabuddy.model.element.Element
import org.eclipse.core.databinding.observable.ChangeEvent
import org.eclipse.jface.databinding.swt.WidgetProperties
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.jface.viewers.ColumnViewer
import org.eclipse.jface.viewers.ComboViewer
import org.eclipse.jface.viewers.ILabelProviderListener
import org.eclipse.jface.viewers.LabelProvider
import org.eclipse.jface.viewers.TextCellEditor
import org.eclipse.jface.viewers.ViewerCell
import org.eclipse.jface.viewers.ViewerColumn
import org.eclipse.swt.SWT
import org.eclipse.swt.events.VerifyEvent
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Text
import org.eclipse.ui.forms.events.HyperlinkAdapter
import org.eclipse.ui.forms.events.HyperlinkEvent
import org.eclipse.ui.forms.widgets.FormToolkit
import org.eclipse.ui.forms.widgets.ImageHyperlink
import org.digimead.tabuddy.desktop.logic.payload.template.attachment.solid

class SolidAttachmentType extends PropertyType[SolidAttachment] {
  /** The property that determines that enumeration is supported */
  val enumerationSupported: Boolean = false
  /** The property type name */
  val id = 'SolidAttachment
  /** The type class */
  val typeClass: Class[SolidAttachment] = classOf[SolidAttachment]
  /** The property that contains an adapter for the given type */
  def adapter(): PropertyType.Adapter[SolidAttachment] =
    new SolidAttachmentType.Adapter

  /**
   * Result of comparing 'value1' with 'value2'.
   * returns `x' where
   * x < 0 iff value1 < value2
   * x == 0 iff value1 == value2
   * x > 0 iff value1 > value2
   */
  def compare(value1: SolidAttachment, value2: SolidAttachment): Int = value1.name.compareTo(value2.name)
  /** Create an editor for the given type */
  def createEditor(initial: Option[SolidAttachment], propertyId: Symbol, element: Element.Generic): PropertyType.Editor[SolidAttachment] =
    new SolidAttachmentType.Editor(WritableValue(initial.getOrElse(null)), propertyId, element)
  /** Returns the new value */
  def createValue: SolidAttachment = throw new UnsupportedOperationException
  /** Returns an iterator for the new value generation */
  def createValues: Iterator[SolidAttachment] = throw new UnsupportedOperationException
  /** Create a viewer for the given type */
  def createViewer(initial: Option[SolidAttachment], propertyId: Symbol, element: Element.Generic): PropertyType.Viewer[SolidAttachment] =
    new SolidAttachmentType.Viewer(WritableValue(initial.getOrElse(null)), propertyId, element)
  /** Convert value to string */
  def valueToString(value: SolidAttachment): String = ""
  /** Convert string to value */
  def valueFromString(value: String): SolidAttachment = null
}

object SolidAttachmentType extends SolidAttachmentType with Loggable {
  class Adapter(implicit val tt: universe.TypeTag[Adapter]) extends PropertyType.Adapter[SolidAttachment] {
    /** Cell label provider singleton with limited API for proxy use case */
    val cellLabelProvider: PropertyType.CellLabelProviderAdapter[SolidAttachment] = new CellLabelProviderAdapter() {
      def dispose(viewer: ColumnViewer, column: ViewerColumn) = throw new UnsupportedOperationException
    }
    /** Label provider singleton with limited API for proxy use case */
    val labelProvider: PropertyType.LabelProviderAdapter[SolidAttachment] = new LabelProviderAdapter() {
      def addListener(listener: ILabelProviderListener) {}
      def dispose() {}
      def removeListener(listener: ILabelProviderListener) {}
    }

    /** Get a cell editor */
    def createCellEditor(parent: Composite, style: Int): CellEditor = new TextCellEditor(parent, style) {
      override protected def doSetValue(value: AnyRef) = // allow an incorrect value transformation, useful while types are changed
        if (value == null || value.isInstanceOf[String]) super.doSetValue("") else super.doSetValue(value)
    }
    /** Get a LabelProveder*/
    def createLabelProvider(): LabelProvider = new SolidAttachmentLabelProvider()
  }
  /**
   * SolidAttachmentType class that provides an editor widget
   */
  class Editor(val data: WritableValue[SolidAttachment], val propertyId: Symbol, val element: Element.Generic)(implicit val tt: universe.TypeTag[Editor])
    extends PropertyType.Editor[SolidAttachment] with AttachmentControl {
    protected val pattern = """[\p{Print}]*""".r.pattern
    /** Add the validator */
    def addValidator(control: Control, showOnlyOnFocus: Boolean): Option[Validator] =
      Some(Validator(control, showOnlyOnFocus)(validate))
    /** Get an UI control */
    def createControl(parent: Composite, style: Int, updateDelay: Int): Control =
      prepareControl(new Text(parent, style), updateDelay)
    /** Get an UI control */
    def createControl(toolkit: FormToolkit, parent: Composite, style: Int, updateDelay: Int): Control = {
      val link = toolkit.createImageHyperlink(parent, style)
      link.addHyperlinkListener(new HyperlinkAdapter() {
        override def linkActivated(e: HyperlinkEvent) = App.execNGet {
          val dialog = new solid.Editor(null, data, propertyId, element)
          //Window.currentShell.withValue(Some(dialog.getShell)) { dialog.open() }
        }
      })
      data.addChangeListener(updateLink(_, _, link))
      updateLink(data.value, null, link)
      link
    }
    /** Get a combo viewer UI control */
    def createCControl(parent: Composite, style: Int, updateDelay: Int): ComboViewer =
      throw new UnsupportedOperationException()
    /** Get a combo viewer UI control */
    def createCControl(toolkit: FormToolkit, parent: Composite, style: Int, updateDelay: Int): ComboViewer = {
      val viewer = createCControl(parent, style, updateDelay)
      toolkit.adapt(viewer.getCombo())
      viewer
    }
    /** Returns true if the data is empty, false otherwise. */
    def isEmpty = data.value == null //|| data.value.trim.isEmpty
    /** The validator function */
    def validate(validator: Validator, event: VerifyEvent) {
      if (event.text.nonEmpty && event.character != '\0')
        event.doit = pattern.matcher(event.text).matches()
      if (!event.doit)
        validator.withDecoration { validator.showDecorationError(_) }
      else
        validator.withDecoration { _.hide() }
    }
    protected def prepareControl(control: Control, updateDelay: Int): Control = {
      App.bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observeDelayed(updateDelay, control), data)
      control
    }
  }
  /**
   * SolidAttachmentType class that provides a viewer widget
   */
  class Viewer(val data: WritableValue[SolidAttachment], val propertyId: Symbol, val element: Element.Generic)(implicit val tt: universe.TypeTag[Viewer])
    extends PropertyType.Viewer[SolidAttachment] with AttachmentControl {
    /** Get an UI control */
    def createControl(parent: Composite, style: Int, updateDelay: Int): Control =
      prepareControl(new Label(parent, style), updateDelay)
    /** Get an UI control */
    def createControl(toolkit: FormToolkit, parent: Composite, style: Int, updateDelay: Int): Control = {
      val link = toolkit.createImageHyperlink(parent, style)
      link.addHyperlinkListener(new HyperlinkAdapter() {
        override def linkActivated(e: HyperlinkEvent) = App.execNGet {
          val dialog = new solid.Viewer(null, data, propertyId, element)
          //Window.currentShell.withValue(Some(dialog.getShell)) { dialog.open() }
        }
      })
      data.addChangeListener(updateLink(_, _, link))
      updateLink(data.value, null, link)
      link
    }
    /** Returns true if the data is empty, false otherwise. */
    def isEmpty = data.value == null //|| data.value.trim.isEmpty

    protected def prepareControl(control: Control, updateDelay: Int): Control = {
      App.bindingContext.bindValue(WidgetProperties.text().observeDelayed(updateDelay, control), data)
      control
    }
  }
  trait AttachmentControl extends PropertyType.Viewer[SolidAttachment] {
    protected def updateLink(value: SolidAttachment, event: ChangeEvent, link: ImageHyperlink) = Option(value) match {
      case Some(value) =>
        link.setText(value.name)
      //link.setImage(Resources.Image.appbar_question_medium)
      case None =>
        link.setText(CoreMessages.empty_text)
      //link.setImage(Resources.Image.appbar_page_bold_small)
    }
  }
  /*
   * Support classes
   */
  class CellLabelProviderAdapter extends PropertyType.CellLabelProviderAdapter[SolidAttachment] {
    /** Update the label for cell. */
    def update(cell: ViewerCell, value: Option[SolidAttachment]) = value match {
      case Some(value) if value != null => cell.setText(StringType.valueToString(null))
      case _ => cell.setText("")
    }
  }
  class LabelProviderAdapter extends PropertyType.LabelProviderAdapter[SolidAttachment] {
    /**
     * The <code>LabelProvider</code> implementation of this
     * <code>ILabelProvider</code> method returns the element's
     * <code>toString</code> string.
     */
    override def getText(value: Option[SolidAttachment]): String = value match {
      case Some(value) if value != null => StringType.valueToString(null)
      case _ => ""
    }
  }
  class SolidAttachmentLabelProvider extends LabelProvider {
    override def getText(element: AnyRef): String = element match {
      case value: String =>
        StringType.valueToString(value)
      case unknown =>
        log.fatal("Unknown item " + unknown.getClass())
        unknown.toString()
    }
  }
}
