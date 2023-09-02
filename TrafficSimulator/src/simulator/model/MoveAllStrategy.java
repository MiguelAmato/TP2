package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy{
	  
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> lista = new ArrayList<Vehicle>();
		 for(int i = 0; i < q.size();i++) {  
			 lista.add(q.get(i));
		 }
		 return Collections.unmodifiableList(lista);
	}

}
