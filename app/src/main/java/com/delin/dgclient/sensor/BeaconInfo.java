package com.delin.dgclient.sensor;

public class BeaconInfo{
	public String beaconSN;
	public double beacon_x;
	public double beacon_y;
	public int beacon_z;
	
	
	
	public BeaconInfo(){
		
	}
	
	
	public BeaconInfo(String SN, double x, double y, int z){
		this.beaconSN = SN;
		this.beacon_x = x;
		this.beacon_y = y;
		this.beacon_z = z;
	}
}