package ca.sheridancollege.enums;

public enum CustomerType {
	STUDENT ("Student"),
	STAFF ("Staff"),
	FACULTY ("Faculty");
	
	private final String customerType;

	CustomerType(String customerType) {
		this.customerType = customerType;
	}
	
	public String getCustomerType() {
		return this.customerType;
	}
}
