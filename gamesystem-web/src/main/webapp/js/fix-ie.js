function fix(){
 if(window.XMLHttpRequest) {
     //
 } else {
//if it`s a IE 6 or less
   document.getElementById('body').style.width = (document.compatMode && document.compatMode == 'CSS1Compat') ? 
   (document.documentElement.clientWidth < 1000 ? "1000px" : "100%") : (document.body.clientWidth < 1000 ? "1000px" : "100%");
 }
}
