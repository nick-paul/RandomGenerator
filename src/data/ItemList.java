package data;

import java.util.ArrayList;
import java.util.Random;

import build.UtilVars;
import ui.ConsoleWindow;
import exceptions.EmptyListException;
import exceptions.InvalidTagException;

public class ItemList {
	private ArrayList<Item> list;	//All items not tagged with percents
	private ArrayList<Item> percentList; //All of the items that are tagged with percents
	private String name;
	
	public ItemList(String name) {
		this.setName(UtilVars.toLowerCase(name));
		this.list = new ArrayList<Item>();
		this.percentList = new ArrayList<Item>();
	}
	
	public void setName(String name) {
		this.name = UtilVars.toLowerCase(name.trim());
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getItemStr() throws EmptyListException {
		return getItem().getName();
	}
	
	public Item getItem() throws EmptyListException {
		int amountAdded = 0;
		if (percentList != null) {
			//Add possible items to the list
			for (Item i : percentList) {
				if (i.shouldAdd()) {
					amountAdded++;
					list.add(i);
				}
			}
		}
		
		Random rand = new Random();		
		
		Item out;
		try {
			out = list.get(rand.nextInt(list.size()));
		} catch (IllegalArgumentException e) {
			throw new EmptyListException("The list '" + this.name + "' contains no elements");
		}
		
		if (percentList != null) {
			//Remove the added items from the top of the list
			for(int i = 0; i < amountAdded; i++) {
				list.remove(list.size()-1);
			}
		}
		
		return out;
	}
	
	public void add(String item) {
		try {
			Item i = new Item(item.replace("\n", "").replace("\r", ""));
			if(i.hasPercentTag()) {
				this.percentList.add(i);
			} else {
				this.list.add(i);
			}
		} catch (InvalidTagException e) {
			ConsoleWindow.printQuiet(e.getMessage());
		}
	}
	
	public String toString() {
		String str = this.name + ": [";
		for (Item i : list) {
			str += i.toString() + ", ";
		}
		return str.substring(0, str.length()-2) + "]";
	}
}
