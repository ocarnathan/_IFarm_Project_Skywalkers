package application;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TreeView;

import javafx.geometry.Insets;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.text.Element;


public class iFarmController {

    @FXML
    private TreeView<String> ItemTree;

    @FXML
    private AnchorPane Visualizer;

    @FXML
    private Button btnAddItem;

    @FXML
    private Button btnEditItem;

    @FXML
    private Button btnGoToItem;

    @FXML
    private Button btnScanFarm;
    
    private ItemContainer rootItemContainer = new ItemContainer("Farm", 0, 0, 0, 600, 800, 0);

    private TreeItem<String> rootItem = new TreeItem<>(rootItemContainer.getName());
    
    private ArrayList<Object> itemList = new ArrayList<>();
    
    private Drone drone;
    

    @FXML
    public void onAddItemClick() {
        // Get the selected TreeItem
        TreeItem<String> selectedTreeItem = ItemTree.getSelectionModel().getSelectedItem();

        if (selectedTreeItem == null) {
        	showAlert("Error", "Please select an Item Container.");
        }else {
        	String selectedItemName = selectedTreeItem.getValue();
        	
        	// Check if selected item is ItemContainer
            if (isItemContainer(selectedItemName)==false) {
                // Show an alert that you cannot create an Item inside an Item
                showAlert("Error", "You cannot create an Item inside an Item.");
            } else {
                // Proceed with creating an Item or ItemContainer
            	ItemContainer parent = findItemContainer(selectedItemName);
                Alert itemTypeAlert = new Alert(Alert.AlertType.CONFIRMATION);
                itemTypeAlert.setTitle("Select Item Type");
                itemTypeAlert.setHeaderText("Choose the type of item you want to add.");
                itemTypeAlert.setContentText("Select:");

                ButtonType btnTypeItem = new ButtonType("Item");
                ButtonType btnTypeItemContainer = new ButtonType("ItemContainer");

                itemTypeAlert.getButtonTypes().setAll(btnTypeItem, btnTypeItemContainer);

                Optional<ButtonType> result = itemTypeAlert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == btnTypeItem) {
                        // Add an Item
                        createItem(parent);
                    } else if (result.get() == btnTypeItemContainer) {
                        // Add an ItemContainer
                        createItemContainer(parent);
                    }
                }
            }
        }
    }

    private void createItem(ItemContainer parent) {
    	Dialog<Item> itemDialog = new Dialog<>();
    	itemDialog.setTitle("Add Item");
    	itemDialog.setHeaderText("Enter Item Information");

    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));

    	Label nameLabel = new Label("Name");
    	TextField nameField = new TextField();
    	nameField.setPromptText("Name");
    	Label priceLabel = new Label("Price");
    	TextField priceField = new TextField();
    	priceField.setPromptText("Price");

    	Label locationXLabel = new Label("Location-X");
    	locationXLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField locationXField = new TextField();
    	Label locationXInfo = new Label("range: <" + parent.getLocationX() + ", " + (parent.getLocationX() + parent.getWidth()) + ">");
    	locationXInfo.setPrefWidth(250); // Set a fixed width for the label
    	double allowedMinX = parent.getLocationX();
	    double allowedMaxX = parent.getWidth() + parent.getLocationX();
	    locationXInfo.setText("Allowed range: <" + allowedMinX + ", " + allowedMaxX + ">");

    	Label locationYLabel = new Label("Location-Y");
    	locationYLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField locationYField = new TextField();
    	Label locationYInfo = new Label("range: <" + parent.getLocationY() + ", " + (parent.getLocationY() + parent.getLength()) + ">");
    	locationYInfo.setPrefWidth(250); // Set a fixed width for the label
    	double allowedMinY = parent.getLocationY();
        double allowedMaxY = parent.getLength() + parent.getLocationY();
        locationYInfo.setText("Allowed range: <" + allowedMinY + ", " + allowedMaxY + ">");

    	Label widthLabel = new Label("Width");
    	widthLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField widthField = new TextField();
    	Label widthInfo = new Label("Allowed range: <0, " + parent.getWidth() + ">");
    	widthInfo.setPrefWidth(250); // Set a fixed width for the label
    	locationXField.textProperty().addListener((observable, oldValue, newValue) -> {
    		double newWidth = (!locationXField.getText().isEmpty())? Double.parseDouble(locationXField.getText()):0;
        	double allowedMax = parent.getWidth() + parent.getLocationX()- newWidth;
        	widthInfo.setText("Allowed range: <0, " + allowedMax + ">");
    	});
    	

    	Label lengthLabel = new Label("Length");
    	lengthLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField lengthField = new TextField();
    	Label lengthInfo = new Label("range: <0, " + parent.getLength() + ">");
    	lengthInfo.setPrefWidth(250); // Set a fixed width for the label
    	locationYField.textProperty().addListener((observable, oldValue, newValue) -> {
    		double newLength = (!locationYField.getText().isEmpty())? Double.parseDouble(locationYField.getText()):0;
        	double allowedMax = parent.getLength() + parent.getLocationY()- newLength;
        	lengthInfo.setText("Allowed range: <0, " + allowedMax + ">");
    	});

    	Label heightLabel = new Label("Height");
    	heightLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField heightField = new TextField();

    	grid.add(nameLabel, 0, 0);
    	grid.add(nameField, 1, 0);
    	grid.add(priceLabel, 0, 1);
    	grid.add(priceField, 1, 1);

    	grid.add(locationXLabel, 0, 2);
    	grid.add(locationXField, 1, 2);
    	grid.add(locationXInfo, 2, 2);

    	grid.add(locationYLabel, 0, 3);
    	grid.add(locationYField, 1, 3);
    	grid.add(locationYInfo, 2, 3);

    	grid.add(widthLabel, 0, 4);
    	grid.add(widthField, 1, 4);
    	grid.add(widthInfo, 2, 4);

    	grid.add(lengthLabel, 0, 5);
    	grid.add(lengthField, 1, 5);
    	grid.add(lengthInfo, 2, 5);

    	grid.add(heightLabel, 0, 6);
    	grid.add(heightField, 1, 6);

    	itemDialog.getDialogPane().setContent(grid);
    	itemDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    	Stage stage = (Stage) itemDialog.getDialogPane().getScene().getWindow();
    	stage.setAlwaysOnTop(true);




        itemDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                // Parse user input and create an Item object
                try {
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    double locationX = Double.parseDouble(locationXField.getText());
                    double locationY = Double.parseDouble(locationYField.getText());
                    double length = Double.parseDouble(lengthField.getText());
                    double width = Double.parseDouble(widthField.getText());
                    double height = Double.parseDouble(heightField.getText());
                    Item newItem = new Item(name, price, locationX, locationY, length, width, height);
                    
                    // Check if new item is within bounds of parent item
                    if (!isWithinBounds(parent, newItem)) {
                        showAlert("Error", "The new item does not fit within the parent's bounds.");
                        return null; // Exit the method without adding the item
                    }
                    
                    // Check if new item is overlapping with other items
                    if (isOverlapping(newItem, parent)) {
                        showAlert("Error", "Items are overlapping.");
                        return null; // Exit the method without adding the item
                    }
                    
                    itemList.add(newItem);		// Add new item to general itemList
                    parent.addItem(newItem);	// Add new item in to parent's ArrayList
                    newItem.setParent(parent);  // Add parent as parent to new item    
                    drawItem(newItem);			// Draw new item
                    
                    TreeItem<String> newItemTreeItem = new TreeItem<>(newItem.getName());
                    
                    // Add the Item as a child to the selected TreeItem
                    TreeItem<String> selectedTreeItem = ItemTree.getSelectionModel().getSelectedItem();
                    if (selectedTreeItem != null) {
                        selectedTreeItem.getChildren().add(newItemTreeItem);
                    }
                    return newItem;
                   // return new Item(name, price, locationX, locationY, length, width, height);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input. Please enter valid numbers for attributes.");
                    return null;
                }
            }
            return null;
        });
        itemDialog.showAndWait();
    }

    private void createItemContainer(ItemContainer parent) {
        Dialog<ItemContainer> itemContainerDialog = new Dialog<>();
        itemContainerDialog.setTitle("Add ItemContainer");
        itemContainerDialog.setHeaderText("Enter ItemContainer Information");

        GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));

    	Label nameLabel = new Label("Name");
    	TextField nameField = new TextField();
    	nameField.setPromptText("Name");
    	Label priceLabel = new Label("Price");
    	TextField priceField = new TextField();
    	priceField.setPromptText("Price");

    	Label locationXLabel = new Label("Location-X");
    	locationXLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField locationXField = new TextField();
    	Label locationXInfo = new Label("range: <" + parent.getLocationX() + ", " + (parent.getLocationX() + parent.getWidth()) + ">");
    	locationXInfo.setPrefWidth(250); // Set a fixed width for the label
    	double allowedMinX = parent.getLocationX();
	    double allowedMaxX = parent.getWidth() + parent.getLocationX();
	    locationXInfo.setText("Allowed range: <" + allowedMinX + ", " + allowedMaxX + ">");

    	Label locationYLabel = new Label("Location-Y");
    	locationYLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField locationYField = new TextField();
    	Label locationYInfo = new Label("range: <" + parent.getLocationY() + ", " + (parent.getLocationY() + parent.getLength()) + ">");
    	locationYInfo.setPrefWidth(250); // Set a fixed width for the label
    	double allowedMinY = parent.getLocationY();
        double allowedMaxY = parent.getLength() + parent.getLocationY();
        locationYInfo.setText("Allowed range: <" + allowedMinY + ", " + allowedMaxY + ">");

    	Label widthLabel = new Label("Width");
    	widthLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField widthField = new TextField();
    	Label widthInfo = new Label("Allowed range: <0, " + parent.getWidth() + ">");
    	widthInfo.setPrefWidth(250); // Set a fixed width for the label
    	locationXField.textProperty().addListener((observable, oldValue, newValue) -> {
    		double newWidth = (!locationXField.getText().isEmpty())? Double.parseDouble(locationXField.getText()):0;
        	double allowedMax = parent.getWidth() + parent.getLocationX()- newWidth;
        	widthInfo.setText("Allowed range: <0, " + allowedMax + ">");
    	});
    	

    	Label lengthLabel = new Label("Length");
    	lengthLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField lengthField = new TextField();
    	Label lengthInfo = new Label("range: <0, " + parent.getLength() + ">");
    	lengthInfo.setPrefWidth(250); // Set a fixed width for the label
    	locationYField.textProperty().addListener((observable, oldValue, newValue) -> {
    		double newLength = (!locationYField.getText().isEmpty())? Double.parseDouble(locationYField.getText()):0;
        	double allowedMax = parent.getLength() + parent.getLocationY()- newLength;
        	lengthInfo.setText("Allowed range: <0, " + allowedMax + ">");
    	});

    	Label heightLabel = new Label("Height");
    	heightLabel.setPrefWidth(100); // Set a fixed width for the label
    	TextField heightField = new TextField();

    	grid.add(nameLabel, 0, 0);
    	grid.add(nameField, 1, 0);
    	grid.add(priceLabel, 0, 1);
    	grid.add(priceField, 1, 1);

    	grid.add(locationXLabel, 0, 2);
    	grid.add(locationXField, 1, 2);
    	grid.add(locationXInfo, 2, 2);

    	grid.add(locationYLabel, 0, 3);
    	grid.add(locationYField, 1, 3);
    	grid.add(locationYInfo, 2, 3);

    	grid.add(widthLabel, 0, 4);
    	grid.add(widthField, 1, 4);
    	grid.add(widthInfo, 2, 4);

    	grid.add(lengthLabel, 0, 5);
    	grid.add(lengthField, 1, 5);
    	grid.add(lengthInfo, 2, 5);

    	grid.add(heightLabel, 0, 6);
    	grid.add(heightField, 1, 6);

        itemContainerDialog.getDialogPane().setContent(grid);

        itemContainerDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Stage stage = (Stage) itemContainerDialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        itemContainerDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                // Parse user input and create an ItemContainer object
                try {
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    double locationX = Double.parseDouble(locationXField.getText());
                    double locationY = Double.parseDouble(locationYField.getText());
                    double length = Double.parseDouble(lengthField.getText());
                    double width = Double.parseDouble(widthField.getText());
                    double height = Double.parseDouble(heightField.getText());
                    
                    ItemContainer newItemContainer = new ItemContainer(name, price, locationX, locationY, length, width, height);
                    
                    // Check if new item is within bounds of parent item
                    if (!isWithinBounds(parent, newItemContainer)) {
                        showAlert("Error", "The new item does not fit within the parent's bounds.");
                        return null; // Exit the method without adding the item
                    }
                    // Check if new item overlaps with other items
                    if (isOverlapping(newItemContainer, parent)) {
                        showAlert("Error", "Items are overlapping.");
                        return null; // Exit the method without adding the item
                    }
                    itemList.add(newItemContainer);				// Add new item to general itemList
                    parent.addItemContainer(newItemContainer);	// Add new item to parent's ArrayList
                    newItemContainer.setParent(parent);			// Add parent as parent to new item
                    drawItem(newItemContainer);					// Draw new item
                    
                    TreeItem<String> newItemContainerTreeItem = new TreeItem<>(newItemContainer.getName());

                    // Add the ItemContainer as a child to the selected TreeItem
                    TreeItem<String> selectedTreeItem = ItemTree.getSelectionModel().getSelectedItem();
                    if (selectedTreeItem != null) {
                        selectedTreeItem.getChildren().add(newItemContainerTreeItem);
                    }
                    
                    return newItemContainer;
                    //return new ItemContainer(name, price, locationX, locationY, length, width, height);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input. Please enter valid numbers for attributes.");
                    return null;
                }
            }
            return null;
        });

        itemContainerDialog.showAndWait();
    }
    
    public void onEditItemClick() {
        TreeItem<String> selectedTreeItem = ItemTree.getSelectionModel().getSelectedItem();
        final Item[] selectedItem = { null }; // Define a final array to hold the item

        if (selectedTreeItem != null) {
            String selectedItemName = selectedTreeItem.getValue();

            if (isItemContainer(selectedItemName)) {
                // Get the corresponding ItemContainer object and cast it to an Item
                selectedItem[0] = (Item) findItemContainer(selectedItemName);
            } else {
                // Get the corresponding Item object
                selectedItem[0] = findItem(selectedItemName);
            }
        }

        // Create a dialog for editing the attributes
        Dialog<Void> editDialog = new Dialog<>();
        editDialog.setTitle("Edit Item");
        editDialog.setHeaderText("Edit the Item attributes");

        // Create text fields for editing attributes
        TextField nameField = new TextField(selectedItem[0].getName());
        TextField priceField = new TextField(String.valueOf(selectedItem[0].getPrice()));
        TextField locationXField = new TextField(String.valueOf(selectedItem[0].getLocationX()));
        TextField locationYField = new TextField(String.valueOf(selectedItem[0].getLocationY()));
        TextField lengthField = new TextField(String.valueOf(selectedItem[0].getLength()));
        TextField widthField = new TextField(String.valueOf(selectedItem[0].getWidth()));
        TextField heightField = new TextField(String.valueOf(selectedItem[0].getHeight()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Location-X:"), 0, 2);
        grid.add(locationXField, 1, 2);
        grid.add(new Label("Location-Y:"), 0, 3);
        grid.add(locationYField, 1, 3);
        grid.add(new Label("Length:"), 0, 4);
        grid.add(lengthField, 1, 4);
        grid.add(new Label("Width:"), 0, 5);
        grid.add(widthField, 1, 5);
        grid.add(new Label("Height:"), 0, 6);
        grid.add(heightField, 1, 6);

        editDialog.getDialogPane().setContent(grid);

        ButtonType saveChangesButton = new ButtonType("Save Changes", ButtonData.CANCEL_CLOSE);
        ButtonType deleteButton = new ButtonType("Delete", ButtonData.CANCEL_CLOSE);
        editDialog.getDialogPane().getButtonTypes().setAll(saveChangesButton, deleteButton);

        Stage stage = (Stage) editDialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        editDialog.setResultConverter(dialogButton -> {
            // Handle the Save Changes button action
            if (dialogButton == saveChangesButton) {
                try {
                    // Create a dummy object with the updated values. 
                    Item dummyItem = new Item(
                        nameField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Double.parseDouble(locationXField.getText()),
                        Double.parseDouble(locationYField.getText()),
                        Double.parseDouble(lengthField.getText()),
                        Double.parseDouble(widthField.getText()),
                        Double.parseDouble(heightField.getText())
                    );
                    
                    // Check if the dummy item overlaps with other items
                    if (isOverlapping(dummyItem, selectedItem[0].getParent())) {
                        showAlert("Error", "Items are overlapping.");
                        return null;
                    }

                    // Check if the dummy item is within its parent's bounds
                    if (isWithinBounds(selectedItem[0].getParent(), dummyItem)) {
                        double deltaX = dummyItem.getLocationX() - selectedItem[0].getLocationX();
                        double deltaY = dummyItem.getLocationY() - selectedItem[0].getLocationY();

                        moveItems(selectedItem[0], deltaX, deltaY);

                        // Update the real Item object with the changed values
                        selectedItem[0].setName(dummyItem.getName());
                        selectedItem[0].setPrice(dummyItem.getPrice());
                        selectedItem[0].setLocationX(dummyItem.getLocationX());
                        selectedItem[0].setLocationY(dummyItem.getLocationY());
                        selectedItem[0].setLength(dummyItem.getLength());
                        selectedItem[0].setWidth(dummyItem.getWidth());
                        selectedItem[0].setHeight(dummyItem.getHeight());

                        selectedTreeItem.setValue(selectedItem[0].getName());

                        // Redraw the edited Item
                        drawItem(selectedItem[0]);
                    } else {
                        showAlert("Error", "The updated item is not within its parent's bounds.");
                    }
                } catch (Exception e) {
                    showAlert("Error", "An error occurred while updating the item: " + e.getMessage());
                }
            }

            return null; // Don't return anything for the Save Changes button
        });

        // Add a separate handler for the Delete button
        Button deleteButtonNode = (Button) editDialog.getDialogPane().lookupButton(deleteButton);
        deleteButtonNode.setOnAction(event -> {
            // Handle the Delete button action
            deleteItem(selectedItem[0]);
            selectedItem[0].getParent().removeItem(selectedItem[0]);

            TreeItem<String> parentItem = selectedTreeItem.getParent();
            parentItem.getChildren().remove(selectedTreeItem);
        });

        editDialog.showAndWait();
    }
    
    public void onScanFarmClick() {
        // Start the drone animation when "Scan Farm" button is pressed
    	drone.startAnimation();
    }
    
    public void onGoToItemClick() {
        // Get the selected TreeItem
        TreeItem<String> selectedTreeItem = ItemTree.getSelectionModel().getSelectedItem();

        if (selectedTreeItem == null) {
            showAlert("Error", "Please select an Item or Item Container.");
            return;
        }

        String selectedItemName = selectedTreeItem.getValue();

        if (isItemContainer(selectedItemName)) {
            ItemContainer target = findItemContainer(selectedItemName);
            target.setTargetX();
            target.setTargetY();
            drone.goToItem(target.getTargetX(), target.getTargetY());
            return;
        } else {
            Item target = findItem(selectedItemName);
            target.setTargetX();
            target.setTargetY();
            drone.goToItem(target.getTargetX(), target.getTargetY());
            return;
        }
    }

    // Checks if new item will be within bounds of the parent item
    private boolean isWithinBounds(ItemContainer parent, Item newItem) {
        double parentX = parent.getLocationX();
        double parentY = parent.getLocationY();
        double parentWidth = parent.getWidth();
        double parentLength = parent.getLength();
        
        double newItemX = newItem.getLocationX();
        double newItemY = newItem.getLocationY();
        double newItemWidth = newItem.getWidth();
        double newItemLength = newItem.getLength();

        // Check if the item's position and dimensions are within the parent's bounds
        return (newItemX >= parentX && newItemY >= parentY
                && newItemX + newItemWidth <= parentX + parentWidth
                && newItemY + newItemLength <= parentY + parentLength
                && newItemWidth <= parentWidth && newItemLength <= parentLength);
    }
    
    // Checks if new item overlaps with other items or item containers
    private boolean isOverlapping(Item newItem, ItemContainer parent) {
    	//getting range of new item
        double newXstart = newItem.getLocationX();
        double newXend = newXstart + newItem.getWidth();
        double newYstart = newItem.getLocationY();
        double newYend = newYstart + newItem.getLength();

        for (Item item : parent.getItemList()) {
        	//getting range of parent item
            double parentXstart = item.getLocationX();
            double parentXend = parentXstart + item.getWidth();
            double parentYstart = item.getLocationY();
            double parentYend = parentYstart + item.getLength();

            if (((newXstart < parentXend && newYstart < parentYend && newXstart > parentXstart && newYstart > parentYstart) ||
                (newXstart < parentXend && newYend < parentYend && newXstart > parentXstart && newYend > parentYstart) ||
                (newXend < parentXend && newYstart < parentYend && newXend > parentXstart && newYstart > parentYstart) ||
                (newXend < parentXend && newYend < parentYend && newXend > parentXstart && newYend > parentYstart))&&(!newItem.getName().equals(item.getName()))) {
                return true; // Overlapping with at least one item, ignore the same name item as original item is still present and may overlap with it's updated position
            }
        }

        return false; // Not overlapping with any items
    }

    // Moves items inside of the container according to the displacement of that container
    private void moveItems(Item item, double deltaX, double deltaY) {
        if (item.type().equals("Container")) {
            ItemContainer container = (ItemContainer) item;
            for (Item childItem : container.getItemList()) {
                // Update child item positions
                childItem.setLocationX(childItem.getLocationX() + deltaX);
                childItem.setLocationY(childItem.getLocationY() + deltaY);

                // Redraw the edited child Item
                drawItem(childItem);

                // Recursively call the method for nested containers
                moveItems(childItem, deltaX, deltaY);
            }
        }
    }
    
    // Deletes selected item or item container and any children objects that are inside of it
    private void deleteItem(Item item) {
        if (item.type().equals("Container")) {
            // If the item is an ItemContainer, run through its children and recursively call deleteItem
            ItemContainer container = (ItemContainer) item;
            for (Item childItem : container.getItemList()) {
                deleteItem(childItem);
            }
        }
        
        // Delete the drawing associated with the item
        deleteDrawing(item);
        
        // Should eliminate a phantom item at the non deleted item container
        //item.getParent().removeItem(item);
        
        // Remove the item from itemList
        itemList.remove(item);
        return;
    }

    // Shows an alert pop-up
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.showAndWait();
    }
    
    // Checks if the selected tree item is item container
    private boolean isItemContainer(String name) {
        for (Object item : itemList) {
            if (((Item) item).getName().equals(name) && ((Item) item).type().equals("Container")) {
                return true; // It's an ItemContainer
            }
        }
        return false; // It's an Item by default
    }
    
    // Returns item container that corresponds to the selected tree item
    private ItemContainer findItemContainer(String name) {
        for (Object item : itemList) {
            if (((ItemContainer) item).getName().equals(name) && ((ItemContainer) item).type().equals("Container")) {
                return (ItemContainer) item; 
            }
        }
		return null;
    }
    
    // Returns item that corresponds to the selected tree item
    private Item findItem(String name) {
        for (Object item : itemList) {
            if (((Item) item).getName().equals(name) && ((Item) item).type().equals("Item")) {
                return (Item) item; 
            }
        }
		return null;
    }
    
    // Draws an item or item container in visualizer. Can also be used for updating the drawing
    private void drawItem(Item item) {
    	if (item.getDrawing()!=null && item.getText()!=null) {
    		deleteDrawing(item);
    	} // Deletes existing drawing and text of the item 
    	

        // Calculate the coordinates for the item's rectangle on the Visualizer
        double startX = item.getLocationX();;
        double startY = item.getLocationY();
        double endX = item.getWidth();
        double endY = item.getLength();

        // Create a rectangle to represent the item and set its properties
        Rectangle itemRectangle = new Rectangle(startX, startY, endX, endY);
        itemRectangle.setFill(Color.TRANSPARENT);
        itemRectangle.setStroke(Color.BLACK);
        itemRectangle.setStrokeWidth(1);
        Text itemNameText = new Text(startX + 5, startY + 15, item.getName());

        // Add the item's rectangle to the Visualizer AnchorPane
        Visualizer.getChildren().add(itemRectangle);
        Visualizer.getChildren().add(itemNameText);
        item.setDrawing(itemRectangle);
        item.setText(itemNameText);
        
    }
    
    // Deletes drawing and text associated with given item from visualizer, then nulls out those attributes
    private void deleteDrawing(Item item) {
    	Visualizer.getChildren().remove(item.getDrawing());
    	Visualizer.getChildren().remove(item.getText());
    	item.setDrawing(null);
    	item.setText(null);
    	
    }
    
    public void initialize() {
    	itemList.add(rootItemContainer);
    	
        ItemTree.setRoot(rootItem);

   
        rootItem.setExpanded(true);
        drawItem(rootItemContainer);
       
        drone = Drone.getInstance(Visualizer);
        
        
    }
}

