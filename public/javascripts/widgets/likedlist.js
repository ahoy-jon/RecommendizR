require.def("widgets/likedlist", [], function() {

   var onSuccess = new function(likedList){

   }

   var onError = new function(){

   }

   return {
      "Instance": function (containerId, resource, limit) {
         $.ajax({
            url: resource,
            data: {'limit':limit},
            success: onSuccess,
            error: onError
         });
      }
   };

});