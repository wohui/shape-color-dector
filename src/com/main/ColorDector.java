package com.main;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hui on 2018/2/11.
 */
public class ColorDector {

    /**
     * 写死颜色数组
     */

    private  double[] redRow = new double [] {136,208, 195,};//red
    private double[] greenRow = new double [] {224,42, 211,};//green
    private double[] blueRow = new double [] {82,207, 20,};//blue
    private String color ="";
    public String detect(Mat image, List<MatOfPoint> contours,int contourIdx){
        int width=image.width();
        int height=image.height();
        Mat lab = Mat.zeros(new Size(3,3),CvType.CV_8UC1);
        /**利用数组初始化颜色数组,
         * 先生成对角线为1的矩阵在放大255倍
         * 0：red
         * 1:green
         * 2:blue
         */

        /**
         *   此段代码本意是生成lab三通道矩阵，
         *   是想通过对角255矩阵生成
         *   [[[136 208 195]]
         *  [[224  42 211]]
         *  [[ 82 207  20]]]
         *  目前还没有调试完成
        Mat mat = Mat.eye( 3, 3, CvType.CV_64FC3 );
        mat = mat.mul(mat, 255);
        Imgproc.cvtColor(mat,lab,Imgproc.COLOR_RGB2Lab);
        System.out.println( "lab = " + lab.dump() );
        */

        Mat mask = Mat.zeros(height,width, CvType.CV_8UC1);   //声明全零矩阵
        Imgproc.drawContours(mask,contours,contourIdx,new Scalar(255,0,0),-1); //绘制contourIdx的单个图
        //Imgcodecs.imwrite("F:\\ideawork\\ShapeColorDetector\\imgs\\maskImg.jpg",mask);
        Mat erodeImg = new Mat();
        Imgproc.erode(mask,erodeImg,new Mat(),new Point(),2);
        //Imgcodecs.imwrite("F:\\ideawork\\ShapeColorDetector\\imgs\\ercode.jpg",erodeImg);
        Scalar scalar = Core.mean(image,erodeImg);
        //申明计算标准差后的结果数组
        double[] meanScalar = new double [] {
                scalar.val[0],
                scalar.val[1],
                scalar.val[2]
        };
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        /**
         *1.分别计算红色、绿色、蓝色和标准差的距离
         */
        double redDistance = euclideanDistance.compute(redRow,meanScalar);
        double greenDistance = euclideanDistance.compute(greenRow,meanScalar);
        double blueDistance = euclideanDistance.compute(blueRow,meanScalar);
        if (redDistance < greenDistance && redDistance < blueDistance){//红色距离最小
                color = "red";
        }else if (greenDistance < blueDistance){ //绿色距离最小
                color = "green";
        }else { //蓝色最小
                color = "blue";
        }

        /**
         * 其中第一个参数image表示目标图像，

         第二个参数contours表示输入的轮廓组，每一组轮廓由点vector构成，

         第三个参数contourIdx指明画第几个轮廓，如果该参数为负值，则画全部轮廓，

         第四个参数color为轮廓的颜色，

         第五个参数thickness为轮廓的线宽，如果为负值或CV_FILLED表示填充轮廓内部，
         */

        return color;
    }
}
