// TODO: Make more efficient. Currently complete board is re-rendered, if one cell is reveal
function renderGame(game) {
    const gameContainer = document.getElementById("game-container");

    if (!game || game.length === 0) {
        console.error('Game data is empty or undefined');
        return;
    }

    console.log(game);
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
            cellDiv.addEventListener("click", () => revealCell(colIndex, rowIndex));
            rowDiv.appendChild(cellDiv);
        });
        gameContainer.appendChild(rowDiv);
    });
}

function revealCell(x, y) {
    console.log(`Revealing cell at (${x}, ${y})`);
    fetch(`/api/game/reveal?column=${x}&row=${y}`, {
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            renderGame(data.board);
        });
}

function startGame() {
    const columns = document.getElementById('columns').value;
    const rows = document.getElementById('rows').value;
    const mines = document.getElementById('mines').value;

    fetch(`/api/game/start?columns=${columns}&rows=${rows}&mines=${mines}`, {
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            renderGame(data.board);
        })
        .catch(error => console.error('Error:', error));
}

document.addEventListener("DOMContentLoaded", function () {
    startGame();
});