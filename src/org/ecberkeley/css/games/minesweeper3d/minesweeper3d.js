/*  Sample Minesweeper game Copyright (c) 2012 Laramie Crocker, http://LaramieCrocker.com
     This project  is licensed under the GNU Affero General Public License.  
     You may read the software license file, called LICENSE.txt, in the project distribution.
     
     requires jQuery, jQuery effects, jQuery UI (core only).  Tested with jQuery 1.7.
  
     We keep the data in the elements of each cell, in data() nodes attached by jQuery.
     This is clean, cross-browser, and (wrapped by jQuery) supposedly free from memory leaks in the browser.
     Currently, it is fast enough for game play.
     
     If the game were to be large, or had more complexity, it might be a needed optimization 
     to move the data into a separate model, which we could iterate over more quickly 
     than having jQuery do DOM searches.
    
         e.g. 
             var model = {  scores: [],
                                   wins: 0,
                                   rows: [
                                      {cells: [{clicked: false, bomb: true, notes: ''}, {clicked: false, bomb: true, notes: ''}]},
                                      {cells: [{clicked: false, bomb: true, notes: ''}, {clicked: false, bomb: true, notes: ''}]}
                                      ]
                                 }
                                 
       This would have the added bonus of keeping all the state in a javascript object that is a bit more
          focused than having the game-state data mixed in with the UI. 
          
       The grid has been tested up to 32x32 cells, times 8 layers.
          
       Rules of the game: 
           fire up the game, and type the 'h' key, or look in the span #helpDialog in minesweeper3d.html.
           
       =============================================
       TODO: 
           I decided to use a probability to assign bombs, with a multiplier for game level, rather than 
           producing exactly, say, 10 bombs.  This yeilds simple flexibility for changing levels and number
           of layers without changes to the algorithm.
           
           The model above notwithstanding, I have optimized the 3D for-loops to where they run very 
           quickly, with a minimum of jQuery queries.  In a large application, this style of looping 
           would not scale so well. Again, the model above is the fix.
           
           For very small games, e.g. 2x2, the random algorithm can yield games with no bombs.
           The way to fix this would be to decide the number of bombs per level and distribute 
           them over the cells in the model, or in the cell.data() after the tables have been built.
           Leaving it this way for now.  More Winning!
           
           I have not spent any time profiling or doing memory leak detection.  Potentially, 
           the dynamic assignment of events can lead to leaks, but I used $(foo).html(newhtml)
            which is supposed to clean up orphaned event handlers.  This should be validated.
            
           My custom non-button buttons are fun (fading, shadows), 
              but probably one should use standard, styled buttons. :-)
            
            Similarly, a jQuery UI dialog might be a better choice than my positioned helpDialog span.
              I chose to do it this way so I could control the interface a bit more, rather than giving control over 
              to the dialog.
              
            I made no attempt to save user state between sessions using either a storage API,
              or AJAX calls.
       
            It is currently not possible to drop two of these games on a single page, 
                  but when designing components, that is always the goal.  
                  I thought of this more of a singleton app than a component, or app that could be easily included
                  on another page.
                                     
       
            Finally, the logger should be turned into a jQuery widget.
            
           =============================================
           The app has this compatability:
           
                Works in desktop browsers, on Mac OS X 10.6.8 (10K540)
:
                    Chrome : 17.0.963.83
                    Firefox : 10.0.2
                    Safari : 5.0.5
                    
                Displays in Android DroidX 2.3.4 with these problems: 
                    Browser: works, but options dialog selects don't select, 
                                  seems like it wants to click through to the z-layer below.
                    Opera Mini: 6.5 works, except floating smiley doesn't float, 
                                  and clicks seem to require a trip to the server.
                    Dolphin: behaves badly
                    
           =============================================
           Bugs: 
              1) when the grid is large (16 to 32 cells), clicks sometimes add cells rather than just marking them.
                   This appears in Firefox, but not in Chrome or Safari.
              
              2) the mouseover stuff is a bit whack when you quickly go from the tables
                  on the right to the one on the left.  Sometimes the left table doesn't show 
                  up in opactiy=1.0, even though you are directly over it.
                    
    */

function Minesweeper(){
    //square is only shape supported now.
    //==== some "constants" ========================
    var DEFAULT_GRIDSIZE = 8;  
    var BOOM = '*';
    var UNCLICKED = '?';
    var NOBOOM = '';
    var OTHER_LAYER_OPACITY = '0.3';
    
    //========= our cached "global" values for speed ===
    // save lookups in the loop.  The real val comes from the options form.
    var gGridSize = DEFAULT_GRIDSIZE; 
    
    // set at resize_relBox time, to cache this value, since it is read in onmouseover, which must be very fast.
    var gActiveLayerRHS = 300;  
    
    var gLayerCount = 1;  //gets reset every time newGame() is called.
    
    var  gGameOver = false;
    
    //========= our "globals" ======================
    var gCurrentLayer = 0;
    var prevOpaque = null;       // for dealing with re-setting the opacity on mouseover.
    var gBombs = 0;                  // the count of cells which have bombs
    var gNoBombs = 0;             // the count of cells which DON'T have bombs
    var gBombsClicked = 0;       // the count of bomb cells clicked.
    var gNoBombsClicked = 0;  // the count of cells clicked that were NOT bombs.
    var gWINS = 0;
    var gLOSSES = 0;
    var gScores = [];        // an array to store history of scores, from which we will also calc highscore.
    var gameTables = [];  //will be filled in with members of the form: ["#gameLayer0", "#gameLayer1", ...].  Each gameTable is a layer.
    var layerColors = [     // each layer has a default color, which is gotten here, and loops if layers > layerColors.
        'LightYellow',
        'Thistle',
        'PaleTurquoise',
        'Lavender',
        'Linen',
        'LightBlue'
    ];
    
    //======== Game scoring ===============
       
    function updateWinsLosses(wins, losses, justWon){
        $('#scoreWinsLosses').text(''+wins+'/'+losses); 
        gScores.push(gNoBombsClicked);
        var highScore = Math.max.apply(Math, gScores);
        
        if (justWon) $('#scoreHigh').text(''+highScore); 
        log('high score: '+highScore+' array: '+gScores.join(','));
    }
 
    function lose(){
        gLOSSES++;
        updateWinsLosses(gWINS, gLOSSES, false);
        showAllBombs();
        showEmo('Lose!<br />Click New Game', ':-(', 2000);
        gGameOver = true;
    }
    
    function winning(){
        gWINS++;
        gGameOver = true;
        updateWinsLosses(gWINS, gLOSSES, true);
        showAllBombs();
        showEmo('Winning!<br />Click New Game', ':-)', 2000);    //WINNING !!(){
    }
    
    function showGameOver(){
        showEmo('Game Over!<br />Click New Game', ':-)', 2000);   
    }
    
    function showEmo(msg, emo, millis){
        $('#smiley').html(emo+'<br />'+msg).show().delay(millis).fadeOut();
    }

    function randomBomb(level){
        //per spec, 
        //  level 1 = 1/10
        //  level 3 = 10/64 = 1/6 of cells ~== 2/10
        //  level 5 = 1/2 = 5/10
        // therefore nBombsPerCell is almost level/10 ==> probability;
        // Since the population is not large for small games, 
        //  the probability is not very reliable--it tends to not create enough bombs. See notes in TODO, above.
        var probability =  (level/10) + 0.1;
        var rn = Math.random();
        var result = rn < (probability) ? true : false;
        return result;
    }
    
    function adjacentBombCount3D(coords){
        var result = 0;
        var c = coords.c;
        var r = coords.r;
        var z = getCurrentLayer() ;
        var layerBombs = [];  //for logging.  It becomes a square matrix, below, but really we just use one layer at a time.
        
        // pre-calculate the ranges so the for-loop below doesn't have crazy ternary statements.
        var maxC = (c<(gGridSize)?c+2: c);
        var minC = (c==0?0:c-1);
        
        var maxR = (r<(gGridSize)?r+2: r);
        var minR = (r==0?0:r-1);
        
        var maxZ = (z<(gGridSize)?z+2: z);
        var minZ = (z==0?0:z-1);
        
        for (var zLay=minZ;  zLay<maxZ; zLay++){
            var zTable = $(gameTables[zLay]);  //  gameTables[zLay] is a string of the form #gameTable1, so we query, once per layer.
            layerBombs[zLay] = [];                    //  an array to store bombs-per-layer, for logging only.
            for (var row=minR; row<maxR; row++){
                var theTDs = zTable.find('tr').eq(row).find('td');
                var cell;
                for (var col=minC; col<maxC; col++){
                    cell = theTDs.eq(col);
                    if ( cell.data('state') == BOOM){
                        result++;
                        layerBombs[zLay].push(''+row+','+col);
                    }
                }
            }
            log('bombs on layer['+zLay+']:'+layerBombs[zLay].join('; '));  
        }
        log('bomb adjacent count: '+result);
        return result;
    }
    
    function showAllBombs() {
        var maxZ = gLayerCount,
              maxC = maxR = gGridSize;               
        for (var zLay=0;  zLay<maxZ; zLay++){
            var zTable = $(gameTables[zLay]);  //  gameTables[zLay] is a string of the form #gameTable1, so we query, once per layer.
            for (var row=0; row<maxR; row++){
                var theTDs = zTable.find('tr').eq(row).find('td');
                var cell;
                for (var col=0; col<maxC; col++){
                    cell = theTDs.eq(col);
                    if ( cell.data('state') == BOOM){
                        cell.find('span').css('background-color', 'blue');
                    }
                }
            }
        }
    }
    
    //========== Eye-candy =================================
    
    function flashButton(selector){
        var sl = $(selector);
        sl.stop(true, true).effect('highlight', {color: 'orange'}, 1000);
    }
    
    function anim(obj, newtop, newleft, newopacity){
        obj.stop();
        obj.animate(
           {opacity: OTHER_LAYER_OPACITY }, 
           50, 
           function() {
                    obj.animate(
                        {  top: newtop,
                            left: newleft }, 
                        200,
                        function() {
                            obj.animate({opacity: newopacity},50);
                        }
                     );
            });
    }
    
    function hidePopups(){
        $('#helpDialog').hide();
        $('#optionsPanel').hide();
    }
    
    //========== the "big" functions: newGame and its big supporting functions ==============
    
    //newGame is public.
    this.newGame = 
    function newGame(level, gridSize, trainingMode, numLayers){
            if (gridSize==undefined || gridSize==null || isNaN(gridSize)){
                gridSize = this.DEFAULT_GRIDSIZE;
            }
            gLayerCount = numLayers;
            gGameOver = false;
            gGridSize = gridSize;
            gBombs = 0;  
            gBombsClicked = 0;
            gNoBombs = 0;
            gNoBombsClicked = 0;
            gCurrentLayer = 0;
            prevOpaque = null;  
            gameTables = [];
    
            clearlog();
            log(' in newGame, level: '+level+' gridSize: '+gridSize);
            loggerSelfTest();
            
            
            var relBox = $('#relBox');
            relBox.empty();
            
            //position smiley relative to relBox, for when we need him.
            $('#smiley').css('left', relBox.position().left).css('top', relBox.position().top+50); 
            
            
            var firstID = "";
            
            OTHER_LAYER_OPACITY = 0.99 / (numLayers < 4 ? numLayers : 4);
            
            for (var i=0; i<numLayers; i++){ 
                var id = "#gameLayer"+i;
                gameTables.push(id);
                var newLayer = buildLayer(level, gridSize, trainingMode, id, i, relBox);
                log('made layer id: '+id);
                if (0==i){
                    firstID = id;
                }
            }
            
            $('#bombCount').val(''+gBombs);  //update the bomb-created counter.
            
            var corner = resize_relBox(firstID, gridSize);  //returns object with left and top of 3D layers, which is offset from main layer.
            
            if (is3DMode()){
                handle3DforLayer(firstID, gridSize, corner);
            }
            gCurrentLayer = -1;
            swapLayers(true, false);
            
            //Layers all built, so boxes resized, so set help area size:
            $('#helpDialog').css('width', $('#GamePad').css('width'));
    }
    
    function buildLayer(level, gridSize, trainingMode, gameTableID, layer, relBox/*the element to add new tables to*/){  //args not checked, but this is a private fn.
        var tbl = $("<table class='gameTableClass shadow3' id='gameLayer"+layer+"'>");
        relBox.append(tbl);
        tbl.append('<caption>'+gameTableID+'</caption>');
        var str = tbl.clone().html();
        var strg = str.replace(/\</g, "&lt;"); 
        var layerColor = layerColors[(layer % layerColors.length)];
        
        for (var r=0; r<gridSize; r++){
            var row = $('<tr>');
            tbl.append(row);
            for (var c=0; c<gridSize; c++){
                var boom; 
                if (randomBomb(level)){
                    boom = BOOM;
                    gBombs++;
                    log({message: 'bomb at ', colon: ':'}, 'r', r, 'c', c, 'z', layer, 'tbl', gameTableID);
                } else {
                    boom = NOBOOM;
                    gNoBombs++;
                    //log('NO bomb at r,c '+r+','+c+' z:'+layer+' tbl: '+gameTableID);
                }
                var cell = $('<span id="cell_'+r+'_'+c+'" class="cell" style="background-color: '+layerColor+'">').text(UNCLICKED); //('cell_'+r+'_'+c+boom);
                if (trainingMode && boom == BOOM){cell.css('background-color','darkorange');}
                var td = $('<td>').append(cell);
                td.data('state', boom);
                coords = {'r': r, 'c': c};
                td.data('coords', coords);
                td.data('gameTableID', gameTableID);
                td.data('layer', layer);
                td.click(cellClicked);  //set the onclick handler
                row.append(td);
                var data = {id: "gameLayer0", "layer": layer};
                cell.mouseover(data, mouseover);
            }
        }
     } 
     
     var MULT = 35;
     
    function resize_relBox(firstID, sGridSize){
        var layerCount = parseInt(getLayerCount());
        var layerCountMinus1 = (layerCount>1) ? layerCount-1 : 1;
        var gridSize = parseInt(sGridSize);
        var relBox = $('#relBox');
        var obj = $(firstID);
        var ow = obj.outerWidth(true);
        var oh = obj.outerHeight(true);
        var owOffset = ow;
        
        var padding = $('#GamePad').css('padding-left');
        var PADDING = parseInt(padding, 10, MULT);
    
        if (is3DMode()){
            var newWidth = (ow*2)+(layerCountMinus1*MULT)+PADDING;
            var newHeight = (oh)+(layerCountMinus1*MULT)+PADDING;
        } else {
            var newWidth = ow;
            var newHeight = oh+PADDING;
        }
        
        gActiveLayerRHS = ow + PADDING;
        
        relBox.css("height", newHeight);
        relBox.css("width", newWidth);
        var top = obj.css('top');
        
        
        var corner = {'left': owOffset, 'top': top};
        //log('owOffset, oh: '+owOffset+','+oh+'  newWidth  '+newWidth+' top: '+top);
        
        dm = $('#helpDialog');
        dm.css("height", newHeight-PADDING);
        dm.css("width", newWidth-PADDING);
        dm.css("left", relBox.position().left);
        dm.css("top", relBox.position().top);
        log('dm.css: '+dm.css("width"));
        
        return corner;
    }
    
    //========== 3D handling =================================

    function handle3DforLayer(firstID, gridSize, corner){
            //Move everything that is not the zeroth layer to the alternate position.
            var len = gameTables.length;
            for (var i=1; i<len; i++){  //skip zeroth one, that's the main layer.
                var id = gameTables[i];
                log('moving id: '+id);
                var gti = $(id);
                gti.css('left', corner.left+(i*MULT));
                gti.css('top', corner.top+(i*MULT));
            }
    }
    
    function is3DMode(){
         var c = parseInt($('#selLayer').val(), 10);
         return  $('#ck3D').is(':checked') && (c>1);
    }
    
    function getLayerCount(){
        if (is3DMode()){
            var c = parseInt($('#selLayer').val(), 10);
            return c;
        } else {
            return 1;  
        }
    }
     
    function toggle3DMode(turnOn){
        if (turnOn){
            $('#swapLayers').show();
        } else {
            $('#swapLayers').hide();
        }
        newGameClick();
    }
    
    function getCurrentLayer(){
        return gCurrentLayer;
    }
    function getCurrentLayerID(){
        return gameTables[gCurrentLayer];
    }
    
    function figureSwap(doZeroLayer) {  
        gCurrentLayer++;
        if (doZeroLayer){
            gCurrentLayer = 0;
        }
        if (gCurrentLayer == gameTables.length) { gCurrentLayer = 0; }
        return gameTables[gCurrentLayer];
    }
    
    function figureNextSwapLayer() {  
        var lidx = gCurrentLayer + 1;
        if (lidx == gameTables.length) { lidx = 0; }
        return gameTables[lidx];
    }
    //============= "private" event handlers ===============================
    
    function cellClicked(event){
        var cell = $(this);
        var currLayerID = cell.data('gameTableID');
        // First we see if they clicked on a non-active layer (is on the stack on the right)
        //  If so, swap layers and get out of here, rather than scoring any clicks.
        if ( getCurrentLayerID() != currLayerID){
            swapLayers(false, false, currLayerID);
            gCurrentLayer = gameTables.indexOf(currLayerID);
            return;
        }
        var dat = cell.data('state');
        var p = cell.offset();
        var cw = cell.width();
        var ch = cell.height();
        
        //reltive positioning of smiley works, but would need to be fixed so that it floats up and left when the cell is low and right.
        //  $('#smiley').css('left', p.left+cw).css('top', p.top + ch);   //  position smiley relative to current cell, in case we need him.
        //Instead, position smiley rel to relBox in newGame().
        
        //Hold down SHIFT key to cheat...
        if (event.shiftKey){
            showEmo((dat==BOOM?dat:'Cheating!'), ';-)', 600);  //display smirkey smiley, plus the bomb marker if bomb, else display 'Cheating!' just to be snarky.
            return;  
        }
        if (gGameOver){
            showGameOver(); 
            return;
        }
        if (dat==BOOM){  // LOSING !!!  , or Clicking on already-red
            if (gBombsClicked ==0 && (gNoBombsClicked != gNoBombs)){ 
                cell.addClass('boomCell cell').text(dat);
                gBombsClicked++;
                lose();
            }
        } else {                  
            if (cell.text()==UNCLICKED  &&(gBombsClicked == 0)){ // WINNING, or Clicking on already-green
                gNoBombsClicked++;
                $('#scoreHits').text(''+gNoBombsClicked);
                var coords = cell.data('coords');
                var bc = adjacentBombCount3D(coords);
                cell.text(''+bc).addClass('noboomCell cell');
                if (gNoBombsClicked == gNoBombs){   //remember, no bombs clicked yet.
                    winning();
                }
            }
            
        }    
    }
       
    this.mouseover = 
    function mouseover(event){
        var $this = $(this);
        if ($this.offset().left < gActiveLayerRHS){
            return;
        }
        var lay = event.data.layer;
        if (prevOpaque ){
            $(prevOpaque).css('opacity', OTHER_LAYER_OPACITY);
            prevOpaque = null;
        }
        var yo = $this.parentsUntil('#relBox');
        prevOpaque = yo[yo.length-1];  
       $(prevOpaque).css('opacity', '1.0');
        
    }
    
    function swapLayers (doZeroLayer, flashTheButton, showThisLayerID){
        if ( flashTheButton ){
            flashButton('#swapLayers');
        }
        var activeID;
        if (showThisLayerID){
            activeID = showThisLayerID;
        } else {
             activeID= figureSwap(doZeroLayer);
        }
        
        var obj = $(activeID);
        var ow = obj.outerWidth(true);
        var oh = obj.outerHeight(true);
        anim(obj, 0, 0,  0.99);
        
        var len = gameTables.length;
        for (var i=0; i<len; i++){  
            var id = gameTables[i];
            if (id==activeID){
                continue;
            }
            var gameTable = $(id);
            anim(gameTable, i*MULT, ow+20+(i*MULT),  0.44);
        }
    }
    
    //================= "public" event handlers ======================
    // also, newGame() is public, above.
    
    this.swapLayersClick = function(){
        swapLayers(false, true);
    };
    
    
    this.newGameClick = 
    function newGameClick(){
         flashButton('#newGame');
         log('newGame clicked');
         var level = $('#level').val();
         var numLayers = getLayerCount();
         var gridSize = $('#editGRIDSIZE').val();
         var training = $('#ckTraining:checked').length>0;
         newGame(level, gridSize, training, numLayers);
    }
    
    this.change3DMode = 
    function change3DMode(event){
       toggle3DMode($(this).is(':checked')); 
    }
    
    this.toggleOptionsPanel = 
    function toggleOptionsPanel(){
        $('#helpDialog').hide();
        flashButton('#showOptions');
        $("#optionsPanel").slideToggle("fast");
        $(this).toggleClass("active");
    }
        
    this.showHelp=
    function showHelp(){
        $('#optionsPanel').hide();
        var dm = $('#helpDialog');
        if ( $('#helpDialog:visible').length > 0){
            dm.hide();
        } else {
            dm.show();
        }
    }
    
    this.installKeyMap = 
    function installKeyMap(){
         $(document).keyup(function (event) {
              var evtHandled = true;
              var keyCode = event.keyCode;
              if (keyCode == 27) {  // (ESC)
                  hidePopups();
              } else if (keyCode == 13) {  // (ENTER/RETURN key)
                  $("#optionsPanel").slideUp("fast");
              } else if (keyCode == 72) {  // 'h'    (help)
                  showHelp();
              } else if (keyCode == 76) {  // 'l'    (Logger)
                  showLogger();
              } else if (keyCode == 78) {  // 'o'    (Options)
                  newGameClick();
              } else if (keyCode == 79) {  // 'o'    (Options)
                  toggleOptionsPanel();
              } else if (keyCode == 83) {  // 's'    (Shuffle)
                  swapLayers(false, true);
              } else if (keyCode == 84) {  // 't'    (Shuffle)
                  var $checkbox = $('#ckTraining');
                  $checkbox.attr('checked', !$checkbox.is(':checked'));
              } else {
                  evtHandled = false; 
              }
              if (evtHandled){
                  event.stopPropagation();  
                  event.preventDefault(); 
              }
         });
     }
   
    return this;  //return from constructor for Minesweeper class.
} //END of Minesweeper class.
          
var theGame; //waiting for jQuery constructor....
 
//===========jQuery constructor / document loaded =================
$(function() {
    theGame = Minesweeper();
    $('#newGame').click(theGame.newGameClick).trigger('click');  //wire onclick handler, and immediately fire it.
    $('#swapLayers').click(theGame.swapLayersClick);  //wire onclick handler.
    $('#ck3D').change(theGame.change3DMode);  //wire onchange handler.
    $("#showOptions").click(theGame.toggleOptionsPanel);    //wire handler for slide button to slide down #optionsPanel.
    $("#hideOptions").click(theGame.toggleOptionsPanel);    //wire handler for slide button to slide down #optionsPanel.
    $("#btnHelp").click(theGame.showHelp);    //wire handler for slide button to slide down #optionsPanel.
    theGame.installKeyMap();
    
});
//==========================================================

/* todo
shortcuts in options not wired DONE
help obscures options -- make 'o' dismiss help DONE
bombs don't work
hand icon for buttons
at game end, not obvious that you can't play again.

*/

