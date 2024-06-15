var stompClient = null;
var playerName = null;

function joinGame() {
    playerName = document.getElementById("playerName").value;
    if (!playerName) {
        alert("Please enter your name");
        return;
    }

    var socket = new SockJS('/random-number');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/status', function (message) {
            showStatus(message.body);
        });

        stompClient.subscribe('/topic/numbers', function (message) {
            var response = JSON.parse(message.body);
            if (response.player === playerName) {
                showNumber(response.randomNumber);
                showScore(response.score);
                if (response.locked) {
                    document.querySelector('button[onclick="generateNumber()"]').disabled = true;
                    document.querySelector('button[onclick="lockScore()"]').disabled = true;
                }
            }
        });

        stompClient.subscribe('/topic/scores', function (message) {
            showScores(JSON.parse(message.body));
        });

        stompClient.subscribe('/topic/winner', function (message) {
            showWinner(JSON.parse(message.body));
        });

        stompClient.send("/app/join", {}, JSON.stringify({'player': playerName}));
        document.getElementById("gameControls").style.display = "block";
    });
}

function generateNumber() {
    stompClient.send("/app/generate", {}, JSON.stringify({'player': playerName}));
}

function lockScore() {
    stompClient.send("/app/lock", {}, JSON.stringify({'player': playerName}));
}

function showNumber(number) {
    document.getElementById("random-number").innerText = "Random Number: " + number;
}

function showScore(score) {
    document.getElementById("score").innerText = "Score: " + score;
}

function showStatus(message) {
    document.getElementById("status").innerText = message;
}

function showScores(scores) {
    var scoreBoard = "Scores:\n";
    for (var player in scores) {
        scoreBoard += player + ": " + scores[player] + "\n";
    }
    document.getElementById("status").innerText = scoreBoard;
}


function showWinner(winner) {
    document.getElementById("winner").innerText = "Winner: " + winner;
}
