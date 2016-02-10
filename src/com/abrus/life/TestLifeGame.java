package com.abrus.life;

import static org.junit.Assert.*;
import javafx.scene.control.Cell;

import org.junit.Test;

public class TestLifeGame {

	@Test
	public void testCreateCell() {
		LifeCell c = new LifeCell(true);
		assertTrue("Cell create error: ", c.isAlive());
	}

	@Test
	public void testCellIsAlive() {
		LifeCell c = new LifeCell();
		assertFalse("Cell create error: ", c.isAlive());
		
		c.alive();
		assertTrue("Cell live error: ", c.isAlive());
	}

	@Test
	public void testCellToString() {
		LifeCell c = new LifeCell(true);
		assertEquals("*", c.toString());
		
		c.die();
		assertEquals("O", c.toString());
	}

	@Test
	public void testLifeGameRullerCounter() {
		LifeCell[][] c = createTestBoard(5, 6);
		
		String board_exp = "OO***O\nOO***O\nOO***O\nOOOOOO\nOOOOOO\n";
		
		c[1][3].alive();
		
		c[0][2].alive();
		c[0][3].alive();
		c[0][4].alive();
		c[1][2].alive();
		c[1][4].alive();
		c[2][2].alive();
		c[2][3].alive();
		c[2][4].alive();
		
		String board = LifeGameRuller.printBoard(c);
		System.out.println(board+"-----------------");
		
		assertEquals("Incorrect board printed: ", board_exp, board);
		
		
		assertEquals("Incorrect alive neighbours counted:", 8, LifeGameRuller.getAliveCellsCount(c, 1, 3));
		assertEquals("Incorrect alive neighbours counted:", 3, LifeGameRuller.getAliveCellsCount(c, 2, 2));
		assertEquals("Incorrect alive neighbours counted:", 5, LifeGameRuller.getAliveCellsCount(c, 2, 3));
		
		assertEquals("Incorrect alive neighbours counted:", 3, LifeGameRuller.getAliveCellsCount(c, 0, 2));
		assertEquals("Incorrect alive neighbours counted:", 0, LifeGameRuller.getAliveCellsCount(c, 4, 0));
		
	}
	
	@Test
	public void testLifeGameRullerTickPeriod() {
		LifeCell[][] c = createTestBoard(5, 6);
		
		String boardExp = "OOOOOO\nOOOOOO\nOO***O\nOOOOOO\nOOOOOO\n";
		String boardExpAfterTick = "OOOOOO\nOOO*OO\nOOO*OO\nOOO*OO\nOOOOOO\n";
		
		c[2][2].alive();
		c[2][3].alive();
		c[2][4].alive();
		
		String board = LifeGameRuller.printBoard(c);
		System.out.println(board+"-----------------");
		assertEquals("Incorrect board printed: ", boardExp, board);
		

		
		c = LifeGameRuller.tick(c);
		board = LifeGameRuller.printBoard(c);
		System.out.println(board+"-----------------");
		assertEquals("Incorrect board after tick: ", boardExpAfterTick, board);
		
		
		c = LifeGameRuller.tick(c);
		board = LifeGameRuller.printBoard(c);
		System.out.println(board+"-----------------");
		assertEquals("Incorrect board after tick: ", boardExp, board);
	}
	
	@Test
	public void testLifeGameRullerTickStable() {
		LifeCell[][] c = createTestBoard(5, 6);
		
		String boardExp = "OOOOOO\nOOOOOO\nOO**OO\nOO**OO\nOOOOOO\n";
		String boardExpAfterTick = "OOOOOO\nOOOOOO\nOO**OO\nOO**OO\nOOOOOO\n";
		
		c[2][2].alive();
		c[2][3].alive();
		c[3][2].alive();
		c[3][3].alive();
		
		String board = LifeGameRuller.printBoard(c);
		System.out.println(board+"-----------------");
		assertEquals("Incorrect board printed: ", boardExp, board);
		

		
		c = LifeGameRuller.tick(c);
		board = LifeGameRuller.printBoard(c);
		System.out.println(board+"-----------------");
		assertEquals("Incorrect board after tick: ", boardExpAfterTick, board);
		
		
		c = LifeGameRuller.tick(c);
		board = LifeGameRuller.printBoard(c);
		System.out.println(board+"-----------------");
		assertEquals("Incorrect board after tick: ", boardExp, board);
	}
	
	private LifeCell[][] createTestBoard(int rows, int cols) {
		LifeCell[][] cells = new LifeCell[rows][cols];

		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				cells[i][j] = new LifeCell();
			}
		}
		return cells;
	}
}
