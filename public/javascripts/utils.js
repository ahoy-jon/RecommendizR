require.def("utils", ["functional"], function() {

   /** Install functional API. */
   Functional.install();

   /**
    * Firebug logging graceful degradation for IE.
    */
   if (! ("console" in window) || !("firebug" in console)) {
      var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];
      window.console = {};
      for (var i = 0; i < names.length; ++i) window.console[names[i]] = function() {
      };
   }

   /**
    * Fonction de clonage
    * @author Keith Devens
    * @see http://keithdevens.com/weblog/archive/2007/Jun/07/javascript.clone
    */
   jQuery.extend({
      /**
       * Generic aggregate core function.
       */
      aggregate: function(f, seed, list) {
         var result = seed;
         for (var index in list) {
            result = f(list[index], result);
         }
         return result;
      },

      /**
       * String joinWith method.
       */
      joinWith: function(separator, selector, elements) {
         return $.aggregate(function(e, r) {
            return r == null ? selector(e) : r + separator + selector(e);
         }, null, elements);
      },

      groupBy: function(fn, elements) {
         if (!elements) return elements;

         if (!fn || typeof (fn) !== typeof (Function)) {
            throw Error.argumentType("fn", typeof (fn), typeof (Function), "groupBy takes a function to filter on");
         }
         var ret = new Array();
         for (var i = 0; i < elements.length; i++) {
            var key = fn(elements[i]);
            if (!ret[key]) {
               ret[key] = new Array();
            }
            ret[key].push(elements[i]);
         }
         var result = new Array();
         var counter = 0;
         for (var k in ret) {
            result[counter] = {"key":k,"items":ret[k]};
            counter++;
         }
         return result;
      },

      objectSize: function(obj) {
         var size = 0, key;
         for (key in obj) if (obj.hasOwnProperty(key)) size++;
         return size;
      },

      /**
       * Retrieve an specific parameter from the current URL.
       */
      getUrlParam: function(name) {
         var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(window.location.href);
         return results ? results[1] || 0 : null;
      },

      /**
       * Verify if the parameter is null or empty.
       */
      isEmpty: function(str) {
         return (str == null || str.length == 0) ? true : false;
      },

      /**
       * Validate an url based on some valid protocols.
       */
      validProtocol: function(path) {
         if ($.isEmpty(path)) return false;
         return some("'" + path + "'.startsWith(_)", ["http", "ftp"]);
      },

      /**
       * Simple filed validation with custom function.
       */
      validate: function(json) {
         $(".validateError").remove();
         return reduce("x y -> x && y", true, map(function(validation) {
            var valid = validation.validate($("#" + validation.name).val());
            if (!valid) $("#" + validation.name).after("&nbsp;<span class='validateError'>" + validation.message + "</span>");
            return valid;
         }, json.validations));
      }

   });

   /**
    * Retrieve the direct text of an element and not the text of its possible children (nodes).
    */
   jQuery.fn.extend({
      innerText: function() {
         return this.contents().filter(function() {
            return this.nodeType != 1;
         });
      }
   });

   /**
    * Repeat function.
    * "Jonas".repeat(5) == "JonasJonasJonasJonasJonas"
    */
   String.prototype.repeat = function(times) {
      return new Array(times + 1).join(this);
   };
   /**
    * Left trim the string.
    */
   String.prototype.ltrim = function (chars) {
      chars = chars || "\\s";
      return this.replace(new RegExp("^[" + chars + "]+", "g"), "");
   };
   /**
    * Right trim the string.
    */
   String.prototype.rtrim = function (chars) {
      chars = chars || "\\s";
      return this.replace(new RegExp("[" + chars + "]+$", "g"), "");
   };
   /**
    * Trim the string.
    */
   String.prototype.trim = function (chars) {
      return this.ltrim(chars).rtrim(chars);
   };
   /**
    * Retrieve the first upper case word of the string.
    */
   String.prototype.retrieveUpperCaseWord = function() {
      return this.replace(new RegExp('[^A-Z]*([A-Z]+).*', "g"), '$1');
   }
   /**
    * Test if a string starts with another.
    */
   String.prototype.startsWith = function(str) {
      return this.indexOf(str) === 0;
   }
   /**
    * Compares two strings, returning -1, 0 or 1.
    */
   String.prototype.compareTo = function(that) {
      var a = this.toLowerCase();
      var b = that.toLowerCase();
      return (a < b)
              ? -1
              : (a > b)
              ? 1
              : 0;
   }

   // Compares strings 'a' and 'b' returning -1, 0 or 1.
   // If given a conversion function 'f', converts object 'a' and 'b' to
   // strings before comparing.
   var stringComparator = function(a, b, f) {
      if (typeof f == 'function') {
         a = f(a);
         b = f(b);
      }
      return a.compareTo(b);
   }

   var getFromSplitAndPosition = function (doubleCount, indice) {
      var array = doubleCount.split("@");
      return array[indice];
   };

   var concatTwo = function (l1, l2) {
      var result = new Array();
      for (var i = 0; i < l1.length; i++)
         result.push(l1[i]);
      for (var i = 0; i < l2.length; i++)
         result.push(l2[i]);
      return result;
   };

   var genericErrorCallback = function () {
      return function(httpStatus) {
         return false;
      };
   };

   var splitAt = function (list, index) {
      var listPrefix = list.slice(0, index);
      var listTail = list.slice(index);
      return [listPrefix,listTail];
   };

   var asListRowWise = function (matrix) {
      return reduce(function(a1, a2) {
         return a1.concat(a2);
      }, new Array(), matrix);
   };

   var transpose = function (matrix) {
      var temp;
      var T = new Array();
      for (i = 0; i < matrix.length; i++) {
         for (j = 0; j < matrix[0].length; j++) {
            if (!T[j])T[j] = new Array();
            if (i < matrix.length && j < matrix[i].length) {
               temp = matrix[i][j];
               T[j][i] = temp;
            }

         }
      }
      return T;
   };

   return {
      /** A simple string comparator returning -1, 0, or 1
       * Optionnal converter function 'f' converts arguments to string
       * before comparing
       */
      "stringComparator": stringComparator,

      /**
       * A simple callback that hide the specified
       * @return false
       */
      "genericErrorCallback": genericErrorCallback,

      /**
       * Event bus that manage registering and execution.
       * This event can handle no or one parameter.
       */
      "Event": function () {
         var handlers = new Array();
         this.handlers_ = handlers;
         this.add = function(f) {
            if (typeof f == "function") handlers[handlers.length] = f;
         };
         this.execute = function(param) {
            for (var i = 0; i < handlers.length; ++i) handlers[i](param);
         };
      },

      /**
       * Event bus that manage registering and execution.
       * This event can handle no, one or two parameter(s).
       */
      "Event2": function () {
         var handlers = new Array();
         this.add = function(f) {
            if (typeof f == "function") handlers[handlers.length] = f;
         };
         this.execute = function(param1, param2) {
            for (var i = 0; i < handlers.length; ++i)handlers[i](param1, param2);
         };
      },

      "transpose": transpose,

      "splitAt": splitAt,

      "concatTwo": concatTwo,

      /**
       * Takes a list of lists and returns one list that contains all elements of these lists
       * join([[1,2],[3],[4]]) gives [1,2,3,4]
       * @param xss
       * @return
       */
      "join": function (xss) {
         return reduce(concatTwo, [], xss);
      },

      /**
       * Get number of item still on market.
       * @param doubleCount
       * @return
       */
      "getCountWithoutSuppress": function (doubleCount) {
         return getFromSplitAndPosition(doubleCount, 0);
      },

      /**
       * Get number of item NOT on market.
       * @param doubleCount
       * @return
       */
      "getCountWithSuppress": function (doubleCount) {
         return getFromSplitAndPosition(doubleCount, 1);
      },

      /**
       * Util method to split on @ character.
       * @param doubleCount
       * @param indice
       */
      "getFromSplitAndPosition": function (doubleCount, indice) {
         var array = doubleCount.split("@");
         return array[indice];
      },

      /**
       * Util method to split on @ character.
       * @param doubleCount
       * @param indice
       */
      "getFromSplitAndPosition" : getFromSplitAndPosition,

      /**
       * Util method to open links addressed by selector rule inside a popup.
       * @param selector JQuery selector which sould point to an <a> tag
       */
      "openThesesLinksInsideAPopup" : function(selector) {
         $(selector).click(function(event) {
            window.open($(this).attr('href'), 'popupWindow', 'menubar=1,statusbar=1,scrollbars=1,width=800,height=600');
            event.preventDefault();
            event.stopPropagation();
         });
      },

      /**
       * Util method to extract a key from the hash part of the url.
       * Exemple for the URL : 'showForeignEquivalent.html?productId=78256#atcId=1159;routeId=38'
       *    getValueFromHash('atcId') returns 1159
       *  getValueFromHash('routeId') returns 38
       * @param key key used to get associated value
       */
      "getValueFromHash": function(key) {
         var regexS = "[#;]" + key + "=([^;]*)";
         var regex = new RegExp(regexS);
         var results = regex.exec(window.location.href);
         if (results == null)
            return "";
         else
            return decodeURIComponent(results[1]);
      }
   };
});
