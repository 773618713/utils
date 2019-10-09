package com.scy.utils.opencv;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

/**
 * OpenCV的测试类
 */
public class Test {
    public static void main(String[] args) {
        try {
            //载入OpenCV的动态链接库
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            //读取图像到矩阵中
            Mat src = Imgcodecs.imread("C:/Users/sun/Desktop/190733878666/62_1.png");

            //判断图片是否存在
            if (src.empty()) {
                throw new Exception("no file");
            }


            int xMargin, yMargin;
            int x0 = src.cols() / 4;
            int x1 = (src.cols() / 4) * 3;
            int y0 = src.cols() / 4;
            int y1 = (src.cols() / 4) * 3;
            Mat dst = new Mat();

            //原始的四个角 的定位点  （左上，左下，右下，右上）
            List<Point> listSrcs = java.util.Arrays.asList(new Point(304, 627), new Point(304, 2915), new Point(1979, 2920), new Point(1937, 602));
            Mat srcPoints = Converters.vector_Point_to_Mat(listSrcs, CvType.CV_32F);

            xMargin = src.cols() / 10;
            yMargin = src.rows() / 10;

            //变换后的 的四个角 的定位点  （左上，左下，右下，右上）
            List<Point> listDsts = java.util.Arrays.asList(new Point(110, 170), new Point(110, 1725), new Point(1200, 1725), new Point(1200, 170));
            Mat dstPoints = Converters.vector_Point_to_Mat(listDsts, CvType.CV_32F);

            Mat perspectiveMmat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
            Imgproc.warpPerspective(src, dst, perspectiveMmat, src.size(), Imgproc.INTER_LINEAR);
            Imgcodecs.imwrite("C:/Users/sun/Desktop/190733878666/62_11.png", dst);

            xMargin = src.cols() / 8;
            yMargin = src.cols() / 8;
            listDsts.set(0, listSrcs.get(0));
            listDsts.set(1, listSrcs.get(1));
            listDsts.set(2, new Point(x1 - xMargin, y1 - yMargin));
            listDsts.set(3, new Point(x1 - xMargin, y0 - yMargin));
            dstPoints = Converters.vector_Point_to_Mat(listDsts, CvType.CV_32F);

            perspectiveMmat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
            Imgproc.warpPerspective(src, dst, perspectiveMmat, src.size(), Imgproc.INTER_LINEAR);
            Imgcodecs.imwrite("C:/Users/sun/Desktop/190733878666/62_12.png", dst);

            xMargin = src.cols() / 6;
            yMargin = src.cols() / 6;
            listDsts.set(0, new Point(x0 + xMargin, y0 + yMargin));
            listDsts.set(1, listSrcs.get(1));
            listDsts.set(2, new Point(x1 - xMargin, y1 - yMargin));
            listDsts.set(3, listSrcs.get(3));
            dstPoints = Converters.vector_Point_to_Mat(listDsts, CvType.CV_32F);

            perspectiveMmat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
            Imgproc.warpPerspective(src, dst, perspectiveMmat, src.size(), Imgproc.INTER_LINEAR);

            Imgcodecs.imwrite("C:/Users/sun/Desktop/190733878666/62_13.png", dst);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("例外：" + e);
        }
    }

    /**
     * 透视变换
     */
    public static void pic() {
        try {
            String picStr = "/mnt/c/Users/sun/Desktop/190733878666/";
            String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
            path = path.replace('\\', '/'); // 将/换成\
            path = path.replace("file:", ""); //去掉file:
            path = path.replace("classes/", ""); //去掉class\

            //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.load(path + "/lib/libopencv_java410.so");

            Mat src = Imgcodecs.imread(picStr + "62_1.png");
            //读取图像到矩阵中
            if (src.empty()) {
                throw new Exception("no file");
            }

            int xMargin, yMargin;
            int x0 = src.cols() / 4;
            int x1 = (src.cols() / 4) * 3;
            int y0 = src.cols() / 4;
            int y1 = (src.cols() / 4) * 3;
            Mat dst = new Mat();

            //System.out.println(x0 + "," +x1+"," + y0+"," +y1);
            //原始的四个角 的定位点  （左上，左下，右下，右上）
            List<Point> listSrcs = java.util.Arrays.asList(new Point(304, 627), new Point(304, 2915), new Point(1979, 2920), new Point(1937, 602));
            Mat srcPoints = Converters.vector_Point_to_Mat(listSrcs, CvType.CV_32F);

            xMargin = src.cols() / 10;
            yMargin = src.rows() / 10;
            //变换后的 的四个角 的定位点  （左上，左下，右下，右上）
            List<Point> listDsts = java.util.Arrays.asList(new Point(110, 170), new Point(110, 1725), new Point(1200, 1725), new Point(1200, 170));
            Mat dstPoints = Converters.vector_Point_to_Mat(listDsts, CvType.CV_32F);

            Mat perspectiveMmat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
            Imgproc.warpPerspective(src, dst, perspectiveMmat, src.size(), Imgproc.INTER_LINEAR);
            Imgcodecs.imwrite(picStr + "62_11.png", dst);

            xMargin = src.cols() / 8;
            yMargin = src.cols() / 8;
            listDsts.set(0, listSrcs.get(0));
            listDsts.set(1, listSrcs.get(1));
            listDsts.set(2, new Point(x1 - xMargin, y1 - yMargin));
            listDsts.set(3, new Point(x1 - xMargin, y0 - yMargin));
            dstPoints = Converters.vector_Point_to_Mat(listDsts, CvType.CV_32F);

            perspectiveMmat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
            Imgproc.warpPerspective(src, dst, perspectiveMmat, src.size(), Imgproc.INTER_LINEAR);
            Imgcodecs.imwrite(picStr + "62_12.png", dst);

            xMargin = src.cols() / 6;
            yMargin = src.cols() / 6;
            listDsts.set(0, new Point(x0 + xMargin, y0 + yMargin));
            listDsts.set(1, listSrcs.get(1));
            listDsts.set(2, new Point(x1 - xMargin, y1 - yMargin));
            listDsts.set(3, listSrcs.get(3));
            dstPoints = Converters.vector_Point_to_Mat(listDsts, CvType.CV_32F);

            perspectiveMmat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
            Imgproc.warpPerspective(src, dst, perspectiveMmat, src.size(), Imgproc.INTER_LINEAR);

            Imgcodecs.imwrite(picStr + "62_13.png", dst);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("例外：" + e);
        }
    }
}
