package toolkit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class EconolineToolkit {

    public static void main(String[] args) {
	// write your code here
        if (args[0].equals("WeaponNameGenerator")) {
            if( args.length > 1 ) {
                if (!args[1].isEmpty()) {

                    //RunWeaponNameGenerator(args[1]);

                }
            }
        }
    }

//    public static void RunWeaponNameGenerator(String weaponDatabaseFilename){
//        WeaponNameGenerator weaponGenerator = new WeaponNameGenerator();
//        List<String> flavors = null;
//        List<String> flavorMaterialType = null;
//        if( weaponGenerator.loadDatabase(weaponDatabaseFilename) ){
//            flavors = weaponGenerator.getFlavors();
//            flavorMaterialType = weaponGenerator.generateFlavorMaterialType();
//        }
//
//        if(flavors != null) {
//
////            for (String name : flavors) {
////                System.out.println(name);
////            }
//            writeStringListAsLinesToFile(flavors, "Flavors.txt");
//        }
//
//        if(flavorMaterialType != null) {
////            for (String name : flavorMaterialType) {
////                System.out.println(name);
////            }
//            writeStringListAsLinesToFile(flavorMaterialType, "FlavorMaterialType.txt");
//        }
//
//    }

    public static void writeStringListAsLinesToFile(List<String> stringList, String fileName){
        if(stringList == null
                || stringList.isEmpty()
                || fileName == null
                || fileName.isEmpty()){
            return;
        }

        Path file = null;
        try {
            file = Paths.get(fileName);
            Files.write(file, stringList, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
