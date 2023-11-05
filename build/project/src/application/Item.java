package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Item {
    private String name;
    private double price;
    private double locationX;
    private double locationY;
    private double length;
    private double width;
    private double height;
    private ItemContainer parent;
    private Rectangle drawing;
    private Text text;
    
    public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public Rectangle getDrawing() {
		return drawing;
	}

	public void setDrawing(Rectangle drawing) {
		this.drawing = drawing;
	}

	public ItemContainer getParent() {
		return parent;
	}

	public void setParent(ItemContainer parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getLocationX() {
		return locationX;
	}

	public void setLocationX(double locationX) {
		this.locationX = locationX;
	}

	public double getLocationY() {
		return locationY;
	}

	public void setLocationY(double locationY) {
		this.locationY = locationY;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	

    public Item(String name, double price, double locationX, double locationY, double length, double width, double height) {
        this.name = name;
        this.price = price;
        this.locationX = locationX;
        this.locationY = locationY;
        this.length = length;
        this.width = width;
        this.height = height;
        this.parent = null;
    }

    // Getters and setters for the attributes
    // ...

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", locationX=" + locationX +
                ", locationY=" + locationY +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
    
    public String type() {
    	return "Item";
    }
}

