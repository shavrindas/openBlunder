const container = document.querySelector(".container");
const knock = document.querySelector(".knock-message");
const guide = document.querySelector(".guide-message");
const board = document.querySelector(".chess-board");

let opened = false;
let knockTimer = null;
let guideTimer = null;
let knockInterval = null;

function playKnock() {
  knock.classList.remove("active");
  void knock.offsetWidth; // 리플로우 강제
  knock.classList.add("active");
}

function startKnocking() {
  knockTimer = setTimeout(() => {
    playKnock();
    knockInterval = setInterval(playKnock, 5000);
  }, 3000);
}

function stopKnocking() {
  clearTimeout(knockTimer);
  clearInterval(knockInterval);
  knock.style.display = "none";
}

function showGuideMessage() {
  guide.style.display = "block"; 
  guide.classList.add("active");
}

function showGuide() {
  guideTimer = setTimeout(showGuideMessage, 15000);
}

function removeGuide() {
  clearTimeout(guideTimer);
  guide.classList.remove("active");
  guide.style.display = "none";
}

function openCurtains() {
  if (!opened) {
    container.classList.add("open");
    opened = true;
    stopKnocking();
    removeGuide();
    generateChessBoard();
  }
}

function generateChessBoard() {
  const whitePieces = ['♔', '♕', '♖', '♗', '♘', '♙'];
  const blackPieces = ['♚', '♛', '♜', '♝', '♞', '♟'];

  const pieceLimits = {
    '♔': 1, '♕': 1, '♖': 2, '♗': 2, '♘': 2, '♙': 8,
    '♚': 1, '♛': 1, '♜': 2, '♝': 2, '♞': 2, '♟': 8
  };

  const totalPieces = Math.floor(Math.random() * 6) + 7; // 7 ~ 12
  const numWhite = Math.floor(totalPieces / 2);
  const numBlack = totalPieces - numWhite;

  const grid = [];
  for (let i = 0; i < 64; i++) grid.push(null);

  function placePieces(count, isWhite) {
    const selected = {};
    let placed = 0;

    while (placed < count) {
      const idx = Math.floor(Math.random() * 64);
      if (grid[idx] !== null) continue;

      const pool = isWhite ? whitePieces : blackPieces;
      const piece = pool[Math.floor(Math.random() * pool.length)];

      if (!selected[piece]) selected[piece] = 0;
      if (selected[piece] >= pieceLimits[piece]) continue;

      grid[idx] = piece;
      selected[piece]++;
      placed++;
    }
  }

  placePieces(numWhite, true);
  placePieces(numBlack, false);

  // 체스보드 렌더링
  board.innerHTML = "";
  for (let i = 0; i < 64; i++) {
    const cell = document.createElement("div");
    cell.classList.add("cell");
    const row = Math.floor(i / 8);
    const col = i % 8;
    const isWhite = (row + col) % 2 === 0;
    cell.classList.add(isWhite ? "white" : "black");

    if (grid[i]) {
  cell.textContent = grid[i];
  // 기물이 흰색이면 검은 글자, 검은색이면 흰 글자
  if (['♔','♕','♖','♗','♘','♙'].includes(grid[i])) {
    cell.style.color = cell.classList.contains('white') ? '#000' : '#fff';
  } else {
    cell.style.color = cell.classList.contains('white') ? '#000' : '#fff';
  }
}
    board.appendChild(cell);
  }
}

window.addEventListener("dblclick", openCurtains);
window.addEventListener("scroll", () => {
  if (!opened && window.scrollY > 10) {
    openCurtains();
  }
});

startKnocking();
showGuide()

