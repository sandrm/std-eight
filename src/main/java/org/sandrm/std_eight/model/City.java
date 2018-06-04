package org.sandrm.std_eight.model;

public enum City {
	NY("New-York"),
	LOS_ANGELOS("Los-Angelos"),
	KYIV("Kyiv");

	
	private String name;

	private City(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
}
