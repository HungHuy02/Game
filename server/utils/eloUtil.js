const ratingFactor = (currentElo) => {
    return 10 ** (currentElo / 400);
}

const expectedProbability = (currentEloA, currentEloB) => {
    var ratingFactorA = ratingFactor(currentEloA);
    var ratingFactorB = ratingFactor(currentEloB);
    var expectedProbabilityA = ratingFactorA / (ratingFactorA + ratingFactorB);
    return {
        expectedProbabilityA: expectedProbabilityA,
        expectedProbabilityB: 1 - expectedProbabilityA
    };
} 

const kFactor = (elo) => {
    if(elo < 1600) {
        return 25;
    }else if(elo < 2000) {
        return 20;
    }else if(elo < 2400) {
        return 15;
    }else {
        return 10;
    }
}

const newElo = (currentEloA, currentEloB, result, player1PieceColor) => {
    var k = kFactor(currentEloA);
    var outcome; 
    if(result === "white") {
        outcome = player1PieceColor ? 1 : 0;
    }else if(result === "black") {
        outcome = player1PieceColor ? 0 : 1;
    }else  {
        outcome = 0.5;
    }
    var {expectedProbabilityA, expectedProbabilityB} = expectedProbability(currentEloA, currentEloB);
    var newEloA = currentEloA + k * (outcome - expectedProbabilityA);
    const newEloB = currentEloB + k * ((1 - outcome) - expectedProbabilityB); 
    return {
        newEloA: newEloA,
        newEloB: newEloB
    };
}

module.exports = {
    newElo
}