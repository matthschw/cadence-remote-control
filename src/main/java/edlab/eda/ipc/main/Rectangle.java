package rbz.eda.generic.core.ipc.main;

public class Rectangle {
	
	private LayoutPoint lowerLeft;
	private LayoutPoint upperRight;
	
	private String layer;
	
	public Rectangle(LayoutPoint lowerLeft,LayoutPoint upperRight,String layer){
		this.lowerLeft=lowerLeft;
		this.upperRight=upperRight;
		this.layer=layer;
	}

	public LayoutPoint getLowerLeft() {
		return lowerLeft;
	}
	
	public LayoutPoint getUpperRight() {
		return upperRight;
	}
	
	public String getLayer() {
		return layer;
	}
	
	public String getSkillCode() {
		return "list(\"rect\" "+this.layer+" "+this.lowerLeft.getSkillCode()+" "+this.upperRight.getSkillCode()+")";
	}
}