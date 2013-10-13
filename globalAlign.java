package CS576.globalAlignment;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class globalAlign {

	private static final int match = 2;
	private static final int mismatch = 1;
	private static final int gapPenalty = 0;


	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		Cell[][] welTable = new Cell[s1.length()][s2.length()];
		createScores(welTable); //Initializes the first row / column
		fillInTable(welTable, s1, s2); //Fills in the remaining spaces



	}






	private static void fillInTable(Cell[][] welTable, String s1, String s2) {
		for (int i = 1; i < welTable.length; i++){
			for (int j = 1; j < welTable[i].length; j++){
				boolean charMatch;
				if(s1.charAt(i) == s2.charAt(j)) charMatch = true;
				else charMatch = false;
				int upScore;
				int leftScore; 
				int diagScore;

				upScore = welTable[i-1][j].score + gapPenalty;
				leftScore = welTable[i][j-1].score + gapPenalty;

				if(charMatch)diagScore = welTable[i-1][j-1].score + match;
				else diagScore = welTable[i-1][j-1].score + mismatch;

				//IF all the cells somehow have the same cost
				if(leftScore == upScore && leftScore == diagScore){
					welTable[i][j].prevCell = welTable[i-1][j];
					welTable[i][j].prevCell2 = welTable[i-1][j-1];
					welTable[i][j].prevCell3 = welTable[i][j-1];
				}
			
				//Left is highest
				if(leftScore > upScore && leftScore > diagScore){
					welTable[i][j].prevCell = welTable[i][j-1];
				}
				//Up is highest
				if(upScore > leftScore && upScore > diagScore){
					welTable[i][j].prevCell = welTable[i-1][j];
				}
				//Diagonal is highest
				if(diagScore > leftScore && diagScore > upScore){
					welTable[i][j].prevCell = welTable[i-1][j-1];
				}
				//Left and Diagonal match
				if(diagScore == leftScore && diagScore > upScore){
					welTable[i][j].prevCell = welTable[i-1][j-1];
					welTable[i][j].prevCell2 = welTable[i][j-1];
				}
				//Left and Up match
				if(upScore == leftScore && leftScore > diagScore){
					welTable[i][j].prevCell = welTable[i-1][j];
					welTable[i][j].prevCell2 = welTable[i][j-1];
					
				}
				//diagonal and up match
				if(diagScore == upScore && diagScore > leftScore){
					welTable[i][j].prevCell = welTable[i-1][j];
					welTable[i][j].prevCell2 = welTable[i-1][j-1];
				}
			

			}
		}

	}






	//Table Section
	public class Cell{
		private Cell prevCell;
		private Cell prevCell2;
		private Cell prevCell3;
		private int score;
		private int row;
		private int col;

		Cell(int score, int row, int col, Cell c){
			this.score = score;
			this.row = row;
			this.col = col;
			this.prevCell = c;
		}

	}
	protected static void createScores(Cell[][] welTable){
		//Initialize the first row
		for (int i = 0; i < welTable.length; i++){
		//	welTable[i][0] = new Cell(i * gapPenalty, i, 0, null);

		}
		//Initialize the first column
		for (int i = 0; i < welTable[0].length; i++){
	//		welTable[i][0] = new Cell(i * gapPenalty, i, 0, null);
		}

		for (int i = 1; i < welTable.length; i++){
			for(int j = 1; j < welTable[0].length; j++){

			}

		}

	}

	protected static void getBestScore(Cell[][] welTable, int i, int j){
		Cell left = welTable[i][j-1];

		Cell up = welTable[i-1][j];
		Cell diagonal = welTable[i-1][j-1];
		Cell current = welTable[i][j];

		if(left.score < up.score && left.score < up.score && left.score < diagonal.score){

		}
		if(up.score < left.score && up.score < up.score && up.score < diagonal.score){

		}
		if(diagonal.score < up.score && diagonal.score < up.score && diagonal.score < left.score){

		}

	}

}
