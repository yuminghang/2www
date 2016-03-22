package com.delin.dgclient.model;



//定义了一个圆形类，主要包括圆心坐标和圆的半径
public class Circle{
	public Point center; //圆心坐标
	public double radius; //半径
	
	
	public Circle(){
		center = new Point(0 , 0);
		radius = 0.0;
	}
	
	public Circle(Point center, double radius){
		this.center = center;
		this.radius = radius;
	}
}