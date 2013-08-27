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

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Date
import scala.ref.WeakReference
import org.digimead.digi.lib.aop.log
import org.digimead.digi.lib.util.FileUtil
import org.digimead.digi.lib.util.Util
import org.digimead.tabuddy.desktop.{ Messages => CoreMessages }
import org.digimead.tabuddy.desktop.Resources
import org.digimead.tabuddy.desktop.support.WritableValue
import org.digimead.tabuddy.model.element.Element
import org.eclipse.jface.action.Action
import org.eclipse.jface.action.ActionContributionItem
import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.jface.dialogs.IInputValidator
import org.eclipse.jface.dialogs.InputDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.dnd.DND
import org.eclipse.swt.dnd.DragSourceEvent
import org.eclipse.swt.dnd.DragSourceListener
import org.eclipse.swt.dnd.DropTargetEvent
import org.eclipse.swt.dnd.DropTargetListener
import org.eclipse.swt.dnd.FileTransfer
import org.eclipse.swt.dnd.TextTransfer
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.widgets.FileDialog
import org.eclipse.swt.widgets.Shell
import org.eclipse.ui.forms.events.ExpansionEvent
import org.eclipse.ui.forms.events.IExpansionListener
import org.eclipse.swt.program.Program
import org.digimead.tabuddy.model.dsl.attachment.solid.SolidAttachment
import org.digimead.digi.lib.log.api.Loggable
import org.digimead.tabuddy.desktop.support.App

abstract class PropertyDialog(val parentShell: Shell)
  extends PropertyDialogSkel(parentShell) with org.digimead.tabuddy.desktop.definition.Dialog with Loggable {
  /** Payload value */
  val data: WritableValue[SolidAttachment]
  /** Element property id */
  val propertyId: Symbol
  /** Element that contains 'data' in property with 'propertyId' */
  val element: Element.Generic
  /** DND source listener */
  protected val dragSourceListener: DNDSource
  /** DND target listener */
  protected val dragTargetListener: DNDTarget

  /**
   * Create contents of the dialog.
   *
   * @param parent
   */
  override protected def createDialogArea(parent: Composite): Control = {
    val result = super.createDialogArea(parent)
    Option(new ActionContributionItem(ActionImport)).foreach { action =>
      action.fill(getCompositeLeft())
      action.getWidget().asInstanceOf[Button].
        setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1))
    }
    Option(new ActionContributionItem(ActionExport)).foreach { action =>
      action.fill(getCompositeLeft())
      action.getWidget().asInstanceOf[Button].
        setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1))
    }
    Option(new ActionContributionItem(ActionDelete)).foreach { action =>
      action.fill(getCompositeLeft())
      action.getWidget().asInstanceOf[Button].
        setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1))
    }
    Option(new ActionContributionItem(ActionEdit)).foreach { action =>
      action.fill(getCompositeLeft())
      action.getWidget().asInstanceOf[Button].
        setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1))
    }
    data.addChangeListener { (value, event) =>
      Option(data.value) match {
        case Some(value) =>
          setMessage(Messages.dialogMessage_text.format(value.name))
        case None =>
          setMessage(Messages.dialogMessageEmpty_text)
      }
      updateLogo(Option(data.value))
      updateGeneralSection(Option(data.value))
      updateSummarySection(Option(data.value))
      updatePreview(Option(data.value))
      updateActions(Option(data.value))
    }
    getSctnGeneral().addExpansionListener(new IExpansionListener {
      def expansionStateChanging(e: ExpansionEvent) {}
      def expansionStateChanged(e: ExpansionEvent) =
        getScrolledCompositeInformation.setMinSize(getCompositeInformation.computeSize(SWT.DEFAULT, SWT.DEFAULT))
    })
    getSctnSummary().addExpansionListener(new IExpansionListener {
      def expansionStateChanging(e: ExpansionEvent) {}
      def expansionStateChanged(e: ExpansionEvent) =
        getScrolledCompositeInformation.setMinSize(getCompositeInformation.computeSize(SWT.DEFAULT, SWT.DEFAULT))
    })
    getLblDNDTip().setFont(Resources.setFontStyle(Resources.fontSmall, SWT.ITALIC))
    createDragNDropZone()
    // update content
    updateLogo(Option(data.value))
    updateGeneralSection(Option(data.value))
    updateSummarySection(Option(data.value))
    updatePreview(Option(data.value))
    updateActions(Option(data.value))
    // Set the dialog title
    setTitle(Messages.dialogTitle_text.format(propertyId.name))
    // Set the dialog message
    Option(data.value) match {
      case Some(value) =>
        setMessage(Messages.dialogMessage_text.format(value.name))
      case None =>
        setMessage(Messages.dialogMessageEmpty_text)
    }
    // Set the dialog window title
    getShell().setText(Messages.dialogShell_text.format(element.eId.name))
    // Update size
    getScrolledCompositeInformation().setMinSize(getCompositeInformation().computeSize(SWT.DEFAULT, SWT.DEFAULT))
    result
  }
  /** Create drag source */
  protected def createDragNDropZone() {
    val source = getDragSourceInfo()
    source.setTransfer(Array(FileTransfer.getInstance()))
    source.addDragListener(dragSourceListener)
    val target = getDropTargetInfo()
    target.setTransfer(Array(FileTransfer.getInstance()))
    target.addDropListener(dragTargetListener)
  }
  /** Export an attachment */
  protected def exportAttachment(exportFile: File): Option[File] = Option(data.value) match {
    case Some(attachment) =>
      var result: Option[File] = None
      log.debug(s"export attachment '${attachment.name}' to " + exportFile.getAbsolutePath())
      attachment.attachment foreach { in =>
        var out: OutputStream = null
        try {
          if (exportFile.exists())
            exportFile.delete()
          exportFile.createNewFile()
          out = new BufferedOutputStream(new FileOutputStream(exportFile))
          FileUtil.writeToStream(in, out)
          result = Some(exportFile)
        } catch {
          case e: IOException =>
            log.error("unable to write the attachment: " + e.getMessage())
        } finally {
          try { in.close() } catch { case e: Throwable => }
          try { out.close() } catch { case e: Throwable => }
        }
      }
      result
    case None =>
      log.error(s"unable to export, the attachment is empty")
      None
  }
  /** Export an attachment to the spool directory */
  protected def exportToSpool(): Option[File] = {
    log.debug("create a temporary file for an attachment")
    val tmpDir = Some(File.createTempFile("TABuddyDesktop-", "-SolidAttachment"))
    tmpDir.flatMap { dir =>
      dir.delete()
      if (dir.mkdirs()) {
        var result: Option[File] = None
        Option(data.value) foreach { attachment =>
          attachment.attachment foreach { in =>
            val file = new File(dir, attachment.name)
            log.debug("create DND temporary file " + file.getAbsolutePath())
            file.deleteOnExit()
            dir.deleteOnExit()
            result = exportAttachment(file)
          }
        }
        result
      } else
        None
    }
  }
  /** Export an attachment to the temporary directory */
  protected def exportToTemp(): Option[File] = {
    log.debug("create a temporary file for an attachment")
    val tmpDir = Some(File.createTempFile("TABuddyDesktop-", "-SolidAttachment"))
    tmpDir.flatMap { dir =>
      dir.delete()
      if (dir.mkdirs()) {
        var result: Option[File] = None
        Option(data.value) foreach { attachment =>
          attachment.attachment foreach { in =>
            val file = new File(dir, attachment.name)
            log.debug("create DND temporary file " + file.getAbsolutePath())
            file.deleteOnExit()
            dir.deleteOnExit()
            result = exportAttachment(file)
          }
        }
        result
      } else
        None
    }
  }
  /** Import an attachment interactive */
  protected def importAttachmentInteractive(importFile: File) = Option(data.value) match {
    case Some(previous) =>
      log.debug(s"ask about to replace attachment with '${importFile.getName}'")
      val dialog = new Dialog.Import(PropertyDialog.this.getShell(), Messages.replaceFileDialogTitle_text.format(importFile.getName()),
        Messages.attachmentName_text, importFile.getName(), new Dialog.Validator, Some(previous.name))
//      if (Window.currentShell.withValue(Some(dialog.getShell)) { dialog.open() } == org.eclipse.jface.window.Window.OK)
//        importAttachment(importFile, dialog.getValue())
    case None =>
      log.debug(s"ask about to import attachment '${importFile.getName}'")
      val dialog = new Dialog.Import(PropertyDialog.this.getShell(), Messages.importFileDialogTitle_text.format(importFile.getName()),
        Messages.attachmentName_text, importFile.getName(), new Dialog.Validator, None)
//      if (Window.currentShell.withValue(Some(dialog.getShell)) { dialog.open() } == org.eclipse.jface.window.Window.OK)
//        importAttachment(importFile, dialog.getValue())
  }
  /** Import an attachment */
  protected def importAttachment(importFile: File, attachmentName: String) = Option(data.value) match {
    case Some(previous) =>
      log.debug(s"replace attachment with '${importFile.getName}'")
      data.value = SolidAttachment(attachmentName, previous.created, System.currentTimeMillis(), element, importFile)
    case None =>
      log.debug(s"import attachment '${importFile.getName}'")
      data.value = SolidAttachment(importFile.getName(), element, importFile)
  }
  /** Update the general section */
  @log
  protected def updateGeneralSection(attachment: Option[SolidAttachment]) {
    val size = attachment.map(_.size match {
      case Some(size) if (size > 1024 * 1024) =>
        "%.2fMb".format(size.toFloat / (1024 * 1024))
      case Some(size) if (size > 1024) =>
        "%.2fKb".format(size.toFloat / 1024)
      case Some(size) =>
        size + "b"
      case None =>
        CoreMessages.nodata_text
    }) getOrElse CoreMessages.nodata_text
    val created = attachment.map(a => Util.dateString(new Date(a.created))) getOrElse CoreMessages.nodata_text
    val modified = attachment.map(a => Util.dateString(new Date(a.modified))) getOrElse CoreMessages.nodata_text
    val mime = CoreMessages.nodata_text
    val application = CoreMessages.nodata_text
    log.___glance("!??" + Program.findProgram(".pdf"))
    //log.___glance("!??" + Program.launch(""))
    log.___glance("!!!" + Program.getExtensions().mkString("\n"))
    getTxtFileSize().setText(size)
    getTxtFileCreated().setText(created)
    getTxtFileModified().setText(modified)
    getTxtFileFormat().setText(mime)
    getTxtFileApplication().setText(application)
  }
  /** Update buttons */
  @log
  protected def updateActions(attachment: Option[SolidAttachment]) {
    attachment match {
      case Some(attachment) =>
        ActionImport.setEnabled(true)
        ActionExport.setEnabled(true)
        ActionDelete.setEnabled(true)
        ActionEdit.setEnabled(true)
      case None =>
        ActionImport.setEnabled(true)
        ActionExport.setEnabled(false)
        ActionDelete.setEnabled(false)
        ActionEdit.setEnabled(false)
        ActionEdit.setChecked(false)
    }
  }
  /** Update logo label */
  @log
  protected def updateLogo(attachment: Option[SolidAttachment]) {
    attachment match {
      case Some(attachment) =>
        //getLblLogo().setImage(Resources.Image.appbar_page_question_large)
      case None =>
        //getLblLogo().setImage(Resources.Image.appbar_page_add_large)
    }
  }
  /** Update the preview block */
  @log
  protected def updatePreview(attachment: Option[SolidAttachment]) {
    getLblPreview().setText(Messages.noPreview_text)
  }
  /** Update the summary section */
  @log
  protected def updateSummarySection(attachment: Option[SolidAttachment]) {
    val documentTitle = CoreMessages.nodata_text
    val author = CoreMessages.nodata_text
    val authorTitle = CoreMessages.nodata_text
    val description = CoreMessages.nodata_text
    getTxtDocumentTitle().setText(documentTitle)
    getTxtAuthor().setText(author)
    getTxtAuthorTitle().setText(authorTitle)
    getTxtDescription().setText(description)
  }

  class DNDSource extends DragSourceListener() {
    @volatile protected var tmpFile: Option[File] = None

    def dragStart(event: DragSourceEvent) {
      // Only start the drag if there is actual data - this data will be what is dropped on the target.
      event.doit = Option(data.value).nonEmpty
    }
    def dragSetData(event: DragSourceEvent) {
      event.data = null
      event.doit = false
      // Provide the data of the requested type.
      if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
        exportToTemp().foreach { file =>
          event.data = Array[String](file.getAbsolutePath())
          event.doit = true
        }
      } else if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
        Option(data.value) foreach { attachment =>
          event.data = Array[String](attachment.name)
          event.doit = true
        }
      }
    }
    def dragFinished(event: DragSourceEvent) {
      tmpFile.foreach { file =>
        log.debug("clean DND temporary file " + file)
        tmpFile = None
        file.listFiles().foreach(_.delete)
        file.delete()
      }
    }
  }
  class DNDTarget extends DropTargetListener() {
    def dragEnter(event: DropTargetEvent) = drag(event)
    def dragOver(event: DropTargetEvent) {}
    def dragOperationChanged(event: DropTargetEvent) = drag(event)
    def dragLeave(event: DropTargetEvent) {}
    def dropAccept(event: DropTargetEvent) {}
    def drop(event: DropTargetEvent) {
      if (event.data == null) {
        event.detail = DND.DROP_NONE
        return
      }
      if (FileTransfer.getInstance().isSupportedType(event.currentDataType))
        event.data.asInstanceOf[Array[String]].headOption.foreach(file => importAttachmentInteractive(new File(file)))
      else
        log.info("unsupported dnd type")
    }
    protected def drag(event: DropTargetEvent) {
      event.detail match {
        case DND.DROP_DEFAULT if (event.operations & DND.DROP_COPY) != 0 =>
          event.detail = DND.DROP_COPY
        case DND.DROP_DEFAULT if (event.operations & DND.DROP_MOVE) != 0 =>
          event.detail = DND.DROP_COPY
        case DND.DROP_NONE =>
          event.detail = DND.DROP_COPY
        case _ =>
          event.detail = DND.DROP_NONE
      }
      val supported = for (i <- 0 until event.dataTypes.length)
        yield FileTransfer.getInstance().isSupportedType(event.dataTypes(i))
      if (supported.forall(_ == false))
        event.detail = DND.DROP_NONE
    }
  }

  object ActionImport extends Action(Messages.import_text) {
    @log
    def apply() = App.exec {
      val dialog = new FileDialog(PropertyDialog.this.getShell(), SWT.NONE)
      if (Option(PropertyDialog.this.data.value).isEmpty)
        dialog.setText(Messages.importFileTitle_text)
      else
        dialog.setText(Messages.replaceFileTitle_text)
      //      val file = Option(Window.currentShell.withValue(Some(dialog.getParent)) { dialog.open() })
      //      file.foreach(f => area.importAttachmentInteractive(new File(f)))
    }
    override def run = apply()
  }
  object ActionExport extends Action(Messages.export_text) {
    @log
    def apply() = App.exec {
      val dialog = new FileDialog(PropertyDialog.this.getShell(), SWT.SAVE)
      dialog.setText(Messages.exportFileTitle_text.format(PropertyDialog.this.data.value.name))
      dialog.setOverwrite(true)
      dialog.setFileName(PropertyDialog.this.data.value.name)
      //val file = Option(Window.currentShell.withValue(Some(dialog.getParent)) { dialog.open() })
      //file.foreach(f => area.exportAttachment(new File(f)))
    }
    override def run = apply()
  }
  object ActionDelete extends Action(Messages.delete_text) {
    @log
    def apply() = {
      SolidAttachment.clear(PropertyDialog.this.data.value)
      PropertyDialog.this.data.value = null
    }
    override def run = apply()
  }
  object ActionEdit extends Action(Messages.edit_text) {
    def apply() = {
      log.___gaze("BOOM")
      // spool
      // monitor
      // close
    }
    override def run = apply()
  }
}

object Dialog extends Loggable {
  /** Import dialog based on InputDialog */
  class Import(parentShell: Shell, dialogTitle: String, dialogMessage: String, initialValue: String, validator: IInputValidator,
    val resetName: Option[String]) extends InputDialog(parentShell, dialogTitle, dialogMessage, initialValue, validator) {
    @volatile var resetNameButton: Option[Button] = None

    override protected def createButtonsForButtonBar(parent: Composite) {
      resetNameButton = Option(createButton(parent, IDialogConstants.CLIENT_ID, Messages.resetName_text, true))
      updateResetNameButton()
      super.createButtonsForButtonBar(parent)
    }
    override protected def createDialogArea(parent: Composite): Control = {
      val result = super.createDialogArea(parent)
      getText.addModifyListener(new ModifyListener {
        def modifyText(e: ModifyEvent) = updateResetNameButton
      })
      result
    }
    protected def updateResetNameButton() = resetName match {
      case Some(name) if getText().getText().trim == name =>
        resetNameButton.foreach(_.setEnabled(false))
      case None =>
        resetNameButton.foreach(_.setEnabled(false))
      case _ =>
        resetNameButton.foreach(_.setEnabled(true))
    }
  }
  /**
   * This class validates a String. It makes sure that the String is between 5 and 8
   * characters
   */
  class Validator extends IInputValidator {
    val pattern = org.digimead.tabuddy.model.dsl.attachment.solid.SolidAttachment.validName.pattern
    /**
     * Validates the String. Returns null for no error, or an error message
     *
     * @param newText the String to validate
     * @return String
     */
    def isValid(newText: String): String = {
      val len = newText.length()
      // Determine if input is too short or too long
      if (len < 1) return "* " + Messages.tooShort
      if (len > 255) return "* " + Messages.tooLong
      if (!pattern.matcher(newText).matches()) return "Invalid"
      // Input must be OK
      return null
    }
  }
}
