/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rules;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author zS20006736
 */

public class AccessCodes {

    private static final String PROPERTY_FILE = "access-codes.properties";
    private static Properties properties;

    static {
        loadProperties();
    }

    public static String getCode(String key) {
        return properties.getProperty(key);
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = AccessCodes.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

