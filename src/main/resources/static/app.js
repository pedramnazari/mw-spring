function renderGame(game) {
    const gameContainer = document.getElementById("game-container");

    if (!game || game.length === 0) {
        console.error('Game data is empty or undefined');
        return;
    }

    console.log(game); // Ausgabe des Spielfelds
    gameContainer.innerHTML = "";
    game.forEach((row, rowIndex) => {
        const rowDiv = document.createElement("div");
        rowDiv.className = "row";
        row.forEach((cell, colIndex) => {
            const cellDiv = document.createElement("div");
            cellDiv.className = "cell";
            if (cell.revealed) {
                cellDiv.classList.add("revealed");
                if (cell.mine) {
                    cellDiv.classList.add("mine");
                    cellDiv.innerHTML = "💣";
                } else if (cell.adjacentMines > 0) {
                    cellDiv.classList.add(`cell-${cell.adjacentMines}`);
                    cellDiv.innerHTML = cell.adjacentMines;
                }
            }
            cellDiv.addEventListener("click", () => revealCell(rowIndex, colIndex));
            rowDiv.appendChild(cellDiv);
        });
        gameContainer.appendChild(rowDiv);
    });
}

function revealCell(x, y) {
    console.log(`Revealing cell at (${x}, ${y})`); // Logge die Koordinaten
    fetch(`/api/game/reveal?row=${x}&column=${y}`, {
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            renderGame(data.board);
        });
}

function startGame() {
    const rows = document.getElementById('rows').value;
    const columns = document.getElementById('columns').value;
    const mines = document.getElementById('mines').value;

    fetch(`/api/game/start?rows=${rows}&columns=${columns}&mines=${mines}`, {
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            console.log(data); // Ausgabe der empfangenen Daten
            renderGame(data.board);
        })
        .catch(error => console.error('Error:', error));
}

document.addEventListener("DOMContentLoaded", function() {
    // Initialisiere das Spiel beim Laden der Seite
    startGame();
});
