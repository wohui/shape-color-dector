package com.main;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Created by hui on 2018/2/11.
 */
public class ColorDector {
    public String detect(Mat image, List<MatOfPoint> contours,int contourIdx){
        int width=image.width();
        int height=image.height();

        Mat mask = Mat.zeros(height,width, CvType.CV_8UC1);   //声明全零矩阵
        Imgproc.drawContours(mask,contours,contourIdx,new Scalar(255,0,0),-1); //绘制contourIdx的单个图
        Imgcodecs.imwrite("F:\\ideawork\\ShapeColorDetector\\imgs\\maskImg.jpg",mask);
        Mat erodeImg = new Mat();
        Imgproc.erode(mask,erodeImg,new Mat(),new Point(),2);
        Imgcodecs.imwrite("F:\\ideawork\\ShapeColorDetector\\imgs\\ercode.jpg",erodeImg);
        Scalar scalar = Core.mean(image,erodeImg);
        double[] row = new double [] {
                136,
                208,
                195,
        };
        //申明计算标准差后的结果数组
        double[] meanScalar = new double [] {
                scalar.val[0],
                scalar.val[1],
                scalar.val[2]
        };
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        double distance = euclideanDistance.compute(row,meanScalar);

        /**
         * 其中第一个参数image表示目标图像，

         第二个参数contours表示输入的轮廓组，每一组轮廓由点vector构成，

         第三个参数contourIdx指明画第几个轮廓，如果该参数为负值，则画全部轮廓，

         第四个参数color为轮廓的颜色，

         第五个参数thickness为轮廓的线宽，如果为负值或CV_FILLED表示填充轮廓内部，
         */
        return "";
    }
}
