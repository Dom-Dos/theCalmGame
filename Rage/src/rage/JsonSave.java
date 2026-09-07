package rage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonSave {
	static String SaveFile = "saves.json";
	
	public static void save(GamePanel gp) {
		StringBuilder json = new StringBuilder();
		json.append("{/n");
		json.append(" \"currentLevel\" :").append(gp.getCurrentLevel()).append("/n");
		json.append(" \"playTime\" :").append(gp.getPlayTimeMs());
		json.append("/n");
		json.append("}");
	try {
		Path path = Paths.get(SaveFile);
		Files.writeString(path, json.toString());
		System.out.println("Gespeichert");
	}catch (IOException e) {
		e.printStackTrace();
	}
	
}
	public static SaveData load() {
		Path path = Paths.get(SaveFile);
		try {
			String content = Files.readString(path);
			SaveData infos = new SaveData();
			infos.currentLevel = parseIntValue(content,"currentLevel");
			infos.playTimeMs = parseLongValue(content,"playTimeMs");
			System.out.println("Geladen");
			return infos;
		} catch (IOException e) {
            e.printStackTrace();
            return null;
        } 
        	
        
		
	}
	// Hilfsmethode: Extrahiert Integer-Werte aus dem JSON-Text
    private static int parseIntValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int index = json.indexOf(searchKey);
        System.out.println("Index = "+index);
        if (index == -1) return 0;

        int start = index + searchKey.length();
        int end = json.indexOf(",", start);
        if (end == -1) { // Falls es der letzte Wert im JSON-Objekt ist
            end = json.indexOf("\n", start);
            if (end == -1) end = json.indexOf("}", start);
        }
        System.out.println("end = " + end +" start = "+start);
        String valueStr = json.substring(start, end).trim();
        return Integer.parseInt(valueStr);
    }

    // Hilfsmethode: Extrahiert Long-Werte aus dem JSON-Text
    private static long parseLongValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int index = json.indexOf(searchKey);
        if (index == -1) return 0L;

        int start = index + searchKey.length();
        int end = json.indexOf(",", start);
        if (end == -1) {
            end = json.indexOf("\n", start);
            if (end == -1) end = json.indexOf("}", start);
        }

        String valueStr = json.substring(start, end).trim();
        return Long.parseLong(valueStr);
    }
}