package com.abrus.life;

public class LifeGameRuller {
	public static LifeCell[][] tick(LifeCell[][] cells){
		LifeCell[][] cellsNewGen = new LifeCell[cells.length][];
		final int rows = cells.length;
		for(int row = 0; row < rows; ++row){
			cellsNewGen[row] = new LifeCell[cells[row].length];
			for(int col = 0; col < cells[row].length; ++col){
				LifeCell cell = cells[row][col];
				cellsNewGen[row][col] = new LifeCell();
				final int aliveCellsCount = getAliveCellsCount(cells, row, col);
				if(cell.isAlive()){
					if(!(aliveCellsCount < 2 || aliveCellsCount >3)) {
						cellsNewGen[row][col].alive();
					}
				} else {
					if(aliveCellsCount == 3){
						cellsNewGen[row][col].alive();
					}
				}
			}
		}
		
		return cellsNewGen;
	}

	public static int getAliveCellsCount(LifeCell[][] cells, int row, int col) {
		int aliveCount = 0;
		
		int startRow = row == 0 ? row : row - 1;
		int endRow = row == cells.length - 1 ? row : row + 2;
		
		int startCol = col == 0 ? col : col - 1;
		int endCol = col == cells[row].length - 1 ? col : col + 2;
		
		for(int i = startRow; i < endRow; ++i){
			for(int j = startCol; j < endCol; ++j){
				if(cells[i][j].isAlive() && !(row == i && col == j)) {
					aliveCount++;
				}
			}
		}
		
		return aliveCount;
	}

	public static String printBoard(LifeCell[][] cells) {
		String res = "";
		
		for(LifeCell[] cellRow : cells){
			for(LifeCell c : cellRow){
				res += c.toString();
			}
			res += "\n";
		}
		return res;
	}
}
