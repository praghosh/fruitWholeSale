package entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

	@Column(name="ADDRESS_LINE_1")
	private String addressLine1;

	@Column(name="ADDRESS_LINE_2")
	private String addressLine2;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="ZIP_CODE")
	private String pinCode;

	public Address() {
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return pinCode;
	}

	public void setZipCode(String zipCode) {
		this.pinCode = zipCode;
	}
	public String toString() {
		StringBuffer addressString= new StringBuffer ("");
		if  ((addressLine1!=null) && !addressLine1.trim().equals("")) {
			addressString.append(addressLine1 +",");
		}
		if  ((addressLine2!=null) && !addressLine2.trim().equals("")) {
			addressString.append(addressLine2 +",");			 
		}
		if  ((city!=null) && !city.trim().equals("")) {
			addressString.append(city +",");		
			 
		}
		if  ((state!=null) && !state.trim().equals("")) {
			addressString.append(state +",");					 
		}
		String finalString = addressString.toString();
		if (finalString.endsWith(",")) {
			finalString = finalString.substring(0, addressString.length() -1);
		}
		
		return finalString;
	}
}