package toolkit;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeaponNameGenerator {

	private ArrayList<ArrayList<String>> weaponClasses;
	private ArrayList<String> materials;
	private ArrayList<String> modifiersOrEnchantments;
	private ArrayList<String> flavors;

	public boolean loadDatabase(String weaponDatabaseFilename) {

		// Get list of files in current directory that matches our database filename
		File curDir = new File(weaponDatabaseFilename);
		// File curDir = new File(".");
		// File[] filesList = curDir.listFiles((file, s) -> {
		// if(s.equals(weaponDatabaseFilename)){
		// return true;
		// }
		//
		// return false;
		// });
		//
		// if(filesList.length < 1) {
		// return false;
		// }

		// Open weaponDatabaseFilename (Tab Seperated Values)
		FileReader weaponDatabaseFileReader = null;
		try {
			weaponDatabaseFileReader = new FileReader(curDir);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		BufferedReader weaponDatabaseBufferedReader = new BufferedReader(weaponDatabaseFileReader);

		try {
			weaponDatabaseBufferedReader.readLine(); // skip the header line
		} catch (IOException e) {
			e.printStackTrace();
			try {
				weaponDatabaseBufferedReader.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}

		// parse data

		// Database format
		// tabs seperate columns
		// newlines seperate rows
		// row 0: headers only (not useful)
		// column 1: weapon types
		// column 2: materials
		// column 3: modifiers / enchantments
		// column 4: flavors

		weaponClasses = new ArrayList<>();
		materials = new ArrayList<>();
		modifiersOrEnchantments = new ArrayList<>();
		flavors = new ArrayList<>();

		try {
			while (weaponDatabaseBufferedReader.ready()) {

				String line = weaponDatabaseBufferedReader.readLine();

				String[] rowCells = line.split("\t", -1);

				if (!rowCells[0].isEmpty()) {
					ArrayList<String> typeSynonyms = new ArrayList<>(
							Arrays.asList(rowCells[0].trim().split("\\s*,\\s*", -1)));
					weaponClasses.add(typeSynonyms);
				}
				if (!rowCells[1].isEmpty()) {
					materials.add(rowCells[1].trim());
				}
				if (!rowCells[2].isEmpty()) {
					modifiersOrEnchantments.add(rowCells[2].trim());
				}
				if (!rowCells[3].isEmpty()) {
					flavors.add(rowCells[3].trim());
				}
			}
			weaponDatabaseBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Name Generation Functions (One per format)

	// $material $type

	public List<String> generateMaterialType(List<Integer> weaponClassesIndices, List<Integer> materialsIndices) {
		List<String> nameList = new ArrayList<String>();

		for (Integer materialIndex : materialsIndices) {
			for (Integer weaponClassIndex : weaponClassesIndices) {

				for (String weaponSynonym : weaponClasses.get(weaponClassIndex)) {
					nameList.add(materials.get(materialIndex) + " " + weaponSynonym);
				}
			}
		}

		return nameList;
	}

	// $flavor $material $type

	public List<String> generateFlavorMaterialType(List<Integer> weaponClassesIndices, List<Integer> materialsIndices,
			List<Integer> flavorIndices) {

		List<String> nameList = new ArrayList<String>();

		for (Integer flavorIndex : flavorIndices) {
			for (Integer materialIndex : materialsIndices) {
				for (Integer weaponClassIndex : weaponClassesIndices) {

					for (String weaponSynonym : weaponClasses.get(weaponClassIndex)) {
						nameList.add(
								flavors.get(flavorIndex) + " " + materials.get(materialIndex) + " " + weaponSynonym);
					}
				}
			}
		}
		return nameList;
	}

	// $flavor $material $type "of" $modifier/enchanment

	public List<String> generateFlavorMaterialTypeOfModifierEnchantment(List<Integer> weaponClassesIndices,
			List<Integer> materialsIndices, List<Integer> modifiersEnchantmentsIndices, List<Integer> flavorIndices) {

		List<String> nameList = new ArrayList<String>();

		for (Integer flavorIndex : flavorIndices) {
			for (Integer materialIndex : materialsIndices) {
				for (Integer modifierEnchantmentIndex : modifiersEnchantmentsIndices) {
					for (Integer weaponClassIndex : weaponClassesIndices) {

						for (String weaponSynonym : weaponClasses.get(weaponClassIndex)) {
							nameList.add(flavors.get(flavorIndex) + " " + materials.get(materialIndex) + " "
									+ weaponSynonym + " of " + modifiersOrEnchantments.get(modifierEnchantmentIndex));
						}
					}
				}
			}
		}
		return nameList;
	}

	// $material $type "of" $modifier/enchanment

	public List<String> generateMaterialTypeOfModifierEnchantment(List<Integer> weaponClassesIndices,
			List<Integer> materialsIndices, List<Integer> modifiersEnchantmentsIndices) {
		List<String> nameList = new ArrayList<String>();

		for (Integer materialIndex : materialsIndices) {
			for (Integer modifierEnchantmentIndex : modifiersEnchantmentsIndices) {
				for (Integer weaponClassIndex : weaponClassesIndices) {

					for (String weaponSynonym : weaponClasses.get(weaponClassIndex)) {
						nameList.add(materials.get(materialIndex) + " " + weaponSynonym + " of "
								+ modifiersOrEnchantments.get(modifierEnchantmentIndex));
					}
				}
			}
		}
		return nameList;
	}

	// $modifier/enchantment $material $type

	public List<String> generateModifierEnchantmentMaterialType(List<Integer> weaponClassesIndices,
			List<Integer> materialsIndices, List<Integer> modifiersEnchantmentsIndices) {
		List<String> nameList = new ArrayList<String>();

		for (Integer materialIndex : materialsIndices) {
			for (Integer modifierEnchantmentIndex : modifiersEnchantmentsIndices) {
				for (Integer weaponClassIndex : weaponClassesIndices) {

					for (String weaponSynonym : weaponClasses.get(weaponClassIndex)) {
						nameList.add(modifiersOrEnchantments.get(modifierEnchantmentIndex) + " " + materials.get(materialIndex) + " " + weaponSynonym);
					}
				}
			}
		}
		return nameList;
	}
	
	// $modifier/enchantment $type
	
	public List<String> generateModifierEnchantmentType(List<Integer> weaponClassesIndices, List<Integer> modifiersEnchantmentsIndices){
		List<String> nameList = new ArrayList<String>();

		for (Integer modifierEnchantmentIndex : modifiersEnchantmentsIndices) {
			for (Integer weaponClassIndex : weaponClassesIndices) {

				for (String weaponSynonym : weaponClasses.get(weaponClassIndex)) {
					nameList.add(modifiersOrEnchantments.get(modifierEnchantmentIndex) + " " + weaponSynonym);
				}
			}
		}
		return nameList;
	}
	
	public List<String> generateTypeOfFlavor(List<Integer> weaponClassesIndices, List<Integer> flavorIndices){
		List<String> nameList = new ArrayList<String>();

		for (Integer flavorIndex : flavorIndices) {
				for (Integer weaponClassIndex : weaponClassesIndices) {

					for (String weaponSynonym : weaponClasses.get(weaponClassIndex)) {
						nameList.add(
								weaponSynonym + " of " + flavors.get(flavorIndex));
					}
				}
		}
		return nameList;
	}

	public ArrayList<ArrayList<String>> getWeaponClasses() {
		return weaponClasses;
	}

	public ArrayList<String> getMaterials() {
		return materials;
	}

	public ArrayList<String> getModifiersEnchantments() {
		return modifiersOrEnchantments;
	}

	public ArrayList<String> getFlavors() {
		return flavors;
	}

}
