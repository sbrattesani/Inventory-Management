
public class Customer {

	private int account;
	private String name;
	private String address;
	private String email;
	private String status;

	public Customer(int account, String name, String address, String email, String status) {
		this.account = account;
		this.name = name;
		this.address = address;
		this.email = email;
		this.status = status;

	}

	// Get customer account
	public int getAccount() {
		return account;
	}

	// Set customer account
	public void setAccount(int account) {
		this.account = account;
	}

	// Get customer name
	public String getName() {
		return name;
	}

	// set customer name
	public void setName(String name) {
		this.name = name;
	}

	// Get customer address
	public String getAddress() {
		return address;
	}

	// Set customer address
	public void setAddress(String address) {
		this.address = address;
	}

	// Get customer email
	public String getEmail() {
		return email;
	}

	// set customer email
	public void setEmail(String email) {
		this.email = email;
	}

	// Get customer status
	public String getStatus() {
		return status;
	}

	// set customer status
	public void setStatus(String offers) {
		this.status = offers;
	}

	// Display as string when called for JList
	public String toString() {
		return "Account: " + account + " - " + "Name: " + name + " - " + "Address: " + address + " - " + "Email: "
				+ email + " - " + "Status: " + status;
	}

}
