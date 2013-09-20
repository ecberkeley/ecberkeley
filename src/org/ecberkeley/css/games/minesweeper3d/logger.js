    //See the logger-test.html which calls this unit with selfTest()
    
    var logi =0;
    
    var loggerDefaultOptionsObject = {
                 colon: ': ', 
                 separator: ', ', 
                 prettyDelimiters: true, 
                 multiple: false,
                 direction: 'jQuery.fn.prepend'
    };
    //Now clone it for the starting instance:
    var loggerOptionsObject = JSON.parse(JSON.stringify(loggerDefaultOptionsObject));

    this.loggerOptions = function loggerOptions(opts){
        if (opts==null){
            return jQuery.extend(loggerOptionsObject, loggerDefaultOptionsObject);
        }
      
        var res =  jQuery.extend(loggerOptionsObject, loggerDefaultOptionsObject, opts);
        return res;
    }
    
    this.log = 
    function log(msg){
        var theLogElement = $('#logger ul');
        var options = loggerOptionsObject;
        
        if (arguments.length ==1){
            addToLog(wrapLine(msg));
        } else {
            var i = 0,
                  line = "",
                  len = arguments.length;
            if (len>1 && len % 2 ==1){
                 i = 1;
                 options = {};
                 jQuery.extend(options, loggerOptionsObject, arguments[0]);
            }  
            var SEP = options.separator;
            var COL = options.colon;
            if (options.prettyDelimiters){
                SEP = sep(options.separator); 
                COL = col(options.colon);
            }
            
            if (options.message){
                if (options.multiple == true){
                    addToLog(wrapLine(options.message));
                } else {
                    line += options.message;
                }
            }
            while (i+1 < len){
                var name = arguments[i];
                var val = arguments[i+1];
                if (options.multiple == true){
                    addToLog(wrapLine(wrapProp(name,val)));
                } else {
                    line +=  wrapProp(name,val)+(i+3<len?SEP:'');
                }
                i += 2;
            }
            if (options.multiple != true){
                addToLog(wrapLine(line));
            }
        }
        
        //Local, organic, helper functions.
        
        function addToLog(msg){
            //we are effectively doing this: 
            //    loggerDefaultOptionsObject['direction'].call($('#logger ul'), (wrapLine(options.message)));
            //    but with a string, because JSON strips functions, so they are gone from any attempt to store before the clone.
            var theFn = eval(options['direction']);
            theFn.call(theLogElement, (msg));
        }
        function lineNum(n){
            return '<span class="logger-linenum">'+(n)+':</span> ';
        }
        function wrapLine(line){
            return "<li class='logger-line'>"+lineNum(logi++)+line+"</li>";
        }
        function wrapProp(name, val){
            return '<span class="logger-name">'+name+'</span>' + COL + '<span class="logger-value">'+val+'</span>';
        }
        function sep(separator){ 
            return '<span class="logger-delimiter">'+separator+'</span>';  
        }
        function col(colon){
            return '<span class="logger-colon">'+colon+'</span>';  
        }
    }
    
    this.clearlog = 
    function clearlog(msg){
         $('#logger ul').html("");
         logi = 0;
    }
    
    function dumpObjBySelector(sel){
        var str = $(sel).clone().html();
        if (str==null){
            log('selector did not find an object: '+sel);
            return;
        }
        var strg = str.replace(/\</g, "&lt;"); 
        log(strg);  
    }

    function showLogger(){
         if ($('#loggerWrapper:visible').length > 0){
             $('#loggerWrapper').stop(true, true).fadeOut();
         } else {
             $('#loggerWrapper').stop(true, true).show();//.delay(5000).fadeOut();
         }
     }
     
     function logObject(msg, obj){
        var resText = JSON.stringify(obj, null, '   ');
        log(msg, resText);
     }
     
     function loggerSelfTest(){
         var model = {  scores: [],
                                   wins: 0,
                                   rows: [
                                      {cells: [{clicked: false, bomb: true, notes: 'r0c0'}, {clicked: false, bomb: true, notes: 'r0c1'}]},
                                      {cells: [{clicked: true, bomb: false, notes: 'r1c0'}, {clicked: false, bomb: true, notes: 'r1c1'}]}
                                      ]
                                 };
                                 
             //Can also run all these tests with: loggerOptions({direction: "jQuery.fn.append"}); 
             
            loggerOptions({direction: "jQuery.fn.prepend"});                     
            logObject("model", model);
            log({message: "Initialization. (multiple)", multiple: true}, "First", 23, "Second", "mojo", "Third", 1.29876);
            log({message: "Initialization. (singleLine)"}, "First", 23, "Second", "mojo", "Third", 1.29876);
            
            log("Same test, with local 'separator:' option override.");
            log({message: "Initialization. (singleLine)", separator: '; ', direction: "jQuery.fn.append"}, "First", 23, "Second", "mojo", "Third", 1.29876);
            
            log("Now testing logger with custom 'separator:' option, but without local override.");
            loggerOptions({separator: '\u00BB '});
            log({message: "Initialization. (singleLine)"}, "First", 23, "Second", "mojo", "Third", 1.29876);
            
            log("Now testing logger with custom 'separator:' option.");
            loggerOptions({separator: '\u00BB '});
            log({message: "Initialization. (singleLine)"}, "First", 23, "Second", "mojo", "Third", 1.29876);
            
            log("Now testing logger with custom 'colon:' option, and custom 'separator:'.");
            loggerOptions({colon: '\u00BB ', separator: '***'});
            log({message: "Initialization. (singleLine)", separator: '; '}, "First", 23, "Second", "mojo", "Third", 1.29876);
            
            log("Now testing logger with options reset");
            loggerOptions(null);
            
            log({message: "(singleLine, default direction)", separator: '; '}, "First", 23, "Second", "mojo", "Third", 1.29876);
            
            log({direction: "jQuery.fn.append"}, "This should be at the bottom of the log, due too option override.");
            log("This should be at the top of the log, local option override should not stick.");
            
            loggerOptions({direction: "jQuery.fn.append"}); 
            log("This should be at the bottom of the log, reset default option override should stick.");
            
            loggerOptions(null);
            
     }
