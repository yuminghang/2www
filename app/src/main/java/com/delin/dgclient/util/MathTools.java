package com.delin.dgclient.util;

import java.util.ArrayList;

import android.util.Log;

import com.delin.dgclient.model.Circle;
import com.delin.dgclient.model.Point;


//数学工具类，主要用于提供一些基本的数学算法

public class MathTools{
	
	private static String TAG = MathTools.class.getName();
	
    //求取两个圆的交点
	public static ArrayList<Point> getIntersectionOf2Circles(Circle circle1,  Circle circle2 ){
		ArrayList<Point>intersectionList = new ArrayList<Point>();
		double centerDistance =  MathTools.getDistanceOf2Points(circle1.center, circle2.center);
		
		Log.i(TAG, "" + centerDistance);
		
		if(centerDistance  - circle1.radius  -  circle2.radius > 0.1){
			intersectionList.clear();
			return intersectionList;
		}else if(centerDistance  - circle1.radius + circle2.radius  < 0.1  && centerDistance  - circle1.radius + circle2.radius > -0.1){
			
			Point intersectionPoint = new Point();
			
			double sina = (circle2.center.x  - circle1.center.x) / (circle1.radius + circle2.radius);
			double cosa = (circle2.center.y - circle1.center.y) / (circle1.radius + circle2.radius);
			
			intersectionPoint.x = circle1.center.x + circle1.radius * sina;
			intersectionPoint.y = circle1.center.y + circle2.radius * cosa;
			
			intersectionList.add(intersectionPoint);
			
			
			return intersectionList;
		}else{
			//有两个交点
			Point intersectionPoint1 = new Point();
			Point intersectionPoint2 = new Point();
			
			
			//两个圆的交点的连线和两个圆的圆心的连线的交点
			Point intersectionTmp = new Point();
			
			
			
			  //a为两个圆心的连线和两个交点的连线的交点，与circle1的圆心的距离
			double a = (circle1.radius * circle1.radius - circle2.radius * circle2.radius +
					centerDistance * centerDistance) / (2 * centerDistance);
			
			
			
			
			//两个交点的连线的长度的一半
			double h = 0.0;
			if(circle1.radius * circle1.radius - a * a > 0){
				h = Math.sqrt(circle1.radius * circle1.radius - a * a);
			}else{
				return intersectionList;
			}
			
			
			intersectionTmp.x  = circle1.center.x + a / centerDistance * (circle2.center.x -circle1.center.x);
			intersectionTmp.y  = circle1.center.y + a / centerDistance * (circle2.center.y -circle1.center.y);
			
			intersectionPoint1.x = intersectionTmp.x + h * (circle2.center.y - circle1.center.y)  /  centerDistance;
			intersectionPoint1.y = intersectionTmp.y -  h * (circle2.center.x - circle1.center.x)  /  centerDistance;
			
			intersectionPoint2.x = intersectionTmp.x - h * (circle2.center.y - circle1.center.y)  /  centerDistance;
			intersectionPoint2.y = intersectionTmp.y +  h * (circle2.center.x - circle1.center.x)  /  centerDistance;
			
			intersectionList.add(intersectionPoint1);
			intersectionList.add(intersectionPoint2);
			
			return intersectionList;
		}
		
	}
	
    //求取两个点之间的距离	
	public static double getDistanceOf2Points(Point p1, Point p2){
		double distance = 0.0;
		double x_dis = p1.x - p2.x;
		double y_dis = p1.y - p2.y;
		distance = x_dis * x_dis + y_dis * y_dis;
		distance = Math.sqrt(distance);
		return distance;
	}
	
	
	
	
}
