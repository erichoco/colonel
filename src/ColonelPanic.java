import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
    			grid[i][j] = -1; //empty
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
        	grid[x][y] = -2; //unavailable
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
	
	
	public boolean isAvailable(int r, int s){
		return grid[r][s] == -1;
	}
	
	
	
	public void writeSubmission() throws FileNotFoundException, UnsupportedEncodingException{
		ListIterator<Server> it;
		PrintWriter writer = new PrintWriter("out", "UTF-8");
		Server s;
		String u;
		for(it = serverList.listIterator(0); it.hasNext();){
			s = it.next();
			u = s.row + " " + s.slot + " " + s.pool;
			writer.println(u);
		}
		writer.close();
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
	
	public int computeGlobalScore(){
		int tmp = 0;
		int score = 0;
		for(int i = 0; i < poolNb; i++){
			tmp = computePoolScore(i);
			if(score > tmp){
				score = tmp;
			}
		}
		
		return score;
	}
	
	public boolean validateSubmission(){
		return true;
	}
	
	
	public int findBestRatio(){ //FInd best ratio among unassigned servers
		double ratio = 0; 
		int index = 0;
		ListIterator<Server> it;
		double tmp = 0.;
		int k = 0;
		for(it = serverList.listIterator(0); it.hasNext();){
			Server s = it.next();
			if(!s.assigned){
				tmp = s.capacity/((double) s.size);
				if(tmp > ratio){
					index = k;
				}
			}
			k++;
		}
		return index;
	}
	
	
	
	public static void main(String[] args){
		try {
			ColonelPanic p = new ColonelPanic();
			p.parseEntry();
			boolean b = true;
			
			while(b){
				ListIterator<Server> it;
				int best = p.findBestRatio();
				Server s = p.serverList.get(best);
				
				//now try to assign it
				
				
			}
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public class Server {
		public int size;
		public int capacity;
		public int pool = -1;
		public int row;
		public int slot;
		public boolean assigned = false;


		public Server(int a, int b){
			size = a;
			capacity = b;
		}


		public void assign(int r, int s){
			try {
				for(int i = 0; i < size; i++){
					if(!isAvailable(r, s+i)){

						throw new Exception("PlaceTake");

					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assigned = true;
			for(int i = 0; i < size; i++){
				grid[r][s+i] = 0; //Switching it to assigned
			}
		}
	}
	
}
