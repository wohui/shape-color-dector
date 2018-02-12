package com.main;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.*;

public class DetecShapeColor {
    private static Map<String, Integer> shapeResult = new HashMap<String, Integer>();
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Welcome to OpenCV " + Core.VERSION);

        //读入图片F:\ideawork\ShapeColorDetector\imgs\sc.png
        Mat image = Imgcodecs.imread("D:\\EWork\\shape-color-dector\\imgs\\scsc.png");
        //缩放之后的图片
        Mat imageResized = image.clone();
        float width=image.width();
        float height=image.height();
        //缩放图片
        Imgproc.resize(image, imageResized, new Size(300,height*(300/width)));
        //Imgcodecs.imwrite("/../imgs/resize.jpg",imageResized);
        float ratio = image.width() / imageResized.width();//计算比例
        //模糊图像
        Mat blurredImg = imageResized.clone();
        Imgproc.GaussianBlur(imageResized,blurredImg,new Size(5,5),0);
        //彩色空间转换
        Mat grayImg = blurredImg.clone();
        Mat labImg = blurredImg.clone();
        Imgproc.cvtColor(blurredImg, grayImg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(blurredImg, labImg, Imgproc.COLOR_BGR2Lab);

        //生成阈值图像
        Mat threshImg = grayImg.clone();
        Imgproc.threshold(grayImg, threshImg, 60, 255, Imgproc.THRESH_BINARY);
        //找轮廓
//    cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL,
//    		cv2.CHAIN_APPROX_SIMPLE)
//    	cnts = cnts[0] if imutils.is_cv2() else cnts[1]
        //定义2个
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(threshImg,contours,hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        //计算轮廓距
        List<Moments> momList = new ArrayList<>(contours.size());
        Moments momentsItem;
        shapeResult.put("triangle",0);
        shapeResult.put("square",0);
        shapeResult.put("rectangle",0);
        shapeResult.put("pentagon",0);
        shapeResult.put("circle",0);
        for( int i = 0; i < contours.size(); i++ ){
            String shape;
            String color;
            MatOfPoint2f  newMatOfPoint2f = new MatOfPoint2f( contours.get(i).toArray() );
//            momentsItem =  Imgproc.moments(contours.get(i), false);
//            int cx = (int) ((int) (momentsItem.get_m10() / momentsItem.get_m00()) *ratio) ;
//            int cy = (int) ((int) (momentsItem.get_m01() / momentsItem.get_m00()) *ratio);
            ShapeDector  shapeDector = new ShapeDector();
            shape = shapeDector.detect(contours.get(i),newMatOfPoint2f);
            //根据形状分别计算数量
            switch (shape){
                case "triangle":
                    shapeResult.put("triangle",shapeResult.get("triangle")+1);
                    break;
                case "square":
                    shapeResult.put("square", shapeResult.get("square") + 1);
                    break;
                case "rectangle":
                    shapeResult.put("rectangle", shapeResult.get("rectangle") + 1);
                    break;
                case "pentagon":
                    shapeResult.put("pentagon",shapeResult.get("pentagon")+1);
                    break;
                default:
                    shapeResult.put("circle",shapeResult.get("circle")+1);
                    break;
            }
            /**
             * 颜色检测
             */
            ColorDector colorDector = new ColorDector();
            color = colorDector.detect(labImg,contours,i);
            System.out.println("第"+i+"个图形是color:"+color+"---shape:"+shape);
        }

//        Set set = shapeResult.entrySet();
//        Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
//        for (int i=0;i<entries.length;i++){
//            System.out.println("形状:"+entries[i].getKey().toString()+"---数量:"+entries[i].getValue().toString());

//        }

    }

}

