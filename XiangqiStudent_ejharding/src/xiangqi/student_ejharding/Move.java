package xiangqi.student_ejharding;

import java.util.concurrent.CompletionException;

import xiangqi.common.MoveResult;
import xiangqi.common.XiangqiColor;
import xiangqi.common.XiangqiCoordinate;
import xiangqi.common.XiangqiPiece;
import xiangqi.common.XiangqiPieceType;
/**
 * class to handle making moved in the game
 * @author Rett
 *
 */
public class Move {

	private MyCoordinate destination;
	private MyCoordinate source;
	private XiangqiPieceImpl piece;
	private XiangqiPieceImpl destPiece;
	private Board board;
	private String message;
	private XiangqiColor player;

	public Move(XiangqiCoordinate source, XiangqiCoordinate dest, Board board, XiangqiColor player){
		this.source = MyCoordinate.copyCoordinate(source, board);
		this.destination = MyCoordinate.copyCoordinate(dest, board);
		this.piece = new XiangqiPieceImpl(board.getPieceAt(source));
		this.destPiece = new XiangqiPieceImpl(board.getPieceAt(dest));
		this.board = board;
		this.message = "";
		this.player = player;
	}
	
	public boolean isValid(){
		//boolean inCheckToStart = board.generalInCheck(player);
		
		if(!board.isValidLocation(source) || !board.isValidLocation(destination)){
			this.message = "That's not a location on the current board";
			throw new CompletionException(new RuntimeException());
		}
		
		//will return false for none-type
		if(piece.getColor() == XiangqiColor.NONE){
			this.message = "There's no piece there to move";
			return false;
		}			
		
		if(piece.getColor() != player){
			this.message = "You cannot move your opponent's pieces!";
			return false;
		}
		
		if(piece.getColor().equals(destPiece.getColor())){
			this.message = "You can't capture your own piece!";
			return false;
		}
		
		if(source.getRank() <= 5 && destination.getRank() >= 6 ||
				source.getRank() >= 6 && destination.getRank() <=5){
			if(piece.getPieceType().equals(XiangqiPieceType.ELEPHANT)){
				this.message = "Elephant's can't cross the river!";
				return false;
			}
		}
		
		if(source.isAcrossRiver(player)){
			if(piece.getPieceType() == XiangqiPieceType.SOLDIER){
				piece.moveValidators.remove(MoveValidatorFactory.verticalValidator);
				piece.moveValidators.add(MoveValidatorFactory.orthogonalValidator);
			}
		}
		
		return piece.isValid(source, destination);
	}
	
	public String getMessage(){
		return this.message;
	}

	public boolean undo(){
		board.placePieceAt(destPiece, destination);
		board.placePieceAt(piece, source);
		return true;
	}
	
	public MoveResult doMove(){
		if(board.placePieceAt(piece, destination)){
			board.removePieceFrom(source);
			this.message = "After Move:\n" + board.toString();
			return MoveResult.OK;
		}
//		board.placePieceAt(destPiece, destination);
//		board.placePieceAt(piece, source);
//		this.message = "YOU CAN'T DO THAT!!!!";
		return MoveResult.ILLEGAL; 
	}
	
//	@Override
//	public String toString(){
//		return "Move " + piece.toString() + " from " + source.toString() + " to " + destination.toString();
//	}
}
