package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{

	private int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot){
		this.timeSlot = timeSlot;
	}
	 
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,int currTime) {
		if(roads.isEmpty()) {
			return -1;
		}
		if(currGreen == -1) {
			int maximo = 0;
			int ret = 0;
			for(int i = 0; i < qs.size();i++) {  
				if(qs.get(i).size() > maximo) {
					maximo = qs.get(i).size();
					ret = i;
				}				
			}
			
			return ret;
		}
		if(currTime - lastSwitchingTime < timeSlot) {
			return currGreen;
		}
		 
		int i = (currGreen + 1)%qs.size();
		int maximo = 0;
		int ret = 0;
		while(i != currGreen) {   
			if(qs.get(i).size() > maximo) {
				maximo = qs.get(i).size();
				ret = i;
			}
			i = (i+1)%qs.size();
		}
		if(qs.get(currGreen).size() > qs.get(ret).size())
			return currGreen;
		return ret;
	}
	
}
