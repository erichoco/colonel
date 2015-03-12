import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;


public class ColonelPanic {
	public LinkedList<Server> serverList = new LinkedList<Server>();
	public LinkedList<Point> unavailableSlots = new LinkedList<Point>();
	public LinkedList<LinkedList<Server>> pools;
	public int rowNb;
	public int slotNb;
	public int unavailableSlotNb;
	public int poolNb;
	public int serverNb;
	public int capacityPerPoolPerRow;
	
	public int[][] grid;
	

	private Map<Integer, LinkedList<Server>> sortedServers = new HashMap<Integer, LinkedList<Server>>();
	
	public ColonelPanic() {
		
	}
	
	public void getBetterServers() {
		for (int i = 0, len = serverList.size(); i < len; i++) {
			Server server = serverList.get(i);
			if (sortedServers.containsKey(server.size)) {
				LinkedList<Server> curList = sortedServers.get(server.size);
				curList.add(server);
			}
			else {
				LinkedList<Server> newList = new LinkedList<Server>();
				newList.add(server);
				sortedServers.put(server.size, newList);
			}
		}
		// TODO
		
		
		 
	}
	
	//test if server fits in a row, if yes, it returns the position of server head, if no, returns -1
	public int fit(int sizeOfServer, int row){
		//for each start
		for(int i = 0; i < grid[row].length ; i++) {
			boolean fit = true;
			//for each subsequent
			for(int j = 0; j < sizeOfServer ; j++) {
				if(grid[row][i+j] != -1){
					fit = false;
					break;
				}
			}
			if(fit == true) {
				return i;
			}
		}
		return -1;
	}
	
	public void parseEntry() throws IOException{
	    BufferedReader br = new BufferedReader(new FileReader("dc.in"));
	    
	    // Parsing the first line
	    
	    StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        String[] first = line.split(" ");
        rowNb = Integer.parseInt(first[0]);
        slotNb = Integer.parseInt(first[1]);
        unavailableSlotNb = Integer.parseInt(first[2]);
        poolNb = Integer.parseInt(first[3]);
        serverNb = Integer.parseInt(first[4]);
    	System.out.println("rown nb: " + rowNb);
    	System.out.println("slot nb: " + slotNb);
    	System.out.println("unavailableSlotNb: " + unavailableSlotNb);
    	System.out.println("poolNb: " + poolNb);
    	System.out.println("serverNb: " + serverNb);
    	
    	int totalCapacity = 0;
    	pools = new LinkedList<LinkedList<Server>>();
    	for (int i = 0; i < rowNb; i++){
    		pools.add(new LinkedList<Server>());
    	}
    	grid = new int[rowNb][slotNb];
    	for(int i = 0; i < rowNb; i++){
    		for(int j = 0; j < rowNb; j++){
    			grid[i][j] = -1;
    		}
    	}
    	
    	System.out.println("****Unavailable slots list****");
        int x, y;
        for(int k = 0; k < unavailableSlotNb; k++){
        	line = br.readLine();
        	first = line.split(" ");
        	x = Integer.parseInt(first[0]);
        	y = Integer.parseInt(first[1]);
        	System.out.println("slot nb" + k + " place: " + x + " " + y);
        	unavailableSlots.add(new Point(x, y));
        	grid[x][y] = -2;
        }
        
        System.out.println("****Server list****");
        for(int k = 0; k < serverNb; k++){
        	line = br.readLine();
        	first = line.split(" ");
        	x = Integer.parseInt(first[0]);
        	y = Integer.parseInt(first[1]);
        	System.out.println("server nb" + k + " size: " + x + " capacity: " + y);
        	serverList.add(new Server(x, y));
        }
        
	}
	
	
	
	public int computePoolScore(int i){
		int score = 0;
		int tmp;
		LinkedList<Server> pool = pools.get(i);
		ListIterator<Server> it;
		Server s;
		for(int r = 0; r < rowNb; r++){
			tmp = 0;
			for(it = pool.listIterator(0); it.hasNext();){
				s = it.next();
				if(s.row != r){
					tmp += it.next().capacity;
				}
			}
			if(score > tmp){
				score = tmp;
			}
			
		}
		
		return score;
	}
	
	public boolean validateSubmission(){
		return true;
	}
	
	public static void main(String[] args){
		try {
			(new ColonelPanic()).parseEntry();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}