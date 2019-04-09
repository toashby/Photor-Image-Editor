/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoeditor.Properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author ta428
 */
public class ThemeProperty {
    public void SaveProperty(String theme){
        Properties prop = new Properties();
	OutputStream output = null;

	try {

		output = new FileOutputStream("config.properties");

		// set the properties value
		prop.setProperty("theme", theme);
		//prop.setProperty("new", "more");

		// save properties to project root folder
		prop.store(output, null);

	} catch (IOException io) {
		io.printStackTrace();
	} finally {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
    }
    
    public String LoadProperty() throws IOException{
       Properties prop = new Properties();
	InputStream input = null;

	try {

		input = new FileInputStream("config.properties");

		// load a properties file
		prop.load(input);

		 System.out.println("theme " + prop.getProperty("theme"));
        
                if(prop.getProperty("theme").equals("0")){
                     return "default";
                }
                if(prop.getProperty("theme").equals("1")){
                    return "dark";
                } 
                if(prop.getProperty("theme").equals("2")){
                    return "blue";
                }
                if(prop.getProperty("theme").equals("3")){
                    return "largeText";
                }

	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        return "";
    }
    
}
