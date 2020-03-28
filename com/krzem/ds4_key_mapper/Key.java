package com.krzem.ds4_key_mapper;



import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;



public class Key{
	public Main cls;
	private String k;
	private boolean st;
	private int id;
	private List<Long> data;
	private boolean _d=false;



	public Key(Main cls,String k,boolean st,int id,List<Long> data){
		this.cls=cls;
		this.k=k;
		this.st=st;
		this.id=id;
		this.data=data;
	}



	public void check(){
		int i=0;
		for (Controller c:this.cls.cl){
			if (i!=this.id&&this.id!=-1){
				i++;
				continue;
			}
			i++;
			boolean iv=true;
			double v=1;
			for (Long l:this.data){
				if (this._bits(l,39,41)==0){
					if (this._bits(l,38,39)==0){
						if (this._bits(l,37,38)==0){
							v=(double)c.get("left-joystick-x");
							if (this._cmp(c.get("left-joystick-x")+128,this._bits(l,26,29),this._bits(l,29,37))==false){
								iv=false;
								break;
							}
						}
						else{
							v=(double)c.get("left-joystick-y");
							if (this._cmp(c.get("left-joystick-y")+128,this._bits(l,26,29),this._bits(l,29,37))==false){
								iv=false;
								break;
							}
						}
					}
					else{
						if (this._bits(l,37,38)==0){
							v=(double)c.get("right-joystick-x");
							if (this._cmp(c.get("right-joystick-x")+128,this._bits(l,26,29),this._bits(l,29,37))==false){
								iv=false;
								break;
							}
						}
						else{
							v=(double)c.get("right-joystick-y");
							if (this._cmp(c.get("right-joystick-y")+128,this._bits(l,26,29),this._bits(l,29,37))==false){
								iv=false;
								break;
							}
						}
					}
				}
				else if (this._bits(l,39,41)==1){
					// System.out.println(c.get(this._b_map(this._bits(l,35,39))));
					if (c.get(this._b_map(this._bits(l,35,39)))==0){
						iv=false;
						break;
					}
				}
			}
			if (iv==true){
				this._press(v);
			}
			else{
				this._release(v);
			}
			if (this.id!=-1){
				break;
			}
		}
	}



	private void _press(double v){
		try{
			if (this.st==true&&this._d==true){
				return;
			}
			this._d=true;
			for (String k:this.k.split("\\+")){
				try{
					this._d=true;
					this.cls.r.keyPress((int)KeyEvent.class.getDeclaredField("VK_"+k.toUpperCase()).get(int.class));
					if (this.st==true){
						this.cls.r.keyRelease((int)KeyEvent.class.getDeclaredField("VK_"+k.toUpperCase()).get(int.class));
					}
				}
				catch (Exception e){
					if (k.startsWith("mm")){
						Point p=MouseInfo.getPointerInfo().getLocation();
						if (!k.substring(2).split(",")[2].equals("V")){
							v=Double.parseDouble(k.substring(2).split(",")[2]);
						}
						this.cls.r.mouseMove(p.x+(int)(Double.parseDouble(k.substring(2).split(",")[0])*v),p.y+(int)(Double.parseDouble(k.substring(2).split(",")[1])*v));
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	private void _release(double v){
		try{
			for (String k:this.k.split("\\+")){
				try{
					if (this._d==true){
						this._d=false;
						this.cls.r.keyRelease((int)KeyEvent.class.getDeclaredField("VK_"+k.toUpperCase()).get(int.class));
					}
				}
				catch (Exception e){
					if (k.startsWith("mm")){
						continue;
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	private int _bits(long l,int s,int e){
		return (int)((l>>s)-((l>>e)<<(e-s)));
	}



	private String _b_map(int v){
		switch (v){
			case 0:
				return "ps-button";
			case 1:
				return "option";
			case 2:
				return "share";
			case 3:
				return "up";
			case 4:
				return "left";
			case 5:
				return "down";
			case 6:
				return "right";
			case 7:
				return "triangle";
			case 8:
				return "square";
			case 9:
				return "cross";
			case 10:
				return "circle";
			case 11:
				return "left-click";
			case 12:
				return "right-click";
			case 13:
				return "touch-pad-click";
		}
		return "";
	}



	private boolean _cmp(int a,int s,int b){
		switch (s){
			case 0:
				return a<b;
			case 1:
				return a==b;
			case 2:
				return a>b;
			case 3:
				return a<=b;
			case 4:
				return a>=b;
			case 5:
				return a!=b;
		}
		return false;
	}
}