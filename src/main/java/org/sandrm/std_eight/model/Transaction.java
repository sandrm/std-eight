package org.sandrm.std_eight.model;

public class Transaction {
	private long id;
	private int value;
	private TransactionType type;
	private Currency currency;
	private City city;
	
	
	public Transaction() {
		super();
	}
	
	
	public Transaction(long id, int value, TransactionType type, Currency currency, City city) {
		super();
		this.id = id;
		this.value = value;
		this.type = type;
		this.currency = currency;
		this.city = city;
	}


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}


	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}


	public String toStringShort() {
		return "Transaction [id=" + id + ", value=" + value + ", type=" + type + ", currency=" + currency + "]";
	}


	@Override
	public String toString() {
		return "Transaction [id=" + id + ", value=" + value + ", type=" + type + ", currency=" + currency + ", city="
				+ city + "]";
	}
	
}
