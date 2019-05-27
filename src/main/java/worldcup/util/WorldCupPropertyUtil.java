package worldcup.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class WorldCupPropertyUtil {
    private static WorldCupPropertyUtil instance;
    private Properties properties = new Properties();
    private static final String PROPERTY_FILE_PATH = "src/main/resources/worldcup.properties";

    public static synchronized WorldCupPropertyUtil getInstance() {
        if(instance == null){
            try {
                instance = new WorldCupPropertyUtil();
                FileReader fileReader = new FileReader(PROPERTY_FILE_PATH);
                instance.properties.load(fileReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
