package simulator.model;

public class InterCityRoad extends Road {

	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		 
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		switch(weather) {
		case SUNNY:
			x = 2;
			break;
		case CLOUDY:
			x = 3;
			break;
		case RAINY:
			x = 10;
		case WINDY:
			x = 15;
		case STORM:
			x = 20;
		}
		totalPol = ((100-x)*totalPol)/100;
	}

	@Override
	void updateSpeedLimit() {
		if(totalPol > contLimit) {
			curSpeedLimit = maxSpeed/2;
		}
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(Weather.STORM.equals(weather)) {
			return (curSpeedLimit*8)/10;
 		}
		return curSpeedLimit;
	}
}
