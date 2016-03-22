package com.delin.dgclient.model;

public class Point{
	public double x;
	public double y;
	
	
	
	public Point(){
		
	}
	
	public  Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	boolean equlas(Point point){
		if(this.x == point.x && this.y == point.y){
			return true;
		}
		return false;
	}
}