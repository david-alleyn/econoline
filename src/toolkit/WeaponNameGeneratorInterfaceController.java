package toolkit;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class WeaponNameGeneratorInterfaceController implements Initializable{
	
	WeaponNameGenerator nameGenerator = null;
	
	@FXML private ListView<String> weaponTypesList;
	@FXML private ListView<String> materialsList;
	@FXML private ListView<String> modifiersEnchantmentsList;
	@FXML private ListView<String> flavorList;
	
	@FXML private ChoiceBox<String> formatPicker;
	@FXML private Spinner<Integer> outputLimitSpinner;
	
	@FXML private TextArea resultsTextArea;
	@FXML private Button generateNamesButton;
	@FXML private Button loadDatabaseButton;
	
	@FXML private CheckBox randomizeCheckbox;
	
	@FXML protected void handleGenerateNamesButton(ActionEvent event) {
//		resultsTextArea.setText(resultsTextArea.getText() + "\nhandleGenerateNamesButton Testing");
		
		//Assumptions: A list with NO picks implies having selected ALL.
		
		List<Integer> weaponClassIndices = weaponTypesList.getSelectionModel().getSelectedIndices();
		List<Integer> materialIndices = materialsList.getSelectionModel().getSelectedIndices();
		List<Integer> modifiersEnchantmentsIndices = modifiersEnchantmentsList.getSelectionModel().getSelectedIndices();
		List<Integer> flavorIndices = flavorList.getSelectionModel().getSelectedIndices();
		
		if(weaponClassIndices.size() == 0) {
			weaponClassIndices = new ArrayList<Integer>();
			for(Integer i = 0; i < nameGenerator.getWeaponClasses().size(); i++) {
				
				weaponClassIndices.add(i);
			}
		}
		
		if(materialIndices.size() == 0) {
			materialIndices = new ArrayList<Integer>();
			for(Integer i = 0; i < nameGenerator.getMaterials().size(); i++) {
				
				materialIndices.add(i);
			}
		}
		
		if(modifiersEnchantmentsIndices.size() == 0) {
			modifiersEnchantmentsIndices = new ArrayList<Integer>();
			for(Integer i = 0; i < nameGenerator.getModifiersEnchantments().size(); i++) {
				
				modifiersEnchantmentsIndices.add(i);
			}
		}
		
		if(flavorIndices.size() == 0) {
			flavorIndices = new ArrayList<Integer>();
			for(Integer i = 0; i < nameGenerator.getFlavors().size(); i++) {
				
				flavorIndices.add(i);
			}
		}
		
		//for format selection, see the order of the ObservableList in the Initialize function below
		// Here is a copy for reference
		
//		"Material Type", 
//		"Flavor Material Type", 
//		"Flavor Material Type OF Modifier", 
//		"Material Type OF Modifier", 
//		"Modifier Material Type"
		
		List<String> outputList = null;
		
		switch(formatPicker.getSelectionModel().getSelectedIndex()) {
		case 0:
			outputList = nameGenerator.generateMaterialType(weaponClassIndices, materialIndices);
			break;
		case 1:
			outputList = nameGenerator.generateFlavorMaterialType(weaponClassIndices, materialIndices, flavorIndices);
			break;
		case 2:
			outputList = nameGenerator.generateFlavorMaterialTypeOfModifierEnchantment(weaponClassIndices, materialIndices, modifiersEnchantmentsIndices, flavorIndices);
			break;
		case 3:
			outputList = nameGenerator.generateMaterialTypeOfModifierEnchantment(weaponClassIndices, materialIndices, modifiersEnchantmentsIndices);
			break;
		case 4:
			outputList = nameGenerator.generateModifierEnchantmentMaterialType(weaponClassIndices, materialIndices, modifiersEnchantmentsIndices);
		}
		
		Collections.shuffle(outputList);
		
		if(randomizeCheckbox.isSelected()) {
			Collections.shuffle(outputList);
		}
		
		if(outputList.size() > outputLimitSpinner.getValue()) {
		
			outputList = outputList.subList(0, outputLimitSpinner.getValue());
		}
	    
	    StringBuilder resultsTextString = new StringBuilder();
	    
	    for(String line : outputList) {
	    	resultsTextString.append(line + "\n");
	    }
		
		resultsTextArea.setText(resultsTextString.toString());
	}
	
	@FXML protected void handleLoadDatabaseButton(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Locate the database TSV file");
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Tab Seperated Values", "tsv"));
		File file = fileChooser.showOpenDialog(((Node)event.getTarget()).getScene().getWindow());
		
		nameGenerator = new WeaponNameGenerator();
		nameGenerator.loadDatabase(file.getAbsolutePath());
		
		ArrayList<ArrayList<String>> weaponClasses = nameGenerator.getWeaponClasses();
		ArrayList<String> materials = nameGenerator.getMaterials();
		ArrayList<String> modifiersEnchantments = nameGenerator.getModifiersEnchantments();
		ArrayList<String> flavors = nameGenerator.getFlavors();
		
		ArrayList<String> weapons = new ArrayList<String>();
		
		for(ArrayList<String> weaponClass : weaponClasses) {
			StringBuilder weaponSynonyms = new StringBuilder();
			for(String synonym : weaponClass) {
				weaponSynonyms.append(synonym + ", ");
			}
			weapons.add(weaponSynonyms.toString());
		}
		
		ObservableList<String> weaponObservableList = FXCollections.observableArrayList(weapons);
		
		weaponTypesList.setItems(weaponObservableList);
		
		generateNamesButton.setDisable(false);
		
		ObservableList<String> materialsObservableList = FXCollections.observableArrayList(materials);
		materialsList.setItems(materialsObservableList);
		
		ObservableList<String> modifiersEnchantmentsObservableList = FXCollections.observableArrayList(modifiersEnchantments);
		modifiersEnchantmentsList.setItems(modifiersEnchantmentsObservableList);
		
		ObservableList<String> flavorsObservableList = FXCollections.observableArrayList(flavors);
		flavorList.setItems(flavorsObservableList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		generateNamesButton.setDisable(true);
		weaponTypesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		materialsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		modifiersEnchantmentsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		flavorList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		//Build Format Picker choices.
		
		formatPicker.setItems(FXCollections.observableArrayList(
				"Material Type", 
				"Flavor Material Type", 
				"Flavor Material Type OF Modifier", 
				"Material Type OF Modifier", 
				"Modifier Material Type"));
		formatPicker.getSelectionModel().select(0);
		
		//Output limit spinner
		outputLimitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20000, 20000));
		
		
		
		randomizeCheckbox.setSelected(true);
	}
}
