require.def("widgets/likedlist", ["jquery", "utils"], function($, Utils) {

   var switchlike = function(likedId, switchLikeResource, containerId, e) {
      e.preventDefault();
      var el = $(this);
      $.ajax({
         url: switchLikeResource,
         data: {likedId:likedId},
         success: onSuccessSwitch.curry(containerId),
         error: function() {
         }
      })
   }

   var onSuccessSwitch = function(containerId, liked) {
      $('#' + containerId + "-list-a-" + liked[0].id).text(liked[0].liked == true ? "unlike" : "like");
      $('#' + containerId + "-list-ignore-" + liked[0].id).text(liked[0].ignored == true ? "unignore" : "ignore");
   }

   var switchIgnore = function(likedId, switchIgnoreResource, containerId, e) {
      e.preventDefault();
      var el = $(this);
      $.ajax({
         url: switchIgnoreResource,
         data: {likedId:likedId},
         success: onSuccessSwitch.curry(containerId),
         error: function() {
         }
      })
   }

   var onSuccess = function(containerId, isLikedResource, switchLikeResource, switchIgnoreResource, likedList) {
      if (likedList.length == 0) {
         $('#' + containerId).html('<p>No data.</p>');
      } else {
         $('#' + containerId).html('<ul id="' + containerId + '-list"></ul>');
         $(likedList).each(function(i, el) {
            var likeOrUnlikeButton = "";
            var ignoreButton = "";
            if (el.liked == true) {
               likeOrUnlikeButton = " <a id='" + containerId + "-list-a-" + el.id + "' href='#'>unlike</a> ";
            } else if (el.liked == false) {
               likeOrUnlikeButton = " <a id='" + containerId + "-list-a-" + el.id + "' href='#'>like</a> ";
            }
            if (el.ignored == true) {
               ignoreButton = " <a id='" + containerId + "-list-ignore-" + el.id + "' href='#'>unignore</a> ";
            } else if (el.ignored == false) {
               ignoreButton = " <a id='" + containerId + "-list-ignore-" + el.id + "' href='#'>ignore</a> ";
            }
            $('#' + containerId + "-list-a-" + el.id).live('click', switchlike.curry(el.id, switchLikeResource, containerId));
            $('#' + containerId + "-list-ignore-" + el.id).live('click', switchIgnore.curry(el.id, switchIgnoreResource, containerId));
            var ahref = $('<a>').addClass('histolink').attr('href','#!/liked/'+el.id).attr('alt',el.description).text(el.name);
            var li = $('<li>').attr('id', containerId + '-li-' + el.id);
            $(li).append(ahref).append(likeOrUnlikeButton).append(ignoreButton);
            $('#' + containerId + '-list').append(li);
         });

      }
   }

   var onError = function(containerId, xhr) {
      $('#' + containerId).html('<p>' + xhr.responseText + '</p>');
   }

   return {
      "Instance": function (containerId, resource, data, isLikedResource, switchlikeResource, switchIgnoreResource) {
         var self = this;

         self.refresh = function() {
            $.ajax({
               url: resource,
               data: data,
               success: onSuccess.curry(containerId, isLikedResource, switchlikeResource, switchIgnoreResource),
               error: onError.curry(containerId)
            });
         }

         self.refresh();
      }
   };

})