/*
 Simple OpenID Plugin
 http://code.google.com/p/openid-selector/

 This code is licenced under the New BSD License.
 */

var providers = {
   google : {
      name : 'Google',
      url : 'https://www.google.com/accounts/o8/id'
   },
   yahoo : {
      name : 'Yahoo',
      url : 'http://me.yahoo.com/'
   },
   aol : {
      name : 'AOL',
      label : 'Entrer votre AOL screenname :',
      url : 'http://openid.aol.com/{username}'
   },
   myopenid : {
      name : 'MyOpenID',
      label : 'Entrer votre nom d\'utilisateur MyOpenID :',
      url : 'http://{username}.myopenid.com/'
   },
   openid : {
      name : 'OpenID',
      label : 'Entrer votre URL Open ID :',
      url : null
   }
};

var openid = {
   version: '1.2', // version constant
   demo: false,
   demo_text: 'demo text',
   cookie_expires: 30,   // 1 month.
   cookie_name: 'openid_provider',
   cookie_path: '/',
   input_id : null,
   lang: 'en', // language, is set in openid-jquery-<lang>.js
   signin_text: 'Valider', // text on submit button on the form
   provider_url: null,
   provider_id: null,
   all_small: false, // output large providers w/ small icons
   no_sprite: false, // don't use sprite image

   init: function(input_id) {
      $('#openid_form').submit(this.submit);
      this.input_id = input_id;
      var box_id = this.readCookie();
      if (box_id) {
         this.signin(box_id, true);
      }
   },

   /* Provider  click */
   signin: function(box_id, onload) {

      var provider = providers[box_id];
      if (! provider) {
         return;
      }

      this.setCookie(box_id);

      this.provider_id = box_id;
      this.provider_url = provider['url'];

      if (! onload) {
         $('#openid_form').submit();
      }
   },
   /* Sign-in button click */
   submit: function() {
      var url = openid.provider_url;
      if (url) {
         openid.setOpenIdUrl(url);
      }
      return true;
   },
   setOpenIdUrl: function (url) {

      var hidden = document.getElementById(this.input_id);
      if (hidden != null) {
         hidden.value = url;
      } else {
         $('#openid_form').append('<input type="hidden" id="' + this.input_id + '" name="' + this.input_id + '" value="' + url + '"/>');
      }
   },
   setCookie: function (value) {

      var date = new Date();
      date.setTime(date.getTime() + (this.cookie_expires * 24 * 60 * 60 * 1000));
      var expires = "; expires=" + date.toGMTString();

      document.cookie = this.cookie_name + "=" + value + expires + "; path=" + this.cookie_path;
   },
   readCookie: function () {
      var nameEQ = this.cookie_name + "=";
      var ca = document.cookie.split(';');
      for (var i = 0; i < ca.length; i++) {
         var c = ca[i];
         while (c.charAt(0) == ' ') c = c.substring(1, c.length);
         if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
      }
      return null;
   },

   setDemoMode: function (demoMode) {
      this.demo = demoMode;
   }
};
