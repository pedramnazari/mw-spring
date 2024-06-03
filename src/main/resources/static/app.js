document.addEventListener("DOMContentLoaded", function() {
    const gameContainer = document.getElementById("game-container");

    // Initialisiere das Spiel
    fetch("/api/game/start?rows=10&columns=10&mines=70", {
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            console.log(data); // Ausgabe der empfangenen Daten
            renderBoard(data.cells);
        });

    function renderBoard(board) {
        console.log(board); // Ausgabe des Spielfelds
        gameContainer.innerHTML = "";
        board.forEach((row, rowIndex) => {
            const rowDiv = document.createElement("div");
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
                renderBoard(data.cells);
            });
    }
});
