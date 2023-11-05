package application;

import java.util.ArrayList;
import java.util.List;

public class ItemContainer extends Item {
    private List<Item> items;
    private double availableArea;

    public ItemContainer(String name, double price, double locationX, double locationY, double length, double width, double height) {
        super(name, price, locationX, locationY, length, width, height);
        items = new ArrayList<>();
        availableArea = length * width;
    }

    public void addItem(Item item) {
        if (availableArea >= item.getLength() * item.getWidth()) {
            items.add(item);
            availableArea -= item.getLength() * item.getWidth();
        } else {
            System.out.println("Error: Not enough available area to add the item.");
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
        availableArea += item.getLength() * item.getWidth();
    }

    // Method to add an ItemContainer to this ItemContainer
    public void addItemContainer(ItemContainer itemContainer) {
        if (availableArea >= itemContainer.getLength() * itemContainer.getWidth()) {
            items.add(itemContainer);
            availableArea -= itemContainer.getLength() * itemContainer.getWidth();
        } else {
            System.out.println("Error: Not enough available area to add the ItemContainer.");
        }
    }

    // Getters and setters for the attributes
    // ...

    @Override
    public String toString() {
        return "ItemContainer{" +
                "items=" + items +
                ", availableArea=" + availableArea +
                "} " + super.toString();
    }
    
    public String type() {
    	return "Container";
    }

	public List<Item> getItemList() {
		// TODO Auto-generated method stub
		return items;
	}
}

