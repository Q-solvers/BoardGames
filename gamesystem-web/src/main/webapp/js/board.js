function updateHistory() {
    if (initial) {
        initial = false;
        return;
    }
    var hu = "${url-base}/chess/chessgame.chessboard:uphistory?t:ac=" + gameId;
    new Ajax.Request(hu, {
        parameters: '',
        method: 'post',
        onSuccess: function(resp) {
            var result = resp.responseJSON;
            if (result != null) {
                if (result.finish == "true") {
                    location.reload(true);
                    return;
                }
                var moveNumber = result.number;
                var moveText = result.moveText;
                var moveTr = document.getElementById("move" + moveNumber);
                update(moveTr, moveNumber, moveText);
            }
            return;
        },
        onFailure : function(resp) {
            //             alert(resp);
        }
    });
    return;
}

function update(moveTr, moveNumber, moveText) {
    var moveTextTd = document.createElement("td");
    var text = document.createTextNode(moveText);
    moveTextTd.appendChild(text);
    if (moveTr) {
        //                                moveTr.innerHTML = moveTr.innerHTML + "<td>" + moveText + "</td>";
        moveTr.appendChild(moveTextTd);
    } else {
        var table = document.getElementById("gameshistory");
        var moveTextTr = document.createElement("tr");
        var moveNumberTd = document.createElement("td");
        var numver = document.createTextNode(moveNumber);
        moveTextTr.setAttribute("id", "move" + moveNumber);
        moveNumberTd.appendChild(numver);
        moveTextTr.appendChild(moveNumberTd);
        moveTextTr.appendChild(moveTextTd);
        table.appendChild(moveTextTr);
        //                                table.innerHTML = table.innerHTML + "<tr id='move" + moveNumber + "'>"
        //                                        + "<td>" + moveNumber + ". </td>"
        //                                        + "<td>" + moveText + "</td>" + "</tr>";
        var hist = document.getElementById("div-gamehistory");
        hist.scrollTop = hist.scrollHeight;
    }
}