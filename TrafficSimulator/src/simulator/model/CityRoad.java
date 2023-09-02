package simulator.model;

public class CityRoad extends Road {

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		// TODO Auto-generated constructor stub
	}
 
	void reduceTotalContamination() {
		 int x;
		 if(Weather.WINDY.equals(weather) || Weather.STORM.equals(weather)) {
			 x = 10;
		 }
		 else
			 x = 2;
		 totalPol -= x;
		 if(totalPol < 0)
			 totalPol = 0;
	}
	void updateSpeedLimit() {}

 	int calculateVehicleSpeed(Vehicle v) {
		 return ((11-v.getContClass())*maxSpeed)/11;
	}
}
