import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Eigenfaces {

	private Mat imageMatrix;
	private Mat meanImage;
	private Mat eigenvectors;
	
	public Eigenfaces(ArrayList<Mat> images){
		imageMatrix = calculateImageMatrix(images);
		calculatePCA();
	}
	
	private Mat calculateImageMatrix(ArrayList<Mat> images){
		if(images.size() == 0)
			throw new IllegalArgumentException("No images...");
		if(images.get(0).type() != CvType.CV_64FC1)
			throw new IllegalArgumentException("Image is not b&w");
		
		int d = (int)images.get(0).total();
		int n = images.size();
		
		Mat ret = new Mat(n, d, CvType.CV_64FC1);
		
		for(int i = 0; i < n; i++){
			if(images.get(i).total() != d)
				throw new IllegalArgumentException("Images are not the same size");
			
			Mat row = ret.row(i);
			images.get(i).reshape(0,1).copyTo(row);
		}
		
		return ret;
	}
	
	private void calculatePCA(){
		meanImage = new Mat();
		eigenvectors = new Mat();
		Core.PCACompute(imageMatrix, meanImage, eigenvectors);
	}
	
	public Mat project(Mat input){
		Mat ret = new Mat();
		ArrayList<Mat> matar = new ArrayList<>();
		matar.add(input);
		Mat reshaped = calculateImageMatrix(matar); //MAKE COLUM VECTOR
		
		for(int i = 0; i < input.rows(); i++){
			Mat r = eigenvectors.row(i);
			Core.subtract(r, meanImage.reshape(1,1), r);
		}
		
		System.err.println(reshaped.dump());
		System.err.println(eigenvectors.dump());
		
		Core.gemm(reshaped, eigenvectors, 1, new Mat(), 0, ret);
		return ret;
	}
	
	public String toString(){
		return String.format("T:\n%s\nEigen:\n%s\nMean:\n%s\n",imageMatrix.dump(), eigenvectors.dump(), meanImage.dump());
	}
	
}
