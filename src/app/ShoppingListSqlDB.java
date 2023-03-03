
package app;

import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;


public class ShoppingListSqlDB extends JFrame  {
	
	String serverName ="jdbc:mysql://localhost/";
	String dbName ="Shoppingdb";
	String dbUser="root";
	String dbPassword ="";
	
	

		//initialize jframe components
		JPanel p1=new JPanel();
		JPanel p2=new JPanel();
		JPanel p3=new JPanel();
		GridLayout grid = new GridLayout(3,0);
		GridLayout grid1 = new GridLayout(6,2,4,5);
		GridLayout grid2 = new GridLayout(4,2,6,15);
		
		//main section
		JLabel month = new JLabel("Select Month: ");
		JLabel item = new JLabel("Select Item: ");
		JButton newItemBtn = new JButton("Enter New Item Name");
		JTextField newItemInput =new JTextField(10);
		JLabel price = new JLabel("Enter Price: ");
		JTextField priceInput =new JTextField(10);
		JButton addBtn =new JButton("Add Item");
		JButton displayBtn =new JButton("Show All Items");
		JButton updateBtn =new JButton("Update Item");
		JButton deleteBtn =new JButton("Delete Item");
		JButton returnBtn2 =new JButton("HIDE");
		
		//update section
		JLabel month2 = new JLabel("Select Month: ");
		JLabel item2 = new JLabel("Select Item: ");
		JLabel updatePrice = new JLabel("Update Price: ");
		JTextField updateItemPrice =new JTextField(10);
		JButton submitBtn =new JButton("Update!");
		JButton returnBtn1 =new JButton("HIDE");
		
		//delete section
		JLabel month3 = new JLabel("Select Month: ");
		JLabel item3 = new JLabel("Select Item: ");
		JButton removeBtn =new JButton("Delete Item!");
		JButton removeAllBtn =new JButton("Delete All Items!");
		JButton returnBtn3 =new JButton("HIDE");
		
		//label for error messages
		JLabel errorMessage =new JLabel("");
		
		
		//string to hold all items
		String output="";
		
		//text area to hold string output
		JTextArea allItems =new JTextArea();
		
		
public ShoppingListSqlDB() {
	this.dbFrame();
}


	private void dbFrame() {
		ArrayList<String> months= new ArrayList<>();
		months.add("JANUARY");
		months.add("FEBRUARY");
		months.add("MARCH");
		months.add("APRIL");
		months.add("MAY");
		months.add("JUNE");
		months.add("JULY");
		months.add("AUGUST");
		months.add("SEPTEMBER");
		months.add("OCTOBER");
		months.add("NOVEMBER");
		months.add("DECEMBER");
		
		//dropdown for the months in different sections
		JComboBox<String> monthCombo = new JComboBox<>();
		for(int i = 0; i < months.size(); i++) {
		monthCombo.addItem(months.get(i));
		monthCombo.setSelectedItem(null);
		}
		JComboBox<String> updateMonthCombo = new JComboBox<>();
		for(int i = 0; i < months.size(); i++) {
		updateMonthCombo.addItem(months.get(i));
		updateMonthCombo.setSelectedItem(null);
		}
		JComboBox<String> deleteMonthCombo = new JComboBox<>();
		for(int i = 0; i < months.size(); i++) {
		deleteMonthCombo.addItem(months.get(i));
		deleteMonthCombo.setSelectedItem(null);
		}
		
		//arraylist holding the items names
		ArrayList<String> itemsList= new ArrayList<>();
		ArrayList<String> itemsListShadow= new ArrayList<>();
		
		//arraylist holding the items
		ArrayList<String> items= new ArrayList<>();
		
		
		//dropdown for the items in different sections
		JComboBox<String> itemsCombo = new JComboBox<>();
		JComboBox<String> updateItemsCombo = new JComboBox<>();		
		JComboBox<String> deleteItemsCombo = new JComboBox<>();
		
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword); 
	       Statement stmt=connection.createStatement();
	       System.out.println("DATABASE connection successful");
	       ResultSet rs=stmt.executeQuery("select * from items");
			while(rs.next()) {
				itemsList.add(rs.getString(1));
			}
			for(int i = 0; i < itemsList.size(); i++) {
				itemsCombo.addItem(itemsList.get(i));
				itemsCombo.setSelectedItem(null);
				}
			rs.close();
			stmt.close();
			connection.close();
			}
		catch(Exception err){ 
			System.out.println("ERROR!!!");
			System.out.println(err);
		}
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword); 
	       Statement stmt=connection.createStatement(); 
	       ResultSet rs=stmt.executeQuery("SELECT `p`.`item_name` FROM `prices` `p` INNER JOIN `items` `i` ON `i`.`item_name` = `p`.`item_name`"); 
	       int peg = itemsList.size();
	       while(rs.next()) {
				itemsList.add(rs.getString(1));
			}
			for(int i = peg ; i < itemsList.size(); i++) {
				updateItemsCombo.addItem(itemsList.get(i));
				deleteItemsCombo.addItem(itemsList.get(i));
				updateItemsCombo.setSelectedItem(null);
				deleteItemsCombo.setSelectedItem(null);
				}
			rs.close();
			stmt.close();
			connection.close();
			}
		catch(Exception err){ 
			System.out.println("ERROR!!!");
			System.out.println(err);
		}
		
		//add jpanels to jframe
		this.add(p1);
		this.add(p2);
		this.add(p3);
		
		//add items to first panel
		p1.add(item);
		p1.add(itemsCombo);
		p1.add(month);
		p1.add(monthCombo);
		p1.add(newItemInput);
		p1.add(newItemBtn);
		p1.add(price);
		p1.add(priceInput);
		p1.add(addBtn);
		p1.add(displayBtn);
		p1.add(updateBtn);
		p1.add(deleteBtn);
		p1.setLayout(grid1);
		
		
		p2.setVisible(false);
		p3.setVisible(false);
		allItems.setEditable(false);
		
		
		//action listener to display all items
		displayBtn.addActionListener(e->{
			
				p3.setVisible(false);
				p2.setVisible(false);
				p3.removeAll();
				p3.setVisible(true);
				p1.setVisible(false);
				output="";
				try{  
					Class.forName("com.mysql.jdbc.Driver");  
					Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword); 
			       Statement stmt=connection.createStatement(); 
			       ResultSet rs=stmt.executeQuery("SELECT `i`.`item_name`, `p`.`month`, `p`.`item_price` FROM `items` `i` "
			       		+ "INNER JOIN `prices` `p` ON `i`.`item_name` = `p`.`item_name` ORDER BY `item_name`"); 
			       items.clear();
					while(rs.next()) {
						for(int i = 1;i<4;i++)
							items.add(rs.getString(i));
					}int j = 0;
					for(String i : items) {
						j++;
						output = output.concat( i + " ");
						if(j%3==0)output = output.concat("\n");
					}
					rs.close();
					stmt.close();
					connection.close();
					System.out.println("ALL ITEMS DISPLAYED");
					}
				catch(Exception err){ 
					System.out.println("ERROR!!!");
					System.out.println(err);
				}
				allItems.setText(output);
				p3.add(allItems);
				p3.add(returnBtn2);
				
			if(output.isBlank()) {
			p3.setVisible(false);
			p3.removeAll();
			errorMessage.setText("Shopping List is Empty, add items.");
			p3.add(errorMessage);
			p3.add(returnBtn2);
			p3.setVisible(true);
			}
		});
		
		//action listener to display update menu
		updateBtn.addActionListener(e->{
			p2.setVisible(false);
			p2.removeAll();
			p2.add(item2);
			p2.add(updateItemsCombo);
			p2.add(month2);
			p2.add(updateMonthCombo);
			p2.add(updatePrice);
			p2.add(updateItemPrice);
			p2.add(returnBtn1);
			p2.add(submitBtn);
			p2.setLayout(grid2);
			p1.setVisible(false);
			p3.setVisible(false);
			p2.setVisible(true);
			
		});
		
		//action listener to display delete menu
		deleteBtn.addActionListener(e->{
			p2.setVisible(false);
			p2.removeAll();
			p2.add(item3);
			p2.add(deleteItemsCombo);
			p2.add(removeAllBtn);
			p2.add(removeBtn);
			p2.add(returnBtn3);
			p2.setLayout(grid2);
			p1.setVisible(false);
			p3.setVisible(false);
			p2.setVisible(true);
		});
		
		//return buttons 
		returnBtn1.addActionListener(e->{
			p2.setVisible(false);
			p1.setVisible(true);
			p3.setVisible(false);
		});
		returnBtn2.addActionListener(e->{
			p2.setVisible(false);
			p1.setVisible(true);
			p3.setVisible(false);
			output = "";
		});
		returnBtn3.addActionListener(e->{
			p2.setVisible(false);
			p1.setVisible(true);
			p3.setVisible(false);
		});
		
		//action listener to add new item name to items dropdown
		newItemBtn.addActionListener (e->{
			//if new item name textfield is empty, give error
			if (newItemInput.getText().isBlank()) {
				errorMessage.setText("Enter new item name!!!");
				p2.removeAll();
				p2.add(errorMessage);
				p2.setVisible(true);
				p3.setVisible(false);
			}else {			
				
			try{  
				Class.forName("com.mysql.jdbc.Driver");  
				Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword); 
		       Statement stmt=connection.createStatement(); 
		       stmt.executeUpdate("INSERT INTO `items` ( item_name ) "
			        +"VALUES ( '"+newItemInput.getText().toUpperCase()+"')");
		       ResultSet rs=stmt.executeQuery("select `item_name` from `items` WHERE `item_name` = ('"+newItemInput.getText().toUpperCase()+"')");
		       p1.setVisible(false);
				while(rs.next()) {
					itemsList.add(rs.getString(1));
				}
				for(int i = itemsList.size()-1; i < itemsList.size(); i++) {
					itemsCombo.addItem(itemsList.get(i));
					itemsCombo.setSelectedItem(itemsList.get(i));
					}
				
				p1.setVisible(true);
				newItemInput.setText("");
				rs.close();
				stmt.close();
				connection.close();
				System.out.println("NEW ITEM NAME ADDED");
				}
			catch(Exception err){ 
				System.out.println("ERROR!!!");
				System.out.println(err);
			}
			//close jpanel p2 in case there was an error displayed
				p2.setVisible(false);
			}
		});
		
		//action listener to add item to the database
		addBtn.addActionListener (e->{
			Scanner check = new Scanner(priceInput.getText());
			//checking if month and item is selected
			if(monthCombo.getSelectedItem()==null || itemsCombo.getSelectedItem()==null){
				errorMessage.setText("Select Item and Month!!! ");
				p3.removeAll();
				p3.add(errorMessage);
				p3.setVisible(true);
			}else //using scanner to check if price input is in number/double 
				if (!check.hasNextDouble()) {
					errorMessage.setText("Enter item price in number!!! ");
				p3.removeAll();
				p3.add(errorMessage);
				p3.setVisible(true);
			}else {
				p2.setVisible(false);
				p3.setVisible(false);
				p3.removeAll();
				String selectedMonth = monthCombo.getSelectedItem().toString().toUpperCase();
				String selectedItem = itemsCombo.getSelectedItem().toString().toUpperCase();
				double itemPrice= Double.valueOf(priceInput.getText());
				try{  
					Class.forName("com.mysql.jdbc.Driver");
					Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword);
			       Statement stmt=connection.createStatement();
			       stmt.executeUpdate("INSERT INTO `prices` ( item_name, item_price, month) "
			   			        +"VALUES (  '"+selectedItem+"','"+itemPrice+"','"+selectedMonth+"')");
					
					stmt.close();
					connection.close();
					System.out.println("ADDED TO DATABASE!!!");
					}
				catch(Exception err){ 
					System.out.println("ERROR!!!");
					System.out.println(err);
					}
				p1.setVisible(false);
						if(!itemsListShadow.contains(selectedItem)) {
							updateItemsCombo.addItem(selectedItem);
							deleteItemsCombo.addItem(selectedItem);
						}
				itemsListShadow.add(selectedItem);
				itemsCombo.setSelectedItem(null);
				monthCombo.setSelectedItem(null);
			output="";
			try{  
				Class.forName("com.mysql.jdbc.Driver");  
				Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword); 
		       Statement stmt=connection.createStatement(); 
		       ResultSet rs=stmt.executeQuery("SELECT `i`.`item_name`, `p`.`month`, `p`.`item_price` "
		       		+ "FROM `items` `i` INNER JOIN `prices` `p` ON `i`.`item_name` = `p`.`item_name` ORDER BY `item_name`"); 
		       items.clear();
				while(rs.next()) {
					for(int i = 1;i<4;i++)
						items.add(rs.getString(i));
				}int j = 0;
				for(String i : items) {
					j++;
					output = output.concat( i + " ");
					if(j%3==0)output = output.concat("\n");
				}
				rs.close();
				stmt.close();
				connection.close();
				}
			catch(Exception err){ 
				System.out.println("ERROR!!!");
				System.out.println(err);
			}
				allItems.setText(output);
				p3.add(allItems);
				p3.add(returnBtn2);
				priceInput.setText("");
				p3.setVisible(true);
			}
		});
		
		//action listener to execute update command
		submitBtn.addActionListener(e->{
			Scanner check = new Scanner(updateItemPrice.getText());
			if(updateMonthCombo.getSelectedItem()==null || updateItemsCombo.getSelectedItem()==null){
				errorMessage.setText("Select Item and Month!!! ");
				p3.removeAll();
				p3.add(errorMessage);
				p3.setVisible(true);
				
			}else if (!check.hasNextDouble()) {
				errorMessage.setText("Enter item's new price in number!!! ");
				p3.removeAll();
				p3.add(errorMessage);
				p3.setVisible(true);
			}
			else {
				p3.removeAll();
				String selectedMonthUpdate = updateMonthCombo.getSelectedItem().toString().toUpperCase();
				String selectedItemUpdate = updateItemsCombo.getSelectedItem().toString().toUpperCase();
				double itemNewPrice= Double.valueOf(updateItemPrice.getText());
				
				try{  
					Class.forName("com.mysql.jdbc.Driver");  
					Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword);
			       Statement stmt=connection.createStatement();
			       stmt.executeUpdate("UPDATE `prices` "
			       		+ "SET `item_price` = ( '"+itemNewPrice+"' ) "
			   			        +"WHERE `item_name` = (  '"+selectedItemUpdate+"')"
			   			     +"AND `month` = (  '"+selectedMonthUpdate+"')");
					stmt.close();
					connection.close(); 
					System.out.println("PRICE UPDATED!!!");
					}
				catch(Exception err){ 
					System.out.println("ERROR!!!");
					System.out.println(err);
					}
				
				p2.setVisible(false);
				updateItemsCombo.setSelectedItem(null);
				updateMonthCombo.setSelectedItem(null);
				output="";
				try{  
					Class.forName("com.mysql.jdbc.Driver");  
					Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword); 
			       Statement stmt=connection.createStatement(); 
			       ResultSet rs=stmt.executeQuery("SELECT `i`.`item_name`, `p`.`month`, `p`.`item_price` FROM `items` `i` "
			       		+ "INNER JOIN `prices` `p` ON `i`.`item_name` = `p`.`item_name` ORDER BY `item_name`"); 
			       items.clear();
					while(rs.next()) {
						for(int i = 1;i<4;i++)
							items.add(rs.getString(i));
					}int j = 0;
					for(String i : items) {
						j++;
						output = output.concat( i + " ");
						if(j%3==0)output = output.concat("\n");
					}
					rs.close();
					stmt.close();
					connection.close();
					}
				catch(Exception err){ 
					System.out.println("ERROR!!!");
					System.out.println(err);
				}
				allItems.setText(output);
				p3.add(allItems);
				p3.add(returnBtn2);
				updateItemPrice.setText("");
				p3.setVisible(true);
			}
		});
		
		//action listener to execute single item delete command
		removeBtn.addActionListener(e->{
			if( deleteItemsCombo.getSelectedItem()==null){
				errorMessage.setText("Select Item to Delete!!! ");
				p3.removeAll();
				p3.add(errorMessage);
				p3.setVisible(true);
				
			}else {
				p3.removeAll();
				String selectedItem = deleteItemsCombo.getSelectedItem().toString().toUpperCase();
				try{  
					Class.forName("com.mysql.jdbc.Driver");  
					Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword); 
			       Statement stmt=connection.createStatement();
			       stmt.executeUpdate("DELETE FROM `items` "
			   			        +"WHERE `item_name` = (  '"+selectedItem+"')");
					stmt.close();
					connection.close();
					itemsCombo.removeItem(selectedItem);
					updateItemsCombo.removeItem(selectedItem);
					deleteItemsCombo.removeItem(selectedItem);
					System.out.println("SELECTED ITEM DELETED!!!");
					}
				catch(Exception err){ 
					System.out.println("ERROR!!!");
					System.out.println(err);
					}

				p2.setVisible(false);
				deleteItemsCombo.setSelectedItem(null);
				output="";
				try{  
					Class.forName("com.mysql.jdbc.Driver");  
					Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword); 
			       Statement stmt=connection.createStatement(); 
			       ResultSet rs=stmt.executeQuery("SELECT `i`.`item_name`, `p`.`month`, `p`.`item_price` FROM `items` `i` "
			       		+ "INNER JOIN `prices` `p` ON `i`.`item_name` = `p`.`item_name` ORDER BY `item_name`"); 
			       items.clear();
					while(rs.next()) {
						for(int i = 1;i<4;i++)
							items.add(rs.getString(i));
					}int j = 0;
					for(String i : items) {
						j++;
						output = output.concat( i + " ");
						if(j%3==0)output = output.concat("\n");
					}
					rs.close();
					stmt.close();
					connection.close();
					}
				catch(Exception err){ 
					System.out.println("ERROR!!!");
					System.out.println(err);
				}
				allItems.setText(output);
				p3.add(allItems);
				p3.add(returnBtn2);
				p3.setVisible(true);
				if(output.isBlank()) {
					p2.setVisible(false);
					p1.setVisible(true);
					p3.setVisible(false);
					p3.removeAll();
					errorMessage.setText("All Items Deleted, Shopping List is Empty, add items.");
					p3.add(errorMessage);
					p3.add(returnBtn2);
					p3.setVisible(true);
				}
			}
				
			
		});
		
		//action listener to delete all items
		removeAllBtn.addActionListener(e->{
			p2.setVisible(false);
			p1.setVisible(true);
			p3.setVisible(false);
			try{  
				Class.forName("com.mysql.jdbc.Driver");  
				Connection connection=DriverManager.getConnection(serverName+dbName,dbUser,dbPassword); 
		       Statement stmt=connection.createStatement();
		       stmt.executeUpdate("DELETE FROM `items`");
		       stmt.executeUpdate("DELETE FROM `prices`");
				stmt.close();
				connection.close();  
				System.out.println("ALL ITEMS DELETED!!!");
				itemsCombo.removeAllItems();
				updateItemsCombo.removeAllItems();
				deleteItemsCombo.removeAllItems();
				}
			catch(Exception err){ 
				System.out.println("ERROR!!!");
				System.out.println(err);
				}
			output="";
			p3.removeAll();
			errorMessage.setText("All Items Deleted, Shopping List is Empty, add items.");
			p3.add(errorMessage);
			p3.add(returnBtn2);
			p3.setVisible(true);
		});
		
		this.setTitle("Shopping List DB");
		this.getContentPane();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(grid);
		this.setSize(600,700);
		this.setVisible(true);
		p1.setBackground(Color.decode("#DADFF7"));
		p2.setBackground(Color.decode("#DADFF7"));
		p3.setBackground(Color.decode("#DADFF7"));
		itemsCombo.setBackground(Color.decode("#A0C1D1"));
		monthCombo.setBackground(Color.decode("#A0C1D1"));
		updateItemsCombo.setBackground(Color.decode("#A0C1D1"));
		updateMonthCombo.setBackground(Color.decode("#A0C1D1"));
		deleteItemsCombo.setBackground(Color.decode("#A0C1D1"));
		newItemInput.setBackground(Color.decode("#A0C1D1"));
		priceInput.setBackground(Color.decode("#A0C1D1"));
		updateItemPrice.setBackground(Color.decode("#A0C1D1"));
		allItems.setBackground(Color.LIGHT_GRAY);
		errorMessage.setForeground(Color.red);
		newItemBtn.setBackground(Color.decode("#232C33"));
		addBtn.setBackground(Color.decode("#232C33"));
		displayBtn.setBackground(Color.decode("#232C33"));
		deleteBtn.setBackground(Color.decode("#232C33"));
		returnBtn2.setBackground(Color.decode("#232C33"));
		submitBtn.setBackground(Color.decode("#232C33"));
		returnBtn1.setBackground(Color.decode("#232C33"));
		removeBtn.setBackground(Color.decode("#232C33"));
		removeAllBtn.setBackground(Color.decode("#232C33"));
		returnBtn3.setBackground(Color.decode("#232C33"));
		updateBtn.setBackground(Color.decode("#232C33"));
		newItemBtn.setForeground(Color.decode("#FFFFFF"));
		addBtn.setForeground(Color.decode("#FFFFFF"));
		displayBtn.setForeground(Color.decode("#FFFFFF"));
		deleteBtn.setForeground(Color.decode("#FFFFFF"));
		returnBtn2.setForeground(Color.decode("#FFFFFF"));
		submitBtn.setForeground(Color.decode("#FFFFFF"));
		returnBtn1.setForeground(Color.decode("#FFFFFF"));
		removeBtn.setForeground(Color.decode("#FFFFFF"));
		removeAllBtn.setForeground(Color.decode("#FFFFFF"));
		returnBtn3.setForeground(Color.decode("#FFFFFF"));
		updateBtn.setForeground(Color.decode("#FFFFFF"));
	
}


	public static void main(String[] args) {
		
		new ShoppingListSqlDB();

	}



}
