package com.krzem.ds4_key_mapper;



import java.lang.Exception;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import purejavahidapi.HidDevice;
import purejavahidapi.HidDeviceInfo;
import purejavahidapi.InputReportListener;
import purejavahidapi.PureJavaHidApi;



public class Controller{
	private HidDevice d;
	private HidDeviceInfo di;
	private Map<String,Integer> data;



	private Controller(HidDeviceInfo di){
		try{
			this.di=di;
			this.d=PureJavaHidApi.openDevice(this.di);
			this.data=new HashMap<String,Integer>();
			this._defaults();
			Controller cls=this;
			this.d.setInputReportListener(new InputReportListener(){
				@Override
				public void onInputReport(HidDevice d,byte id,byte[] data,int len){
					cls._input_report(data);
				}
			});
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	public int get(String k){
		return (int)this.data.get(k);
	}



	public static ArrayList<Controller> list(){
		ArrayList<Controller> l=new ArrayList<Controller>();
		for (HidDeviceInfo d:PureJavaHidApi.enumerateDevices()){
			try{
				if (d.getProductString().equals("Wireless Controller")){
					l.add(new Controller(d));
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		return l;
	}



	private void _defaults(){
		this._set("left-joystick-x",0);
		this._set("left-joystick-y",0);
		this._set("right-joystick-x",0);
		this._set("right-joystick-y",0);
		this._set("up",0);
		this._set("down",0);
		this._set("left",0);
		this._set("right",0);
		this._set("traingle",0);
		this._set("rectangle",0);
		this._set("cross",0);
		this._set("square",0);
		this._set("l1",0);
		this._set("l2",0);
		this._set("r1",0);
		this._set("r2",0);
		this._set("l2-trigger",0);
		this._set("r2-trigger",0);
		this._set("share",0);
		this._set("option",0);
		this._set("ps-button",0);
		this._set("touch-pad-click",0);
		this._set("touch-pad-1-finger-id",0);
		this._set("touch-pad-1-finger-down",0);
		this._set("touch-pad-1-finger-x",0);
		this._set("touch-pad-1-finger-y",0);
		this._set("touch-pad-2-finger-id",0);
		this._set("touch-pad-2-finger-down",0);
		this._set("touch-pad-2-finger-x",0);
		this._set("touch-pad-2-finger-y",0);
	}



	private void _input_report(byte[] data){
		this._set("left-joystick-x",(int)(data[0]&0xff)-128);
		this._set("left-joystick-y",(int)(data[1]&0xff)-128);
		this._set("right-joystick-x",(int)(data[2]&0xff)-128);
		this._set("right-joystick-y",(int)(data[3]&0xff)-128);
		int u=0;
		int d=0;
		int l=0;
		int r=0;
		switch (String.format("%d%d%d%d",this._get_bit(data[4],3),this._get_bit(data[4],2),this._get_bit(data[4],1),this._get_bit(data[4],0))){
			case "0000":
				u=1;
				break;
			case "0001":
				u=1;
				r=1;
				break;
			case "0010":
				r=1;
				break;
			case "0011":
				d=1;
				r=1;
				break;
			case "0100":
				d=1;
				break;
			case "0101":
				d=1;
				l=1;
				break;
			case "0110":
				l=1;
				break;
			case "0111":
				u=1;
				l=1;
				break;
		}
		this._set("up",u);
		this._set("down",d);
		this._set("left",l);
		this._set("right",r);
		this._set("rectangle",this._get_bit(data[4],4));
		this._set("cross",this._get_bit(data[4],5));
		this._set("circle",this._get_bit(data[4],6));
		this._set("triangle",this._get_bit(data[4],7));
		this._set("l1",this._get_bit(data[5],0));
		this._set("r1",this._get_bit(data[5],1));
		this._set("l2",this._get_bit(data[5],2));
		this._set("r2",this._get_bit(data[5],3));
		this._set("share",this._get_bit(data[5],4));
		this._set("option",this._get_bit(data[5],5));
		this._set("left-click",this._get_bit(data[5],6));
		this._set("right-click",this._get_bit(data[5],7));
		this._set("ps-button",this._get_bit(data[6],0));
		this._set("touch-pad-click",this._get_bit(data[6],1));
		this._set("l2-trigger",(int)data[7]&0xff);
		this._set("r2-trigger",(int)data[8]&0xff);
		this._set("battery-level",(int)data[11]&0xff);
		this._set("touch-pad-1-finger-id",data[34]-this._get_bit(data[34],7)*128);
		this._set("touch-pad-1-finger-down",1-this._get_bit(data[34],7));
		this._set("touch-pad-1-finger-x",((data[36]&0x0f)<<8)|data[35]);
		this._set("touch-pad-1-finger-y",data[37]<<4|((data[36]&0xf0)>>4));
		this._set("touch-pad-2-finger-id",data[38]-this._get_bit(data[38],7)*128);
		this._set("touch-pad-2-finger-down",1-this._get_bit(data[38],7));
		this._set("touch-pad-2-finger-x",((data[40]&0x0f)<<8)|data[39]);
		this._set("touch-pad-2-finger-y",data[41]<<4|((data[40]&0xf0)>>4));
		// this._set("gyro-x",(((int)(data[13]&0xff)<<8)|(int)data[14]&0xff+(int)Math.pow(2,15))%(int)Math.pow(2,16)-(int)Math.pow(2,15));
		// this._set("gyro-y",(((int)(data[15]&0xff)<<8)|(int)data[16]&0xff+(int)Math.pow(2,15))%(int)Math.pow(2,16)-(int)Math.pow(2,15));
		// this._set("gyro-z",(((int)(data[17]&0xff)<<8)|(int)data[18]&0xff+(int)Math.pow(2,15))%(int)Math.pow(2,16)-(int)Math.pow(2,15));
		// this._set("acceleration-x",(int)(data[13]<<8|data[14]));
		// this._set("acceleration-y",(int)(data[21]<<8|data[22]));
		// this._set("acceleration-z",(int)(data[23]<<8|data[24]));
	}



	private void _set(String k,int v){
		if (this.data.get(k)==null||(int)this.data.get(k)!=v){
			this.data.put(k,v);
		}
	}



	private int _get_bit(byte b,int idx){
		return (int)((b>>idx)&1);
	}
}