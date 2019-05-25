package ca.sheridancollege.enums;

public enum BikeState {
	AVAILABLE ("Available"),
	RENTED ("Rented"),
	OUT_OF_SERVICE ("Out of Service"),
	NEEDS_SAFETY_CHECK ("Needs Safety Check"),
	MISSING ("Missing"),
	IN_MAINTENANCE ("In Maintenance");
	
	private final String stateName;
    
	BikeState(String stateName) {
        this.stateName = stateName;
    }
    
    public String getStateName() {
		return stateName;
	}
}
