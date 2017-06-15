
public class Product {
	private int ID;
	private String name;
	private String price;
	private String offers;
	private int stock;
	
	public Product(int ID, String name, String price, String offers, int stock){
		this.ID = ID;
		this.name = name;
		this.price = price;
		this.offers = offers;
		this.stock = stock;
	}
	
	// Get product ID
	public int getID(){
		return ID;
	}
	// Set product ID
	public void setID(int ID){
		this.ID = ID;
	}
	
	// Get product name
	public String getName(){
		return name;
		}
	//set product name
	public void setName(String name){
		this.name = name;
	}
	
	// Get product price
	public String getPrice(){
		return price;
		}
	// Set product name
	public void setPrice(String price){
		this.price = price;
	}
	
	// Get product offers
	public String getOffers(){
		return offers;
		}
	// set product offers
	public void setOffers(String offers){
		this.offers = offers;
	}
	
	// Get product stock levels
	public int getStock(){
		return stock;
		}
	// Set product stock levels
	public void setStock(int stock){
		this.stock = stock;
	}
	
	// Display as string when called for JList
	public String toString() {
		return "ID: " + ID + " - " + "Name: " + name + " - " + "Price: " + price + " - " + "Offers: " + offers + " - " + "Stock: " + stock;
	}
	
}
