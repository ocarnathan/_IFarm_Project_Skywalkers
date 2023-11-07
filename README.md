# Group 4: Design and Implementation 1
Obie Carnathan

De'Nyka Brown

Alex Vasiliev

Cory Davis

Github:  https://github.com/ocarnathan/_IFarm_Project_Skywalkers

# Compilation Instruction
IDE instruction:
```bash
Unzip and insert the project file (Teamproject) into your IDE.
Press run.
```

.jar instruction:
```bash
Open terminal and navigate to the directory containing iFarm.jar file.
Then run the following command:
java -jar --module-path "<Path to javafx-sdk\lib>" --add-modules javafx.controls,javafx.fxml iFarm.jar

example:
java -jar --module-path "C:\Users\adv31\OneDrive\Documents\CS420\javafx-sdk-20.0.2\lib" --add-modules javafx.controls,javafx.fxml iFarm.jar
```
# Overview
### Purpose
This is a GUI that simulates an app that helps farmers manage their farm and use automated commands on the drone to help them with their tasks.

### Appearance
Main page includes: Item Tree of existing items,  "Add Item" button, "Edit" button, "Go To Item" button, "Scan Farm" button, and a Visualizer that represents a map of a farm with the existing items.

### Functionality
User must select an item from the Item Tree (dropdown menu) before pressing "Add Item", "Edit Item", or "Go To Item" buttons. User will be prompted to do so if any of those buttons are pressed with no selected item. 

When "Add Item" button is pressed, user is prompted to chose "Item" or "Item Container" to create inside of selected Item Container. Once it is picked, user is then prompted to enter required attributes and then pressing "Ok" will draw that item on the Visualizer and add that item inside the drop down Item Tree. Items and Item Containers may only be added inside another Item Container.

When "Edit ITem" button is pressed, user is shown the current attributes of the selected item and is prompted to change any of them. Items may not be moved outside of the Item Container that they are located in. Pressing "Save Changes" will apply those changes. Pressing "Delete" will delete the selected Item and any Items that were contained inside of it. 

When "Scan Farm" button is pressed, the drone is moved across the Visualizer until it has moved over the entire area. It then returns to its original location.

When "Go To Item" button is pressed, the drone turns towards the selected item and flies to the center of it. It then turns around and returns to its original location.




# Classes
## iFarm
Main Class that loads the GUI

## Item
### Attributes
String: name (Editable by User)

double: price, locationX, locationY, length, width, height (Editable by User)

double: tagetX, targetY (Coordinates of the center)

ItemContainer: parent (ItemContainer object in which this resides)

Rectangle: drawing (Rectangle object drawn on visualizer)

Text: text (Text object that is drawn on visualizer)

### Methods
Standard getters/setters for all attributes.

type(): returns String "Item". 

## ItemContainer extends Item
### Attributes
All the inherited attributes

List\<Item\>: items (this list holds all the Item and ItemContainer objects that are produced inside of this object)

double: availableArea (calculates the total area of this. Subtracts/adds areas of objects added/deleted from it. Not really utilizes)

### Methods
Standard getters/setters for all attributes

type(): returns String "Container"

addItemContainer():  <br/>
 &emsp; &emsp; &emsp; &emsp;Params: ItemContainer<br/>
 &emsp; &emsp; &emsp; &emsp;Return: Void<br/><br/>
 &emsp; &emsp; &emsp; &emsp;adds ItemContainer to this.items. Subtracts added object's area form this.availableArea

addItem(): <br/>
&emsp; &emsp; &emsp; &emsp;Params: Item<br/>
&emsp; &emsp; &emsp; &emsp;Return: Void<br/><br/>
&emsp; &emsp; &emsp; &emsp;adds Item to the List. Subtracts added object's area from this.availableArea

removeItem(): <br/>
&emsp; &emsp; &emsp; &emsp;Params: ItemContainer<br/>
&emsp; &emsp; &emsp; &emsp;Return: Void<br/><br/>
&emsp; &emsp; &emsp; &emsp;removes Item or ItemContainer object from this.items. Adds the area of removed object to this.availableArea

## Drone (Signleton)
### Attributes
Drone: instance

ImageView; droneImage

double: sceneWidth, sceneHeight, droneWidth, droneHight

Animation: droneAnimation (unused)

### Methods
Drone(): <br/>
&emsp; &emsp; &emsp; &emsp;Parmas: AnchorPane (Visualizer portion of GUI)<br/>
&emsp; &emsp; &emsp; &emsp;Return: is a constructor<br/><br/>
&emsp; &emsp; &emsp; &emsp;Gets "drone.png" image and creates ImageView object using it. Sets all the double attributes accordingly. <br/>
&emsp; &emsp; &emsp; &emsp;Adds created ImageView object into Anchorpane

getInstance: <br/>
&emsp; &emsp; &emsp; &emsp;Params: AnchorPane (Visualizer portion of GUI)<br/>
&emsp; &emsp; &emsp; &emsp;Return: Drone<br/><br/>
&emsp; &emsp; &emsp; &emsp;if this.instance is null it will create an intance of the Drone object. <br/>
&emsp; &emsp; &emsp; &emsp;If not null, it will return the existing instance of Drone object.

startAnimation(): <br/>
&emsp; &emsp; &emsp; &emsp;Creates SequentialTransition object and adds Timeline objects to it that move the drone across the Visualizer in a way that covers <br/>
&emsp; &emsp; &emsp; &emsp;the entire area. Then it plays the animation and upon finishing calls returnHome() method. <br/><br/>
&emsp; &emsp; &emsp; &emsp;*ChatGPT: After creating one sequence of drone movement, ChatGPT was promted to make a loop of that movement. It created the <br/>
&emsp; &emsp; &emsp; &emsp;forloop which added the timelines to SequentialTransition object using .getChildren().addAll() method.

returnHome():<br/>
&emsp; &emsp; &emsp; &emsp;Creates SequentialTransiton object that rotates the drone towards its home position, then moves it to the home position.<br/>
&emsp; &emsp; &emsp; &emsp;Upon finishing, it resets the drone's rotation to 0.<br/><br/>
&emsp; &emsp; &emsp; &emsp;*ChatGPT: It was prompted to calculate the angle to which the drone should be rotated based on its current point and return point.

goToItem():<br/>
&emsp; &emsp; &emsp; &emsp;Params: double x-position, double y-position<br/><br/>
&emsp; &emsp; &emsp; &emsp;Creates SequentialTransiton object that rotates the drone towards the target point, then moves it to it. <br/><br/>
&emsp; &emsp; &emsp; &emsp;Upon finishing, it runs returnHome().<br/>
&emsp; &emsp; &emsp; &emsp;*ChatGPT: GPT generated code from returnHome() was modified to calculate the rotation angle.

moveY():<br/>
&emsp; &emsp; &emsp; &emsp;Param: double<br/>
&emsp; &emsp; &emsp; &emsp;Return: Timeline<br/><br/>
&emsp; &emsp; &emsp; &emsp;Creates Timeline object that moves droneImage along Y axis until entered double value.<br/>
&emsp; &emsp; &emsp; &emsp;Animation is performed in 0.5seconds.

moveX():<br/>
&emsp; &emsp; &emsp; &emsp;Param: double<br/>
&emsp; &emsp; &emsp; &emsp;Return: Timeline<br/><br/>
&emsp; &emsp; &emsp; &emsp;Creates Timeline object that moves droneImage towards left along X axis over the distance entered.<br/>
&emsp; &emsp; &emsp; &emsp;Animation is performed in 0.2seconds.

rotate():<br/>
&emsp; &emsp; &emsp; &emsp;Param: double<br/>
&emsp; &emsp; &emsp; &emsp;Return: Timeline<br/><br/>
&emsp; &emsp; &emsp; &emsp;Creates Timeline object that rotates the drone to the entered double value. <br/>
&emsp; &emsp; &emsp; &emsp;Animation is performed in 0.2seconds. 

## iFarmController
Controller class that assigns methods to the buttons setup in iFarmScene.fxml.

Contains helper methods that help it achieve the functionalites.

Line-by-line comments are present inside iFarmController.java file.

### Methods
initialize():<br/>
&emsp; &emsp; &emsp; &emsp;This method runs immediately as the program starts. It adds the root ItemContainer <br/>
&emsp; &emsp; &emsp; &emsp;(Farm, size 800x600) inside general itemList, sets it's name in Item Tree, draws the Farm, and gets an instance of a Drone class.

isWithinBounds():<br/>
&emsp; &emsp; &emsp; &emsp;Params: ItemContainer(parent), Item(newItem)<br/>
&emsp; &emsp; &emsp; &emsp;Return: boolean<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method checks bounds of newItem against parent. If newItem is within parent, it returns True. 

isOverlapping():<br/>
&emsp; &emsp; &emsp; &emsp;Params: Item (newItem), ItemContainer (parent)<br/>
&emsp; &emsp; &emsp; &emsp;Return: boolean<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method checks if newItem's bounds overlap with any objects inside parent.getItemList(). <br/>
&emsp; &emsp; &emsp; &emsp;Overlap is ignored if the newItem is checked against an object with the same name, to avoid phantom overlap <br/>
&emsp; &emsp; &emsp; &emsp;when moving an object in short distance. Returns True if the objects overlap.<br/><br/>
&emsp; &emsp; &emsp; &emsp;*ChatGPT: Original conditional code for overlapping was very conveluted and did not work properly. <br/>
&emsp; &emsp; &emsp; &emsp;GPT was prompted to write a method to find overlap between two rectangles, and produced code was modified to fit the existing <br/>
&emsp; &emsp; &emsp; &emsp;one.

moveItems():<br/>
&emsp; &emsp; &emsp; &emsp;Params: Item (parent), double X, double Y<br/>
&emsp; &emsp; &emsp; &emsp;Return: void<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method moves all Item and ItemContainer objects inside of parent.getItemList() by changing subtracting X and Y <br/>
&emsp; &emsp; &emsp; &emsp;from their attributes and redrawing them. This method uses recursion to accomodate for any objects that may be inside moved <br/>
&emsp; &emsp; &emsp; &emsp;ItemContainer.

deleteItem():<br/>
&emsp; &emsp; &emsp; &emsp;Param: Item (parent)<br/>
&emsp; &emsp; &emsp; &emsp;Return: void<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method runs deleteDrawing(parent) method and then removes that object from the general itemList. <br/>
&emsp; &emsp; &emsp; &emsp;There is a forloop that runs for any object that is an ItemContainer and runs this method recursively. <br/>
&emsp; &emsp; &emsp; &emsp;This allows the deletion of all objects inside of the parent.

showAlert():<br/>
&emsp; &emsp; &emsp; &emsp;Parmas: String title, String content<br/>
&emsp; &emsp; &emsp; &emsp;Return: void<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method creates an Alert object (popup) who's title and content represent the inputted strings.<br/><br/>
&emsp; &emsp; &emsp; &emsp;*ChatGPT: This method was created by GPT automatically during a prompt that asked it to have a Alert window.


showAlert2():<br/>
&emsp; &emsp; &emsp; &emsp;Parmas: String title, String content<br/>
&emsp; &emsp; &emsp; &emsp;Return: void<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method is identical to showAlert(), except it uses Platfrom.runlater() to make sure that there is no overlap in threading.<br/><br/>
&emsp; &emsp; &emsp; &emsp;*ChatGPT: Original showAlert() message was having a bug where its content would not be generated when it was ran inside of Button handler. <br/>
&emsp; &emsp; &emsp; &emsp;When this issue was prompted, it made these modification which fixed the issue.

isContainer():<br/>
&emsp; &emsp; &emsp; &emsp;Params: String<br/>
&emsp; &emsp; &emsp; &emsp;Return: boolean<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method checks the inputted String (usually selected item form Item Tree) against name variable of every object in<br/>
&emsp; &emsp; &emsp; &emsp;general itemList, and when it finds the matching object, it checks its .type() against "Container" string. If they match, the method <br/>
&emsp; &emsp; &emsp; &emsp;returns True.

findItemContainer():<br/>
&emsp; &emsp; &emsp; &emsp;Params: String<br/>
&emsp; &emsp; &emsp; &emsp;Return: ItemContainer<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method finds ItemContainer object from general itemList whos name attribute matches the inputted string.

findItem():<br/>
&emsp; &emsp; &emsp; &emsp;Params: String<br/>
&emsp; &emsp; &emsp; &emsp;Return: Item<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method finds Item object from general itemList who's name attribute matches the inputted string.

drawItem():<br/>
&emsp; &emsp; &emsp; &emsp;Params: Item (item)<br/>
&emsp; &emsp; &emsp; &emsp;Return: void<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method first checks if item contains Rectangle object. If it does it runds deleteDrawing() method <br/>
&emsp; &emsp; &emsp; &emsp;(this allows this method to be used for re-drawing). Then using the item's locationX, locationY, width, and length attributes,<br/>
&emsp; &emsp; &emsp; &emsp;it creates Rectangle object. It also creates Text object that contains the name variable of item and is positioned to fit in top-left <br/>
&emsp; &emsp; &emsp; &emsp;corner of the Rectangle object. It then adds the Rectangle and Text objects to the Visualizer and finally using setter, these objects are<br/>
&emsp; &emsp; &emsp; &emsp;added to the item.

deleteDrawing():<br/>
&emsp; &emsp; &emsp; &emsp;Params: Item (item)<br/>
&emsp; &emsp; &emsp; &emsp;Return: void<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method removes the Rectangle and Text objects that are inside item form the Visualizer. <br/>
&emsp; &emsp; &emsp; &emsp;Then it uses setters to change those values to null inside the item.

onAddItemClick():<br/>
&emsp; &emsp; &emsp; &emsp;This method runs when "AddItem" button is clicked. First it gets the String value of the item selected in Item Tree. <br/>
&emsp; &emsp; &emsp; &emsp;If Item is not selected, it shows alert that indicates that.<br/>
&emsp; &emsp; &emsp; &emsp;Then it runs isItemContainer() mehtod using this string, if it is false (is Item), it shows alert that indicates that <br/>
&emsp; &emsp; &emsp; &emsp;items cannot be added to Item class objects.<br/>
&emsp; &emsp; &emsp; &emsp;Then it uses findItemContainer method to get the reference to the object that matches the selected string, and creates <br/>
&emsp; &emsp; &emsp; &emsp;a popup window that prompt the user to either<br/>
&emsp; &emsp; &emsp; &emsp;select to create "Item" or "Item Container". Corresponding methods are ran to create the Item or Item Container <br/>
&emsp; &emsp; &emsp; &emsp;when user makes selection.<br/>

&emsp; &emsp; &emsp; &emsp;*ChatGPT: A prompt similar to the description of this method was given and GPT created the code that setup the if statements <br/>
&emsp; &emsp; &emsp; &emsp;and created the correct popup windows.

createItem():<br/>
&emsp; &emsp; &emsp; &emsp;Params: ItemContainer (parent)<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method first creates a popup window with text field and labels matching the general attributes of an Item object. User <br/>
&emsp; &emsp; &emsp; &emsp;is prompted to enter the information. <br/>
&emsp; &emsp; &emsp; &emsp;The fields for locationX, locationY, width and length have a label that shows allowed range of numbers for user to input.<br/>
&emsp; &emsp; &emsp; &emsp;Once user presses "Ok", the method creates a new Item object using the user input. it then runs isWithinBounds() and <br/>
&emsp; &emsp; &emsp; &emsp;isOverlapping() method to make sure the newitem can be placed correctly. <br/>
&emsp; &emsp; &emsp; &emsp;If the code passes those if statements, it performs the following:<br/>
&emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp;Adds new item to the general itemList.<br/>
&emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp;Adds new item to parent using its addItem() method.<br/>
&emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp;Uses setParent() method on new item and adds parent to it.<br/>
&emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp;Draws new item.<br/>
&emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp;Adds the name of the new item as a TreeItem to the selected item.<br/>

&emsp; &emsp; &emsp; &emsp;*ChatGPT: It was prompted to write code to create popup window described above, and it was given the names for the labels <br/>
&emsp; &emsp; &emsp; &emsp;and text fields it should have. <br/>
&emsp; &emsp; &emsp; &emsp;Then it was prompted to create a code block that activates by "Ok" button, and creates the new Item object using user inputted <br/>
&emsp; &emsp; &emsp; &emsp;values. <br/>
&emsp; &emsp; &emsp; &emsp;If statemets and following code was written manually.

createItemContainer():<br/>
&emsp; &emsp; &emsp; &emsp;Params: ItemContainer (parent)<br/><br/>
&emsp; &emsp; &emsp; &emsp;This method is identical to createItem() but creates ItemContainer object istead.

onEditItemClick():<br/>
&emsp; &emsp; &emsp; &emsp;This method runs when "Edit" button is pressed. It first gets the stirng corresponding to the selected item in Item Tree. <br/>
&emsp; &emsp; &emsp; &emsp;Then it runs isItemContainer() in an if statement and if ture it finds the corresponding ItemContainer object using <br/>
&emsp; &emsp; &emsp; &emsp;findItemContainer, else it finds corresponding Item object using findItem() method. This object is placed inside a <br/>
&emsp; &emsp; &emsp; &emsp;final array so it can be accsessed inside code block of the button handler. <br/>
&emsp; &emsp; &emsp; &emsp;It then creates a popup window with editable text fields corresponding to the selected object. User is prompted to edit <br/>
&emsp; &emsp; &emsp; &emsp;the fields and select either "Save Changes" or "Delete". When "Delete" is selected, delteItem() method is ran on the <br/>
&emsp; &emsp; &emsp; &emsp;object, followed by object.getParent().removeItem(object) to avoid any phantom objects interfering in future operations. <br/>
&emsp; &emsp; &emsp; &emsp;When "Save Changes" is selceted, a dummy Item object is created using the values from the text field. If statements using <br/>
&emsp; &emsp; &emsp; &emsp;isOVerlapping and isWithinBounds are used to determine if the new item can be drawn without interfering with other items. <br/>
&emsp; &emsp; &emsp; &emsp;If the code passes the if statemetns, it then updates the user selected object using setter methods. Selected tree item's<br/>
&emsp; &emsp; &emsp; &emsp;name is set to the edited item's name (to accomodate name changes), and drawItem() method is ran using an updated item.<br/>

&emsp; &emsp; &emsp; &emsp;*ChatGPT: GPT was again used to create the popup window and set up the code blocks for corresponding button inputs of the <br/>
&emsp; &emsp; &emsp; &emsp;popup window. <br/>

onScanFarmClick(): <br/>
&emsp; &emsp; &emsp; &emsp;This method runs when "Scan Farm" button is pressed. It runs dron.startAnimation() method. <br/>

onGoToItemClick(): <br/>
&emsp; &emsp; &emsp; &emsp;This method runs when "Go To Item" button is pressed. It gets the string value of the selceted Item Tree item, and finds the  <br/>
&emsp; &emsp; &emsp; &emsp;corresponding ItemContainer or Item object as in previous methods. It then runs .setTragetX() and .setTargetY() methods for  <br/>
&emsp; &emsp; &emsp; &emsp;that object and then runs drone.goToItem() method using .getTragetX() and .getTargetY() as input values.

# References
Material provided during lab sessions.

ChatGPT: If it was used, its use is described inside method descriptions above following "*ChatGPT:" text.
