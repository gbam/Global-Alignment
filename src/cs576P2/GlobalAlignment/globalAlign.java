package cs576P2.GlobalAlignment;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class globalAlign {

	private static final int match = 1;
	private static final int mismatch = 1;
	private static final int gapPenalty = 2;
	private static List<String> aligned;

	public globalAlign(String[] args2) throws Exception {
		main(args2);
	}



	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if(args.length != 3){
			System.out.println("Not Correct # of Arguements");
			System.exit(0);
		}
		String seq1FileName = args[0];
		String seq2FileName = args[1];
		String ending = args[2];
		String s1 = "";
		String s2 = "";

		try{
			File s1file = new File(seq1FileName + ending);
			File s2file = new File(seq2FileName + ending);
			BufferedReader  reader = new BufferedReader(new FileReader(s1file));
			s1 = reader.readLine();
			reader = new BufferedReader(new FileReader(s2file));
			s2 = reader.readLine();
		}catch(Exception e){
			System.out.println("Failed to open file");
		}
		s1 = " " + s1;
		s2 = " " + s2;
		Cell[][] welTable = new Cell[s1.length()][s2.length()];
		createScores(welTable); //Initializes the first row / column
		fillInTable(welTable, s1, s2); //Fills in the remaining spaces

		for(int i = 0; i < welTable.length; i++){
			for(int j = 0; j < welTable[0].length; j++){
				
			
			System.out.print(welTable[i][j].score + "  ||  ");
			}
			System.out.println();
			
		}
		Cell cornerCell = welTable[welTable.length-1][welTable[0].length-1];
		List<Paths> paths = new ArrayList<Paths>();
		backTrace(cornerCell, paths, new Paths());
		System.out.println("Possible Paths:");
		List<Integer> totalScore = new ArrayList<Integer>();
		for (Paths pp: paths){
			Integer tempScore = 0;
			for (Cell cc: pp.paths){
				System.out.print(" || " + cc.row + "," + cc.col);
				tempScore += cc.score;
			}
			totalScore.add(tempScore);
			System.out.print(" 			Score: " + tempScore);
			System.out.println("");
		}
		Integer lowestIndex = Integer.MIN_VALUE;
		Paths lowestPath = null;
		for(int i = 0; i < totalScore.size(); i ++)
			if(totalScore.get(i) >= lowestIndex){
				lowestIndex = totalScore.get(i);
				lowestPath = paths.get(i);
				
		}

		System.out.println("");
		System.out.println("");
		System.out.println("");
		int tempScore = 0;
		for (Cell cc: lowestPath.paths){
				System.out.print(" || " + cc.row + "," + cc.col);
				tempScore += cc.score;
			
		}
		System.out.print(" 			Score: " + tempScore);
		}
	//TODO Get the char at each thing and correctly print out the string



private static void backTrace(Cell c, List<Paths> possiblePaths, Paths path){


	//Case we made it back to origin
	if(c.prevCell == null && c.col == 0 && c.row == 0){
		path.paths.add(c);
		possiblePaths.add(path);
		return;
	}
	else{
		Paths newPath = new Paths();
		for (Cell cell: path.paths){
			newPath.paths.add(cell);
			
		}
		newPath.paths.add(c);
		backTrace(c.prevCell, possiblePaths, newPath);
		if(c.prevCell2 != null){
			backTrace(c.prevCell2, possiblePaths, newPath);
		}
		if(c.prevCell3 != null){
			backTrace(c.prevCell3, possiblePaths, newPath);
		}
		
		
	}
		
}




	private static void fillInTable(Cell[][] welTable, String s1, String s2) throws Exception {
		for (int i = 1; i < welTable.length; i++){
			for (int j = 1; j < welTable[i].length; j++){
				boolean charMatch;
				if(s1.charAt(i) == s2.charAt(j) && s1.compareTo(" ") != 0 && s2.compareTo(" ") != 0) charMatch = true;
				else charMatch = false;
				int upScore =  welTable[i-1][j].score - gapPenalty;
				int leftScore = welTable[i][j-1].score - gapPenalty;
				int diagScore;
				welTable[i][j] = new Cell(-1, i, j, null);
				if(charMatch)diagScore = welTable[i-1][j-1].score + match;
				else diagScore = welTable[i-1][j-1].score - mismatch;

				//IF all the cells somehow have the same cost
				if(leftScore == upScore && leftScore == diagScore){
					welTable[i][j].prevCell = welTable[i-1][j];
					welTable[i][j].prevCell2 = welTable[i-1][j-1];
					welTable[i][j].prevCell3 = welTable[i][j-1];
					welTable[i][j].score = leftScore;
				}
			
				//Left is highest
				else if(leftScore > upScore && leftScore > diagScore){
					welTable[i][j].prevCell = welTable[i][j-1];
					welTable[i][j].score = leftScore;
				}
				//Up is highest
				else if(upScore > leftScore && upScore > diagScore){
					welTable[i][j].prevCell = welTable[i-1][j];
					welTable[i][j].score = upScore;
				}
				//Diagonal is highest
				else if(diagScore > leftScore && diagScore > upScore){
					Cell pointCell = welTable[i-1][j-1];
					welTable[i][j].prevCell = pointCell;
					welTable[i][j].score = diagScore; 
				}
				//Left and Diagonal match
				else if(diagScore == leftScore && diagScore > upScore){
					welTable[i][j].prevCell = welTable[i-1][j-1];
					welTable[i][j].prevCell2 = welTable[i][j-1];
					welTable[i][j].score = diagScore;
				}
				//Left and Up match
				else if(upScore == leftScore && leftScore > diagScore){
					welTable[i][j].prevCell = welTable[i-1][j];
					welTable[i][j].prevCell2 = welTable[i][j-1];
					welTable[i][j].score = upScore;
					
				}
				//diagonal and up match
				else if(diagScore == upScore && diagScore > leftScore){
					welTable[i][j].prevCell = welTable[i-1][j];
					welTable[i][j].prevCell2 = welTable[i-1][j-1];
					welTable[i][j].score = diagScore;
				}
				else{
					throw new Exception();
				}
			

			}
		}
		
	}






	//Table Section
	public static class Cell{
		public Cell prevCell;
		public Cell prevCell2;
		public Cell prevCell3;
		public int score;
		public int row;
		public int col;

		Cell(int score, int row, int col, Cell c){
			this.score = score;
			this.row = row;
			this.col = col;
			this.prevCell = c;
		}
		

	}

	public static class Paths{
		public List<Cell> paths;
		public Paths(){
			paths = new ArrayList<Cell>();
		}
	}
	protected static void createScores(Cell[][] welTable){
		//Initialize the first row
		for (int i = 0; i < welTable.length; i++){
			welTable[i][0] = new Cell(-i * gapPenalty, i, 0, null);
			if(i != 0) 	welTable[i][0].prevCell = 	welTable[i-1][0];

		}
		//Initialize the first column
		for (int i = 0; i < welTable[0].length; i++){
			welTable[0][i] = new Cell(-i * gapPenalty, i, 0, null);
			
			if(i != 0) 	welTable[0][i].prevCell = 	welTable[0][i-1];
		}
	}
	public List<String> getAligned1() throws Exception {
		if(aligned == null)throw new Exception("No aligned Strings");
		else return aligned;
	}

}