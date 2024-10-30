package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;
import com.huy.game.chess.manager.ChessGameManager;
import com.huy.game.chess.manager.ChessImage;
import com.huy.game.chess.manager.ChessSound;
import com.huy.game.chess.ui.Colors;
import com.huy.game.chess.ui.SuggestiveArrow;

public class Board {
    private Spot[][] spots = new Spot[8][8];
    private Spot wKingSpot;
    private Spot bKingSpot;
    private Move promotingMove;
    private Move suggestMove = null;
    private PolygonSprite polygonSprite = null;
    private Spot possibleEnPassantTargetsSpot;
    private boolean isPromoting = false;
    private int turn = 0;

    public void setSpots(Spot[][] spots) {
        this.spots = spots;
    }

    public Spot[][] getSpots() {
        return spots;
    }

    public Spot[][] cloneSpots(Spot[][] spots) {
        Spot[][] testSpots = new Spot[8][8];
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                testSpots[i][j] = new Spot(spots[i][j]);
            }
        }
        return testSpots;
    }

    public void setwKingSpot(Spot wKingSpot) {
        this.wKingSpot = wKingSpot;
    }

    public void setbKingSpot(Spot bKingSpot) {
        this.bKingSpot = bKingSpot;
    }

    public Board cloneBoard() {
        Board testBoard = new Board();
        testBoard.setSpots(cloneSpots(spots));
        testBoard.setwKingSpot(testBoard.getSpots()[wKingSpot.getX()][wKingSpot.getY()]);
        testBoard.setbKingSpot(testBoard.getSpots()[bKingSpot.getX()][bKingSpot.getY()]);
        if (possibleEnPassantTargetsSpot != null) {
            testBoard.setPossibleEnPassantTargetsSpot(testBoard.getSpots()[possibleEnPassantTargetsSpot.getX()][possibleEnPassantTargetsSpot.getY()]);
        }
        testBoard.turn=turn;
        return testBoard;
    }

    public void setPromoting(boolean isPromoting) {
        this.isPromoting = isPromoting;
    }

    public boolean isPromoting() {
        return isPromoting;
    }

    public Spot getSpot(int x, int y) {
        return spots[x][y];
    }

    public Move getPromotingMove() {
        return promotingMove;
    }

    public void setPromotingMove(Move promotingMove) {
        this.promotingMove = promotingMove;
    }

    public Spot getPossibleEnPassantTargetsSpot() {
        return possibleEnPassantTargetsSpot;
    }

    public void setPossibleEnPassantTargetsSpot(Spot possibleEnPassantTargetsSpot) {
        this.possibleEnPassantTargetsSpot = possibleEnPassantTargetsSpot;
    }

    public void setSuggestMove(Move suggestMove) {
        this.suggestMove = suggestMove;
    }

    public void setPolygonSprite(PolygonSprite polygonSprite) {
        this.polygonSprite = polygonSprite;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void increaseTurn() {
        turn++;
    }

    public void decreaseTurn() {
        turn--;
    }

    public int getTurn() {
        return turn;
    }

    public void setSpot(int x, int y, Piece piece) {
        spots[x][y].setPiece(piece);
        if(piece instanceof King) {
            if(piece.isWhite()) {
                wKingSpot = spots[x][y];
            }else {
                bKingSpot = spots[x][y];
            }
        }
    }

    public Spot getKingSpot(boolean isWhite) {
        return isWhite ? wKingSpot : bKingSpot;
    }

    public void handleSoundAfterMove(Piece endPiece, Move move, ChessSound chessSound, ChessGameManager chessGameManager) {
        switch (move.getMoveType()) {
            case NORMAL, DOUBLE_STEP_PAWN -> chessSound.playMoveSound();
            case CAPTURE -> {
                chessSound.playCaptureSound();
                chessGameManager.putValue(endPiece.getType());
            }
            case EN_PASSANT -> {
                chessSound.playCaptureSound();
                chessGameManager.putValue(PieceType.PAWN);
            }
            case CASTLING_KING_SIDE, CASTLING_QUEEN_SIDE -> chessSound.playCastleSound();
            case PROMOTE_TO_QUEEN, PROMOTE_TO_KNIGHT, PROMOTE_TO_ROOK, PROMOTE_TO_BISHOP -> {
                chessSound.playPromoteSound();
                chessGameManager.putValueForPromote(switch (move.getMoveType()) {
                    case PROMOTE_TO_QUEEN -> PieceType.QUEEN;
                    case PROMOTE_TO_KNIGHT -> PieceType.KNIGHT;
                    case PROMOTE_TO_ROOK -> PieceType.ROOK;
                    case PROMOTE_TO_BISHOP -> PieceType.BISHOP;
                    default -> throw new IllegalStateException("Unexpected value: " + move.getMoveType());
                });
            }
        }
        if (move.isCheck()) {
            chessSound.playCheckSound();
        }
    }

    public void resetBoard(ChessImage chessImage ) {
        spots[0][0] = new Spot(new Rook(true, chessImage.getwRock()), 0, 0);
        spots[0][1] = new Spot(new Knight(true, chessImage.getwKnight()), 0, 1);
        spots[0][2] = new Spot(new Bishop(true, chessImage.getwBishop()), 0, 2);
        spots[0][3] = new Spot(new Queen(true, chessImage.getwQueen()), 0, 3);
        spots[0][4] = new Spot(new King(true, chessImage.getwKing()), 0, 4);
        wKingSpot = spots[0][4];
        spots[0][5] = new Spot(new Bishop(true, chessImage.getwBishop()), 0, 5);
        spots[0][6] = new Spot(new Knight(true, chessImage.getwKnight()), 0, 6);
        spots[0][7] = new Spot(new Rook(true, chessImage.getwRock()), 0, 7);

        for(int i = 0; i <= 7; i++) {
            spots[1][i] = new Spot(new Pawn(true, chessImage.getwPawn()), 1, i);
        }

        spots[7][0] = new Spot(new Rook(false, chessImage.getbRook()), 7, 0);
        spots[7][1] = new Spot(new Knight(false, chessImage.getbKnight()), 7, 1);
        spots[7][2] = new Spot(new Bishop(false, chessImage.getbBishop()), 7, 2);
        spots[7][3] = new Spot(new Queen(false, chessImage.getbQueen()), 7, 3);
        spots[7][4] = new Spot(new King(false, chessImage.getbKing()), 7, 4);
        bKingSpot = spots[7][4];
        spots[7][5] = new Spot(new Bishop(false, chessImage.getbBishop()), 7, 5);
        spots[7][6] = new Spot(new Knight(false, chessImage.getbKnight()), 7, 6);
        spots[7][7] = new Spot(new Rook(false, chessImage.getbRook()), 7, 7);

        for(int i = 0; i <= 7; i++) {
            spots[6][i] = new Spot(new Pawn(false, chessImage.getbPawn()), 6, i);
        }

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                spots[i][j] = new Spot(null,i, j);
            }
        }
    }

    public void renderBoard(SpriteBatch batch, float spotSize, float pieceSize, float centerX, float centerY, ChessImage chessImage) {
        float padding = spotSize / 10f;
        float x = centerX + padding;
        float y = centerY + padding;
        batch.draw(chessImage.getPosition(), centerX, centerY, chessImage.getScaledBoardWidth(), chessImage.getScaledBoardHeight());
        for(int i = 0; i <= 7; i++) {
            float distanceY = spotSize * i + y;
            for (int j = 0;j <= 7; j++) {
                float distance = spotSize * j;
                if(spots[i][j].getPiece() != null) {
                    batch.draw(spots[i][j].getPiece().getTexture(), x + distance, distanceY, pieceSize, pieceSize);
                }
            }
        }
    }

    public void renderRotateBoard(SpriteBatch batch, float spotSize, float pieceSize, float centerX, float centerY, ChessImage chessImage) {
        float padding = spotSize / 10f;
        float x = centerX + padding;
        float y = centerY + padding;
        batch.draw(chessImage.getRotate_position(), centerX, centerY, chessImage.getScaledBoardWidth(), chessImage.getScaledBoardHeight());
        for(int i = 0; i <= 7; i++) {
            float distanceY = spotSize * i + y;
            for (int j = 0;j <= 7; j++) {
                float distance = spotSize * j;
                if(spots[7- i][7- j].getPiece() != null) {
                    batch.draw(spots[7 -i][7 - j].getPiece().getTexture(), x + distance, distanceY, pieceSize, pieceSize);
                }
            }
        }
    }

    public void renderBoardReverseOneSide(SpriteBatch batch, float spotSize, float originPieceSize , float pieceSize, float centerX, float centerY, ChessImage chessImage) {
        float padding = spotSize / 10f;
        float x = centerX + padding;
        float y = centerY + padding;
        batch.draw(chessImage.getPosition(), centerX, centerY, chessImage.getScaledBoardWidth(), chessImage.getScaledBoardHeight());
        for(int i = 0; i <= 7; i++) {
            float distanceY = spotSize * i + y;
            for (int j = 0;j <= 7; j++) {
                float distance = spotSize * j;
                Piece piece = spots[i][j].getPiece();
                if(piece != null) {
                    if(piece.isWhite()) {
                        batch.draw(piece.getTexture(), x + distance, distanceY, pieceSize, pieceSize);
                    }else {
                        batch.draw(piece.getTexture(), x + distance, distanceY, pieceSize, pieceSize, 0, 0, (int) originPieceSize, (int) originPieceSize, false, true);
                    }
                }
            }
        }
    }

    public void renderRotateBoardReverseOneSide(SpriteBatch batch, float spotSize, float originPieceSize , float pieceSize, float centerX, float centerY, ChessImage chessImage) {
        float padding = spotSize / 10f;
        float x = centerX + padding;
        float y = centerY + padding;
        batch.draw(chessImage.getRotate_position(), centerX, centerY, chessImage.getScaledBoardWidth(), chessImage.getScaledBoardHeight());
        for(int i = 0; i <= 7; i++) {
            float distanceY = spotSize * i + y;
            for (int j = 0;j <= 7; j++) {
                float distance = spotSize * j;
                Piece piece = spots[7 - i][7 - j].getPiece();
                if(piece != null) {
                    if(!piece.isWhite()) {
                        batch.draw(piece.getTexture(), x + distance, distanceY, pieceSize, pieceSize);
                    }else {
                        batch.draw(piece.getTexture(), x + distance, distanceY, pieceSize, pieceSize, 0, 0, (int) originPieceSize, (int) originPieceSize, false, true);
                    }
                }
            }
        }
    }

    public void renderColorAndPoint(ShapeRenderer shapeRenderer, float circlePointRadius, float pieceSide, float spotSize, float centerX, float centerY) {
        float padding = spotSize / 10f;
        float scale = (spotSize - (2 * padding)) / pieceSide;
        float scaledSide = scale * pieceSide;
        for(int i = 0; i <= 7; i++) {
            float distanceY = spotSize * i + centerY;
            for (int j = 0;j <= 7; j++) {
                float distance = spotSize * j;
                if(spots[i][j].isShowColor()) {
                    shapeRenderer.setColor(spots[i][j].getSpotColor());
                    shapeRenderer.rect(centerX + distance, distanceY, spotSize, spotSize);
                }
                if(spots[i][j].isShowMovePoint()) {
                    if(spots[i][j].isCanBeCaptured()) {
                        shapeRenderer.end();
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(centerX + distance + spotSize / 2, distanceY + spotSize / 2, scaledSide / 2);
                        shapeRenderer.end();
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    }else  {
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(centerX + distance + spotSize / 2, distanceY + spotSize / 2, circlePointRadius);
                    }

                }

            }
        }
    }

    public void renderRotateColorAndPoint(ShapeRenderer shapeRenderer, float circlePointRadius, float pieceSide, float spotSize, float centerX, float centerY) {
        float padding = spotSize / 10f;
        float scale = (spotSize - (2 * padding)) / pieceSide;
        float scaledSide = scale * pieceSide;

        for (int i = 7; i >= 0; i--) {
            float distanceY = spotSize * (7 - i) + centerY;
            for (int j = 7; j >= 0; j--) {
                float distance = spotSize * (7 - j);

                if (spots[i][j].isShowColor()) {
                    shapeRenderer.setColor(spots[i][j].getSpotColor());
                    shapeRenderer.rect(centerX + distance, distanceY, spotSize, spotSize);
                }

                if (spots[i][j].isShowMovePoint()) {
                    if (spots[i][j].isCanBeCaptured()) {
                        shapeRenderer.end();
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(centerX + distance + spotSize / 2, distanceY + spotSize / 2, scaledSide / 2);
                        shapeRenderer.end();
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    } else {
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(centerX + distance + spotSize / 2, distanceY + spotSize / 2, circlePointRadius);
                    }
                }
            }
        }
    }


    public void clearColor() {
        for (int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                spots[i][j].setShowColor(false);
                spots[i][j].setIdentificationColor(false);
            }
        }
    }

    public void clearColorAndPoint() {
        for (int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                spots[i][j].setShowColor(false);
                spots[i][j].setIdentificationColor(false);
                spots[i][j].setShowMovePoint(false);
                spots[i][j].setCanBeCaptured(false);
            }
        }
    }

    public void clearGuidePoint() {
        for (int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                spots[i][j].setShowMovePoint(false);
                spots[i][j].setCanBeCaptured(false);
            }
        }
    }

    public boolean isWithinBoard(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public boolean isIndirectCheck(Spot spot, boolean isWhite) {
        Spot king = getKingSpot(!isWhite);
        int distanceX = Math.abs(king.getX() - spot.getX());
        int distanceY = Math.abs(king.getY() - spot.getY());
        if(distanceX == 0 || distanceY == 0 || distanceX == distanceY) {
            int directionX = Integer.signum(spot.getX() - king.getX());
            int directionY = Integer.signum(spot.getY() - king.getY());
            int currentX = spot.getX() + directionX;
            int currentY = spot.getY() + directionY;
            loop:
            while(isWithinBoard(currentX, currentY)) {
                Piece test = spots[currentX][currentY].getPiece();
                if(test != null) {
                    if(test.isWhite() == isWhite) {
                        if (distanceX != 0 && directionY != 0) {
                            switch (test.getType()) {
                                case BISHOP, QUEEN -> {
                                    directionX = -directionX;
                                    directionY = -directionY;
                                    currentX = spot.getX() + directionX;
                                    currentY = spot.getY() + directionY;
                                    while (currentX != king.getX() && currentY != king.getY()) {
                                        if(spots[currentX][currentY].getPiece() != null) {
                                            break loop;
                                        }
                                        currentX += directionX;
                                        currentY += directionY;
                                    }
                                    return true;
                                }
                                default -> {
                                    break loop;
                                }
                            }
                        }else {
                            switch (test.getType()) {
                                case ROOK, QUEEN -> {
                                    directionX = -directionX;
                                    directionY = -directionY;
                                    currentX = spot.getX() + directionX;
                                    currentY = spot.getY() + directionY;
                                    while (currentX != king.getX() || currentY != king.getY()) {
                                        if(spots[currentX][currentY].getPiece() != null) {
                                            break loop;
                                        }
                                        currentX += directionX;
                                        currentY += directionY;
                                    }
                                    return true;
                                }
                                default -> {
                                    break loop;
                                }
                            }
                        }
                    }
                }
                currentX += directionX;
                currentY += directionY;
            }
        }
        return false;
    }

    public boolean isHaveAvailableMove(boolean isWhite) {
        if(canKingMove(isWhite)) {
            return true;
        }else {
            for(int i = 0; i <= 7; i++) {
                for(int j = 0; j <= 7; j++) {
                    Spot checkSpot = spots[i][j];
                    Piece checkPiece = checkSpot.getPiece();
                    if(checkPiece != null) {
                        if(checkPiece.isWhite() == isWhite) {
                            if(checkPiece.calculateMove(this,checkSpot)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean canKingMove(boolean isWhite) {
        Spot kingSpot = getKingSpot(isWhite);
        return kingSpot.getPiece().calculateMove(this, kingSpot);
    }

    public boolean isKingSafe(boolean isWhite) {
        Spot kingSpot = getKingSpot(isWhite);
        int kingX = kingSpot.getX();
        int kingY = kingSpot.getY();
        return isPositionSafe(kingX, kingY, isWhite);
    }

    public boolean isPositionSafe(int positionX, int positionY, boolean isWhite) {
        if(isKingInCheckByKnight(positionX, positionY, isWhite)) {
            return false;
        }

        for(int i = -1; i <= 1; i++) {
            int x = positionX + i;
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int y = positionY + j;
                if(isWithinBoard(x, y)) {
                    Piece checkPiece = spots[x][y].getPiece();
                    if(checkPiece != null) {
                        if(checkPiece.isWhite() != isWhite) {
                            if(i != 0 && j != 0) {
                                switch (checkPiece.getType()) {
                                    case BISHOP, QUEEN, PAWN, KING -> {
                                        return false;
                                    }
                                }
                            }else {
                                switch (checkPiece.getType()) {
                                    case ROOK, QUEEN -> {
                                        return false;
                                    }
                                }
                            }
                        }
                    }else {
                        int coordinatesX = x + i;
                        int coordinatesY = y + j;
                        loop:
                        while(isWithinBoard(coordinatesX, coordinatesY)) {
                            Piece testPiece = spots[coordinatesX][coordinatesY].getPiece();
                            if(testPiece != null) {
                                if(testPiece.isWhite() == isWhite) {
                                    break;
                                }else {
                                    if(i != 0 && j != 0) {
                                        switch (testPiece.getType()) {
                                            case BISHOP, QUEEN -> {
                                                return false;
                                            }
                                            default -> {
                                                break loop;
                                            }
                                        }
                                    }else {
                                        switch (testPiece.getType()) {
                                            case ROOK, QUEEN -> {
                                                return false;
                                            }
                                            default -> {
                                                break loop;
                                            }
                                        }
                                    }
                                }
                            }
                            coordinatesX += i;
                            coordinatesY += j;
                        }
                    }
                }

            }
        }
        return true;
    }

    public boolean isKingInCheckByKnight(int positionX, int positionY, boolean isWhite) {
        for (int[] move : Knight.knightMoves()) {
            int x = positionX + move[0];
            int y = positionY + move[1];
            if(isWithinBoard(x, y)) {
                Piece checkPiece = spots[x][y].getPiece();
                if(checkPiece != null) {
                    if(checkPiece.getType() == PieceType.KNIGHT && checkPiece.isWhite() != isWhite) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void warnIllegalMove(boolean isWhite) {
        Timer.schedule(new Timer.Task() {
            int count = 1;
            @Override
            public void run() {
                if(count++ <= 5) {
                    Spot kingSpot = getKingSpot(isWhite);
                    if(kingSpot.isShowColor()) {
                        kingSpot.setShowColor(false);
                    }else {
                        kingSpot.setShowColor(true);
                        kingSpot.setSpotColor(Colors.ILLEGAL_MOVE);
                    }
                }else {
                    this.cancel();
                }
            }
        }, 0, 0.5f);
    }

    public void handlePawnPromotion(int boardX, int boardY, BoardSetting setting) {
        isPromoting = false;
        int promoteY = promotingMove.getEnd().getY();
        if(setting.isRotate()) {
            promoteY = 7 - promoteY;
        }
        boolean isWhite = promotingMove.getStart().getPiece().isWhite();
        if(boardX != promoteY) {
            promotingMove = null;
            clearColor();
        }else {
            if((isWhite && !setting.isRotate()) || (!isWhite && setting.isRotate())) {
                switch (boardY) {
                    case 4 -> promotingMove.setMoveType(MoveType.PROMOTE_TO_ROOK);
                    case 5 -> promotingMove.setMoveType(MoveType.PROMOTE_TO_BISHOP);
                    case 6 -> promotingMove.setMoveType(MoveType.PROMOTE_TO_KNIGHT);
                    case 7 -> promotingMove.setMoveType(MoveType.PROMOTE_TO_QUEEN);
                    default -> {
                        promotingMove.setMoveType(MoveType.PROMOTE);
                        clearColor();
                    }
                }
            }else {
                switch (boardY) {
                    case 0 -> promotingMove.setMoveType(MoveType.PROMOTE_TO_ROOK);
                    case 1 -> promotingMove.setMoveType(MoveType.PROMOTE_TO_BISHOP);
                    case 2 -> promotingMove.setMoveType(MoveType.PROMOTE_TO_KNIGHT);
                    case 3 -> promotingMove.setMoveType(MoveType.PROMOTE_TO_QUEEN);
                    default -> {
                        promotingMove.setMoveType(MoveType.PROMOTE);
                        clearColor();
                    }
                }
            }

        }
    }

    public void showPromoteSelection(SpriteBatch batch,ShapeRenderer shapeRenderer,float centerX, float centerY, ChessImage chessImage, BoardSetting setting) {
        float padding = chessImage.getSpotSize() / 10f;
        float scale = (chessImage.getSpotSize() - (2 * padding)) / chessImage.getPieceSize();
        float scaledSide = scale * chessImage.getPieceSize();
        int spotX, spotY;
        if(setting.isRotate()) {
            spotX = 7 - promotingMove.getEnd().getX();
            spotY = 7 - promotingMove.getEnd().getY();
        }else {
            spotX = promotingMove.getEnd().getX();
            spotY = promotingMove.getEnd().getY();
        }
        float x = centerX + chessImage.getSpotSize() * spotY ;
        float y;
        if((promotingMove.getEnd().getX() == 7 && !setting.isRotate()) || (promotingMove.getEnd().getX() == 0 && setting.isRotate()) ) {
            y = centerY + chessImage.getSpotSize() * spotX - 3 * chessImage.getSpotSize();
        }else {
            y = centerY;
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x, y, chessImage.getSpotSize(), chessImage.getSpotSize() * 4);
        shapeRenderer.end();
        batch.begin();
        if (promotingMove.getStart().getPiece().isWhite()) {
            batch.draw(chessImage.getwRock(), x + padding, y + padding , scaledSide, scaledSide);
            batch.draw(chessImage.getwBishop(), x + padding, y + padding + chessImage.getSpotSize() , scaledSide, scaledSide);
            batch.draw(chessImage.getwKnight(), x + padding, y + padding + chessImage.getSpotSize() * 2, scaledSide, scaledSide);
            batch.draw(chessImage.getwQueen(), x + padding, y + padding + chessImage.getSpotSize() * 3, scaledSide, scaledSide);
        }else {
            batch.draw(chessImage.getbRook(), x + padding, y + padding , scaledSide, scaledSide);
            batch.draw(chessImage.getbBishop(), x + padding, y + padding + chessImage.getSpotSize() , scaledSide, scaledSide);
            batch.draw(chessImage.getbKnight(), x + padding, y + padding + chessImage.getSpotSize() * 2, scaledSide, scaledSide);
            batch.draw(chessImage.getbQueen(), x + padding, y + padding + chessImage.getSpotSize() * 3, scaledSide, scaledSide);
        }
        batch.end();
    }

    public void renderSuggestiveMove(PolygonSpriteBatch batch, float centerX, float centerY, float spotSide ) {
        if (suggestMove != null) {
            batch.begin();
            int radian;
            float height;
            Spot start = suggestMove.getStart();
            Spot end = suggestMove.getEnd();
            float distance = spotSide / 2;
            if(suggestMove.getStartPiece().getType() != PieceType.KNIGHT) {
                int x = Math.abs(end.getX() - start.getX());
                int y = Math.abs(end.getY() - start.getY());
                if (x == y) {
                    int directionX = Integer.signum(end.getX() - start.getX());
                    int directionY = Integer.signum(end.getY() - start.getY());
                    if (directionX == 1 && directionY == 1) {
                        radian = -45;
                    }else if (directionX == 1 && directionY == -1) {
                        radian = 45;
                    }else if (directionX == -1 && directionY == -1) {
                        radian = 135;
                    }else {
                        radian = -135;
                    }
                    height = (float) (x * Math.sqrt(2));
                }else if (x == 0 && y != 0) {
                    int directionY = Integer.signum(end.getY() - start.getY());
                    radian = directionY > 0 ? - 90 : 90;
                    height = y;
                }else {
                    int directionX = Integer.signum(end.getX() - start.getX());
                    radian = directionX > 0 ? 0 : 180;
                    height = x;
                }
                polygonSprite = SuggestiveArrow.createArrow(height * spotSide, radian, spotSide, centerX + distance + start.getY() * spotSide - 25, centerY + distance + start.getX() * spotSide);
            }else {
                boolean isReserve = false;
                int x = end.getX() - start.getX();
                int y = end.getY() - start.getY();
                if (x == 1 && y == -2) {
                    radian = 0;
                }else if (x == -1 && y == -2) {
                    radian = 180;
                    isReserve = true;
                }else if (x == -2 && y == -1) {
                    radian = 90;
                }else if (x == -2 && y == 1) {
                    radian = -90;
                    isReserve = true;
                }else if (x == -1 && y == 2) {
                    radian = 180;
                }else if (x == 1 && y == 2) {
                    radian = 0;
                    isReserve = true;
                }else if (x == 2 && y == 1) {
                    radian = -90;
                }else {
                    radian = 90;
                    isReserve = true;
                }
                if(isReserve) {
                    polygonSprite = SuggestiveArrow.createArrowForKnightReverse(radian, spotSide, centerX + distance + start.getY() * spotSide - 15, centerY + distance + start.getX() * spotSide);
                }else {
                    polygonSprite = SuggestiveArrow.createArrowForKnight(radian, spotSide, centerX + distance + start.getY() * spotSide - 15, centerY + distance + start.getX() * spotSide);
                }
            }
            polygonSprite.draw(batch);
            batch.end();
        }
    }

    public void renderSuggestiveMoveForRotateBoard( PolygonSpriteBatch batch, float centerX, float centerY, float spotSide ) {
        if (suggestMove != null) {
            batch.begin();
            int radian;
            float height;
            Spot start = suggestMove.getStart();
            Spot end = suggestMove.getEnd();
            float distance = spotSide / 2;
            if(suggestMove.getStartPiece().getType() != PieceType.KNIGHT) {
                int x = Math.abs(start.getX() - end.getX());
                int y = Math.abs(start.getY() - end.getY());
                if (x == y) {
                    int directionX = Integer.signum(start.getX() - end.getX());
                    int directionY = Integer.signum(start.getY() - end.getY());
                    if (directionX == 1 && directionY == 1) {
                        radian = -45;
                    }else if (directionX == 1 && directionY == -1) {
                        radian = 45;
                    }else if (directionX == -1 && directionY == -1) {
                        radian = 135;
                    }else {
                        radian = -135;
                    }
                    height = (float) (x * Math.sqrt(2));
                }else if (x == 0 && y != 0) {
                    int directionY = Integer.signum(start.getY() - end.getY());
                    radian = directionY > 0 ? - 90 : 90;
                    height = y;
                }else {
                    int directionX = Integer.signum(start.getX() - end.getX());
                    radian = directionX > 0 ? 0 : 180;
                    height = x;
                }
                polygonSprite = SuggestiveArrow.createArrow(height * spotSide, radian, spotSide, centerX + distance + (7 - start.getY()) * spotSide - 25, centerY + distance + (7 - start.getX()) * spotSide);
            }else {
                boolean isReserve = false;
                int x = start.getX() - end.getX();
                int y = start.getY() - end.getY();
                if (x == 1 && y == -2) {
                    radian = 0;
                }else if (x == -1 && y == -2) {
                    radian = 180;
                    isReserve = true;
                }else if (x == -2 && y == -1) {
                    radian = 90;
                }else if (x == -2 && y == 1) {
                    radian = -90;
                    isReserve = true;
                }else if (x == -1 && y == 2) {
                    radian = 180;
                }else if (x == 1 && y == 2) {
                    radian = 0;
                    isReserve = true;
                }else if (x == 2 && y == 1) {
                    radian = -90;
                }else {
                    radian = 90;
                    isReserve = true;
                }
                if(isReserve) {
                    polygonSprite = SuggestiveArrow.createArrowForKnightReverse(radian, spotSide, centerX + distance + (7 - start.getY()) * spotSide - 15, centerY + distance + (7 - start.getX()) * spotSide);
                }else {
                    polygonSprite = SuggestiveArrow.createArrowForKnight(radian, spotSide, centerX + distance + (7 - start.getY()) * spotSide - 15, centerY + distance + (7 - start.getX()) * spotSide);
                }
            }
            polygonSprite.draw(batch);
            batch.end();
        }
    }
}
