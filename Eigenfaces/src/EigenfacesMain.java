

import org.opencv.core.*;
import org.opencv.highgui.Highgui;

public class EigenfacesMain {

	public static void main(String args[]){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat m = Highgui.imread("pics/Richardson.jpg");
		System.out.println(m);
	}
	
}
