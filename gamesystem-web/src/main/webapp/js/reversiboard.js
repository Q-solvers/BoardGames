var selected = null;
var board = new Object();
var canMove = false;
var inMove = false;
var inReq = false;
var gameId = null;
var stepNum = 1;

function checkForCorrectness(url) {
    //    if(inMove)
    //        return;
    //    inMove=true;
//    if (selected == null)
//        return;
    // create url to move event handler onMove(position, piece)
//    url = url.replace("?t:", "/" + selected.id + "?t:");
    if(!canMove) {
        return;
    }
    new Ajax.Request(url, {
        parameters: '',
        method: 'post',
        onSuccess: function(resp) {
            try {
                var result = resp.responseJSON;
                if (result != null && result.content) {
                    document.getElementById("boardBlock").innerHTML = result.content;
                    canMove=false;
                }
//                if (result != null && result.length != 0 && !isPawnExchange(result)) {
//                    for (var i = 0; i < result.length; i++) {
//                        var pieceId = result[i].id;
//                        var piecePosition = result[i].position;
//                        var pieceType = result[i].pieceType;
//                        updatePiece(pieceId, piecePosition, pieceType);
//                    }
//                    canMove = false;
//                    inMove = false;
//                    updateHistory();
//                }
//                if (isPawnExchange(result)) {
//                    pawnExchangeId = result[1].id;
//                    pawnExchangePosition = result[1].position;
//                    exchKill = result[1].kill;
//                    showPawnExchange('visible', 'hidden');
//                }
                unsetBorder(selected);
            } finally {
                inMove = false;
            }
        },
        onFailure : function(resp) {
            //alert(resp);
            inMove = false;
        }
    });
    inMove = false;
}

function echo(url) {
    url = url.replace("?t:", "/" + canMove + "/" + stepNum + "?t:");
    if (!inReq) {
        inReq = true;
        new Ajax.Request(url, {
            parameters: '',
            method: 'post',
            onSuccess: function(resp) {
                try {
                    var res = resp.responseJSON;
                    if (res != null) {
                        if (res.content && !res.length) {
                            // update board
                            document.getElementById("boardBlock").innerHTML = res.content;
                            // undone draw
                            document.getElementById("draw").style.visibility = 'hidden';
                            document.getElementById("suggest").style.visibility = 'visible';
                            // enable movement
                            canMove = document.getElementById("hidden_move").value == 1;
                            stepNum = document.getElementById("hidden_step").value;
                            var hist = document.getElementById("div-gamehistory");
                            hist.scrollTop = hist.scrollHeight;
                        } // drow proposed
                        else if (res.length && (res.length != 0)) {
                            var result = res[0].split(",");
                            //game over
                            if (result[0] == 999) {
                                location.reload(true);
                            } // enemy has done movement
                            else if (result[0] == 2) {
                                if (result[1] == 1) {
                                    document.getElementById("suggest").style.visibility = 'hidden';
                                } else {
                                    document.getElementById("suggest").style.visibility = 'visible';
                                }
                                if (result[2] == 1) {
                                    document.getElementById("draw").style.visibility = 'hidden';
                                    document.getElementById("suggest").style.visibility = 'hidden';
                                }
                                if (result[1] == 1) {
                                    document.getElementById("draw").style.visibility = 'visible';
                                }
                            } else if (result[0] == 3) {
                                canMove = true;
                            }
                            // nothing happens
                            else if (result[0] == 0) {
                                    document.getElementById("draw").style.visibility = 'hidden';
                                    document.getElementById("suggest").style.visibility = 'visible';
                                }
                        }
                    }
                } catch(e)  {
//                   alert(e.message);
                }finally {
                    inReq = false;
                }
            },
            onFailure : function(resp) {
                inReq = false;
//                alert(resp);
            }
        });
    }
}