var selected = null;
var board = new Object();
var canMove = false;
var inMove = false;
var inReq = false;
var gameId = null;
var initial = false;
var pawnExchangePosition = null;
var pawnExchangeId = null;
var exchKill = false;

function pieceSelect(obj) {
    if (!canMove) {
        return;
    }
    if (selected == obj) {
        unsetBorder(obj);
        selected = null;
    } else {
        if (selected != null) {
            unsetBorder(selected);
        }
        selected = obj;
        setBorder(obj);
    }
}

function checkForCorrectness(url) {
    //    if(inMove)
    //        return;
    //    inMove=true;
    if (selected == null)
        return;
    // create url to move event handler onMove(position, piece)
    url = url.replace("?t:", "/" + selected.id + "?t:");
    new Ajax.Request(url, {
        parameters: '',
        method: 'post',
        onSuccess: function(resp) {
            //            var result = resp.responseJSON.position;
            //            if(result!=null) {
            //                selected.className = result.substring(0, 1) + " l" + result.substring(1, 2);
            //            }
            try {
                var result = resp.responseJSON;
                if (result != null && result.length != 0 && !isPawnExchange(result)) {
                    for (var i = 0; i < result.length; i++) {
                        var pieceId = result[i].id;
                        var piecePosition = result[i].position;
                        var pieceType = result[i].pieceType;
                        updatePiece(pieceId, piecePosition, pieceType);
                    }
                    canMove = false;
                    inMove = false;
                    updateHistory();
                }
                if (isPawnExchange(result)) {
                    pawnExchangeId = result[1].id;
                    pawnExchangePosition = result[1].position;
                    exchKill = result[1].kill;
                    showPawnExchange('visible', 'hidden');
                }
                unsetBorder(selected);
                selected = null;
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
function isPawnExchange(respJSON) {
    if (respJSON[0].pawnExchange)
        return true;
    else
        return false;
}

function showPawnExchange(pawnExchangeVisibility, chessBoardVisibility) {
    var pawnExchangeBlock = document.getElementById("pawnExchangeBlock");
    pawnExchangeBlock.style.visibility = pawnExchangeVisibility;
}

function chooseExchangeFigure(url) {
    url = url.replace("?t:", "/" + pawnExchangeId + "/" + pawnExchangePosition+"/"+ exchKill + "?t:");
    new Ajax.Request(url, {
        parameters: '',
        method: 'post',
        onSuccess: function(resp) {
            var result = resp.responseJSON;
            try {
                if (result != null && result.length != 0) {
                    for (var i = 0; i < result.length; i++) {
                        var pieceId = result[i].id;
                        var piecePosition = result[i].position;
                        var pieceType = result[i].pieceType;
                        updateExchangePiece(pieceId, piecePosition, pieceType);
                        //                    pawnExchangeId = null;
                        //                    pawnExchangePosition = null;
                        //                    exchKill = false;
                    }
                    updateHistory();
                    showPawnExchange('hidden', 'visible');
                }
            } catch(e) {
                throw e;
            } finally {
                pawnExchangeId = null;
                pawnExchangePosition = null;
                exchKill = false;
                canMove = false;
                inMove = false;
            }
        },
        onFailure : function(resp) {
//            alert(resp);
            inMove = false;
        }
    });
}

function updateExchangePiece(pieceId, piecePosition, pieceType) {
    var piece = document.getElementById(pieceId);
    if (piecePosition == "kill") {
        piece.className = "killed";
        piece.innerHTML = "";
    }
    else {
        piece.className = piecePosition.substring(0, 1) + " l" + piecePosition.substring(1, 2);
        var imgTag = piece.childNodes.item(0);
        imgTag.src = figureTypes[pieceType];
    }
}

function transformSrc(src, fileName) {
    var addrArray = src.split("/");
    var newSrc = "";
    addrArray[addrArray.length - 1] = fileName;
    newSrc = addrArray.join("/");
    return newSrc;
}

function movePiece(div) {
    if (selected == null)
        return;
    selected.className = "piece " + div.id.substring(0, 1) + " l" + div.id.substring(1, 2);
    unsetBorder(selected);
    selected = null;
}

function unsetBorder(div) {
    //    div.style.borderStyle = "none";
    //    div.borderWidth= "0px";
    //    div.borerColor = "#FFFFFF";
    //div.style.backgroundColor = "transparent";
    new Effect.Opacity(div.id, { from: 0.5, to: 1 });
}
function setBorder(div) {
    try {
        new Effect.Opacity(div.id, { from: 1, to: 0.5 });
    } catch (e) {
//        alert(e);
    }
}

function echo(url) {
    url = url.replace("?t:", "/" + canMove + "?t:");
    if (!inReq) {
        inReq = true;
        new Ajax.Request(url, {
            parameters: '',
            method: 'post',
            onSuccess: function(resp) {
                try {
                    var res = resp.responseJSON;
                    if (res != null && res.length != 0) {
                        var result = res[0].split(",");
                        //game over
                        if (result[0] == 999) {
                            location.reload(true);
                        } // enemy has done movement
                        else if (result[0] == 1) {
                            // update history
                            var moveNumber = result[1];
                            var moveText = result[2];
                            var moveTr = document.getElementById("move" + moveNumber);
                            update(moveTr, moveNumber, moveText);
                            // move pieces
                            for (var i = 3; i < result.length; i+=3) {
                                var piecePosition = result[i];
                                var pieceId = result[i+1];
                                var pieceType = result[i+2];
                                updatePiece(pieceId, piecePosition, pieceType);
                            }
                            // undone draw
                            document.getElementById("draw").style.visibility = 'hidden';
                            document.getElementById("suggest").style.visibility = 'visible';
                            // enable movement
                            canMove = true;
                        } // drow proposed
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
                        } // nothing happens
                        else if (result[0] == 0) {
                            document.getElementById("draw").style.visibility = 'hidden';
                            document.getElementById("suggest").style.visibility = 'visible';
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

function updatePiece(pieceId, piecePosition, pieceType) {
    var piece = document.getElementById(pieceId);
    if (piecePosition == "kill") {
        piece.className = "killed";
        piece.innerHTML = "";
    }
    else {
        piece.className = piecePosition.substring(0, 1) + " l" + piecePosition.substring(1, 2);
        var imgTag = piece.childNodes[0];
        imgTag.src = figureTypes[pieceType];
    }
}