package xiangqi.student_ejharding;

import java.util.ArrayList;
import java.util.concurrent.CompletionException;

import xiangqi.common.XiangqiColor;
import xiangqi.common.XiangqiCoordinate;
import xiangqi.common.XiangqiPiece;
import xiangqi.common.XiangqiPieceType;

/**
 * data structure to represent the Xiangqi Board
 * coordinates are stored internally from the red aspect
 * @author Everett Harding
 *
 */
public class Board {

	Intersection board[][];
	private int numRanks, numFiles;

	static class Intersection{
		XiangqiPiece piece;

		public Intersection(XiangqiPiece p ){
			this.piece = p;
		}

		boolean place(XiangqiPiece p){
			this.piece = p;
			return true;
		}

		XiangqiPiece getPiece(){
			return this.piece;
		}

		/**
		 * returns the piece in this cell, and replaces that piece with a null piece
		 * @return the piece in this cell
		 */
		XiangqiPiece removePiece(){
			XiangqiPiece temp = this.piece;
			this.piece = new XiangqiPieceImpl(XiangqiPieceType.NONE, XiangqiColor.NONE);
			return temp;
		}

	} 

	public Board(int numRanks, int numFiles){
		this.numRanks = numRanks;
		this.numFiles = numFiles;
		board = new Intersection[numRanks][numFiles];
		//initialize the board array to none-pieces
		for(int rank = 0; rank < numRanks; rank++){
			for(int file = 0; file < numFiles; file++){
				board[rank][file] = new Intersection(new XiangqiPieceImpl(XiangqiPieceType.NONE, XiangqiColor.NONE));
			}
		}
	}

	/**
	 * method for returning the piece at the specified location
	 * if an invalid location is given, throws CompletionException
	 * if no piece is at the location returns noneType piece
	 * @param location:: the coordinates from which to remove the piece
	 * @return the piece found at the given location
	 */
	public XiangqiPiece getPieceAt(XiangqiCoordinate location){
		int rank = location.getRank()-1;
		int file = location.getFile()-1;

		if(!isValidLocation(location)){
			return new XiangqiPieceImpl(XiangqiPieceType.NONE, XiangqiColor.NONE);
		}

		return board[rank][file].getPiece();
	}

	/**
	 * removes the piece from the given location if it can, and returns it
	 * if the given location is invalid, a None-Type piece is returned
	 * @param location : the location from which to return the piece
	 * @return the piece formerly stored at the given location
	 */
	public XiangqiPiece removePieceFrom(XiangqiCoordinate location){
		int rank = location.getRank()-1;
		int file = location.getFile()-1;

		if(!isValidLocation(location)){
			return new XiangqiPieceImpl(XiangqiPieceType.NONE, XiangqiColor.NONE);
		}

		return board[rank][file].removePiece();
	}

	/**
	 * places the given piece on the board in the specified location
	 * @param piece :: the piece to be placed
	 * @param location :: the coordinates at which to place the given piece
	 * @return true if the piece was successfully placed, false otherwise
	 */
	public boolean placePieceAt(XiangqiPiece piece, XiangqiCoordinate location){
		/*
		 * error checks:
		 * valid board location
		 * will not duplicate pieces on the board more times than they are supposed to be
		 */
		int rank, file;
		if(!isValidLocation(location)){
			return false;
		}
		if(piece.getColor() == XiangqiColor.RED){
			rank = location.getRank() -1;
			file = location.getFile() -1;
		} else {
			rank = location.getRank() -1;
			file = location.getFile() -1;
		} return board[rank][file].place(piece);
	}

	/**
	 * determines if the given (rank, file) location is a valid coordinate in the current board
	 * @param loc : the location to check
	 * @return true if the location exists in the board, false otherwise
	 */
	public boolean isValidLocation(XiangqiCoordinate loc){
		int rank = loc.getRank() -1;
		int file = loc.getFile() -1;
		return rank < numRanks && file < numFiles && rank >= 0 && file >= 0;
	}

	public int getNumRanks(){
		return numRanks;
	}

	public int getNumFiles(){
		return numFiles;
	}

	@Override
	public String toString(){
		String s = "";
		for(int r = 0; r < getNumRanks(); r++){
			for(int f = 0; f < getNumFiles(); f++){
				s += getPieceChar(board[r][f].getPiece()) + " ";
			}
			s+="\n";
		}
		return s;
	}


	/**
	 * a method to determine if the given color's general is in check
	 * @param player : the color of the general who might be in check
	 * @return true if the general is in check, false otherwise
	 */
	public boolean generalInCheck(XiangqiColor player) {
		MyCoordinate generalLoc = findGeneral(player);
		XiangqiColor attackingPlayer;
		
		if(player.equals(XiangqiColor.BLACK)){
			attackingPlayer = XiangqiColor.RED;
		} else {
			attackingPlayer = XiangqiColor.BLACK;
		}
		
		ArrayList<MyCoordinate> pieceLocs = getPiecesOf(attackingPlayer);

		for(MyCoordinate loc: pieceLocs){
			Move test = new Move(loc, generalLoc, this, attackingPlayer);
			if(test.isValid()){
				return true;
			}
		}
		return false;
	}

	public boolean isWinFor(XiangqiColor player){
		XiangqiColor defendingPlayer;
		final boolean checkmate = true;
		
		if(player == XiangqiColor.RED){
			defendingPlayer = XiangqiColor.BLACK;
		} else {
			defendingPlayer = XiangqiColor.RED;
		}
		
		
		MyCoordinate generalLoc = findGeneral(defendingPlayer);
		//create all possible moves for defending pieces. If any are valid, not checkmate. if they are all invalid, then checkmate.
		//find checkmate while under threat
		ArrayList<MyCoordinate> defendingPieceLocations = getPiecesOf(defendingPlayer);
		
//		if(!generalCanMove(generalLoc, defendingPlayer)){
//			if(pieceCanFreeGeneral(defendingPieceLocations, generalLoc)){
//				return !checkmate;
//			} else {
//				return checkmate;
//			}
//		}
		
		if(!generalInCheck(defendingPlayer)){
			return !checkmate;
		}
		
		//general is able to move, may be in check
		for(MyCoordinate loc: defendingPieceLocations){
			if(canSaveGeneral(loc, defendingPlayer)){
				return !checkmate;
			}
		}
		return checkmate;
	}
	
//	private boolean pieceCanFreeGeneral(ArrayList<MyCoordinate> pieceLocs, MyCoordinate generalLoc){
//		XiangqiColor player = this.getPieceAt(generalLoc).getColor();
//		for(MyCoordinate loc: pieceLocs){
//			for(int r = 0; r < numRanks; r++){
//				for(int f = 0; f < numFiles; f++){
//					MyCoordinate c = new MyCoordinate(r+1, f+1, this);
//					Move m = new Move(loc, c, this, player);
//					if(m.isValid()){
//						m.doMove();
//						if(generalCanMove(generalLoc, player)){
//							m.undo();
//							return true;
//						}
//						m.undo();
//					}
//				}
//			}
//		}
//		return false;
//	}
//	
//	private boolean generalCanMove(MyCoordinate generalLoc, XiangqiColor player){
//		ArrayList<Move> moves = new ArrayList<Move>();
//		
//		MyCoordinate nextLoc = new MyCoordinate(generalLoc.getRank() - 1, generalLoc.getFile(), this);
//		moves.add(new Move(generalLoc, nextLoc, this, player));
//		
//		nextLoc = new MyCoordinate(generalLoc.getRank() + 1, generalLoc.getFile(), this);
//		moves.add(new Move(generalLoc, nextLoc, this, player));
//		
//		nextLoc = new MyCoordinate(generalLoc.getRank(), generalLoc.getFile()-1, this);
//		moves.add(new Move(generalLoc, nextLoc, this, player));
//		
//		nextLoc = new MyCoordinate(generalLoc.getRank(), generalLoc.getFile()+1, this);
//		moves.add(new Move(generalLoc, nextLoc, this, player));
//		
//		for(Move m: moves){
//			try{
//				if(m.isValid()){
//					m.doMove();
//					if(!generalInCheck(player)){
//						m.undo();
//						return true;
//					}
//					m.undo();
//				}
//			}
//			catch(CompletionException ce){
//				//do nothing
//			}
//		}
//		return false;
//	}
	
	private boolean canSaveGeneral(MyCoordinate loc, XiangqiColor defendingPlayer){
		for(int r = 0; r < numRanks; r++){
			for(int f = 0; f < numFiles; f++){
				MyCoordinate c = new MyCoordinate(r+1, f+1, this);
				Move m = new Move(loc, c, this, defendingPlayer);
				if(getsGeneralOutOfCheck(m, defendingPlayer)){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean getsGeneralOutOfCheck(Move m, XiangqiColor defendingPlayer){
		if(m.isValid()){
			m.doMove();
			if(!generalInCheck(defendingPlayer)){
				m.undo();
				return true;
			}
			m.undo();
		} return false;
	}
	
	private ArrayList<MyCoordinate> getPiecesOf(XiangqiColor player){
		ArrayList<MyCoordinate> pieceLocs = new ArrayList<MyCoordinate>();
		for(int r = 0; r < getNumRanks(); r++){
			for(int f = 0; f < getNumFiles(); f++){
				if(board[r][f].getPiece().getColor() != XiangqiColor.NONE && board[r][f].getPiece().getColor() == player){
					pieceLocs.add(new MyCoordinate(r+1,f+1,this));
				}
			}
		}
		return pieceLocs;
	}

	private MyCoordinate findGeneral(XiangqiColor player){
		for(int r = 0; r < getNumRanks(); r++){
			for(int f = 0; f < getNumFiles(); f++){
				if(board[r][f].getPiece().getColor() == player && board[r][f].getPiece().getPieceType() == XiangqiPieceType.GENERAL){
					return new MyCoordinate(r+1,f+1,this);
				}
			}
		}
		return null;
	}


	private String getPieceChar(XiangqiPiece p){
		String chars = "";
		switch(p.getColor()){
		case BLACK: chars += "b"; break;
		case RED: chars += "r"; break;
		case NONE: chars += "."; break;
		default: break;
		}
		XiangqiPieceType type = p.getPieceType();
		switch(type){
		case CHARIOT: chars += type.getSymbol(); break;
		case ADVISOR: chars += type.getSymbol(); break;
		case GENERAL: chars += type.getSymbol(); break;
		case SOLDIER: chars += type.getSymbol(); break;
		case ELEPHANT: chars += type.getSymbol(); break;
		case NONE: chars += type.getSymbol(); break;
		default: break;
		}
		return chars;
	}
}
