package me.daniel.shredder.gui;

import java.io.File;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

/**
 * Enables quicker filling of the input and output fields by dropping
 * files into them.
 */
public class DragHandler implements EventHandler<DragEvent> {

	private final TextField source; //The control we're dropping onto
	private final boolean file_mode; //Should this instance accept files or directories?
	
	/**
	 * @param source The text field this handelr is targetted at
	 * @param file_mode File or directory mode?
	 */
	public DragHandler(TextField source, boolean file_mode) {
		this.source = source;
		this.file_mode = file_mode;
	}
	
	@Override
	public void handle(DragEvent event) {
		if(event.getGestureSource() != source && event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			File file = event.getDragboard().getFiles().get(0);
			
			//Only allow files xor directories, not both
			//Don't allow more than one file to be dropped
			if(file_mode && file.isDirectory() || !file_mode && !file.isDirectory() || event.getDragboard().getFiles().size() > 1) {
				event.acceptTransferModes(TransferMode.NONE);
			}
			
			//Only update the text field when the file is dropped
			if(event.getEventType().equals(DragEvent.DRAG_DROPPED)) {
				source.setText(file.getAbsolutePath());
				event.setDropCompleted(true);
			}
		}
		event.consume();
	}

}
