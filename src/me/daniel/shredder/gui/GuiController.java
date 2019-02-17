package me.daniel.shredder.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import me.daniel.shredder.api.Shredder;

public class GuiController {
	@FXML
	public Button input_button, output_button, shred_button;
	@FXML
	public CheckBox compress;
	@FXML
	public TextField label_input, label_output, shred_count;
	@FXML
	public ListView<String> list;

	//Called when the "Open" button is clicked
	//Selects the file to use as input for shredding
	public void onOpenInputClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Input File");
		File input = fileChooser.showOpenDialog(null);
		if(input == null || !input.canRead()) {
			label_input.setText("Error: File not readable.");
			return;
		}
		
		label_input.setText(input.getAbsolutePath());
	}
	
	//Called when the "Select" button is clicked
	//Selects the output directory
	public void onOpenOutputClick() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Select Output Folder");
		File output = fileChooser.showDialog(null);
		if(output == null || !output.canWrite()) {
			label_output.setText("Error: Folder not writable.");
			return;
		}
		
		label_output.setText(output.getAbsolutePath());
	}
	
	//Called when the "Shred" button is clicked
	//Collects settings from the other GUI elements,
	//such as "shred_count" and "compress", and then
	//proceeds to shred the input file.
	//Updates the ListView with the paths of files
	//written as a result.
	public void onShredClick() {
		if(label_input.getText().trim().isEmpty()) return;
		if(label_output.getText().trim().isEmpty()) return;
		
		File input = new File(label_input.getText());
		File output = new File(label_output.getText());
		
		int count = 3;
		try {
			count = Integer.valueOf(shred_count.getText());
		} catch(NumberFormatException e) {
			//default to 3 shreds.
			shred_count.setText("3");
		}
		
		try {
			Shredder.shredFile(input, compress.isSelected(), count);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> outputs = Shredder.save(output, input.getName());
		list.setItems(FXCollections.observableArrayList(outputs));
	}

}
