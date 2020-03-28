package com.krzem.ds4_key_mapper;



import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class Main{
	public static void main(String[] args){
		new Main(args);
	}



	public ArrayList<Controller> cl;
	public Robot r;
	private List<Key> kl;



	public Main(String[] args){
		try{
			this.cl=Controller.list();
			this.kl=new ArrayList<Key>();
			this.r=new Robot();
			Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(args[0]);
			doc.getDocumentElement().normalize();
			Element root=doc.getDocumentElement();
			for (Element e:this._xml_child(root,"key")){
				List<Long> k=new ArrayList<Long>();
				for (Element c:this._xml_children(e)){
					String v="";
					switch (c.getTagName()){
						case "joystick":
							v+="00"+(c.getAttribute("type").equals("right")?"1":"0")+(c.getAttribute("axis").equals("y")?"1":"0")+this._pad(Integer.toBinaryString(Integer.parseInt(c.getAttribute("threshold"))+128),8)+this._sign(c.getAttribute("threshold-type"))+"00000000000000000000000000";
							break;
						case "button":
							v+="01"+this._b_loc(c.getAttribute("type"))+"00000000000000000000000000000000000";
							break;
						case "trackpad":
							v+="10"+(c.getAttribute("finger").equals("2")?"1":"0")+this._pad(Integer.toBinaryString(Integer.parseInt(c.getAttribute("x-threshold"))),16)+this._pad(Integer.toBinaryString(Integer.parseInt(c.getAttribute("y-threshold"))),16)+this._sign(c.getAttribute("x-threshold-type"))+this._sign(c.getAttribute("y-threshold-type"));// 41
							break;
						case "tigger":
							v+="11"+(c.getAttribute("type").equals("right")?"1":"0")+(c.getAttribute("position").equals("top")?"1":"0")+this._pad(Integer.toBinaryString(Integer.parseInt(c.getAttribute("-threshold"))),8)+this._sign(c.getAttribute("threshold-type"))+"00000000000000000000000000";
							break;
					}
					// System.out.println(v);
					k.add(Long.parseLong(v,2));
				}
				this.kl.add(new Key(this,e.getAttribute("name"),(e.getAttribute("static").length()==0?false:Boolean.parseBoolean(e.getAttribute("static"))),(e.getAttribute("id").length()==0?-1:Integer.parseInt(e.getAttribute("id"))),k));
			}
			while (true){
				for (Key k:this.kl){
					k.check();
				}
				Thread.sleep(1000/500);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	private String _pad(String v,int l){
		while (v.length()<l){
			v="0"+v;
		}
		return v;
	}



	private String _b_loc(String n){
		switch (n){
			case "ps":
				return "0000";
			case "option":
				return "0001";
			case "share":
				return "0010";
			case "up":
				return "0011";
			case "left":
				return "0100";
			case "down":
				return "0101";
			case "right":
				return "0110";
			case "triangle":
				return "0111";
			case "square":
				return "1000";
			case "cross":
				return "1001";
			case "circle":
				return "1010";
			case "left-joystick":
				return "1011";
			case "right-joystick":
				return "1100";
			case "trackpad":
				return "1101";
		}
		return "1111";
	}



	private String _sign(String s){
		switch (s){
			case "l":
				return "000";
			case "e":
				return "001";
			case "m":
				return "010";
			case "le":
				return "011";
			case "me":
				return "100";
			case "lm":
				return "101";
		}
		return "111";
	}



	private ArrayList<Element> _xml_child(Element p,String tn){
		ArrayList<Element> o=new ArrayList<Element>();
		NodeList cl=p.getChildNodes();
		for (int j=0;j<cl.getLength();j++){
			if (cl.item(j).getNodeType()!=Node.ELEMENT_NODE){
				continue;
			}
			Element e=(Element)cl.item(j);
			if (e.getTagName().equals(tn)){
				o.add(e);
			}
		}
		return o;
	}



	private ArrayList<Element> _xml_children(Element p){
		ArrayList<Element> o=new ArrayList<Element>();
		NodeList cl=p.getChildNodes();
		for (int j=0;j<cl.getLength();j++){
			if (cl.item(j).getNodeType()!=Node.ELEMENT_NODE){
				continue;
			}
			Element e=(Element)cl.item(j);
			o.add(e);
		}
		return o;
	}
}