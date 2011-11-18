<%@page import="com.google.gdata.client.authn.oauth.OAuthParameters"%>
<%@page import="com.expressflow.security.KeyHolder"%>
<%@page import="com.expressflow.singleton.ProcessSingleton"%>
<%@page import="com.google.gdata.client.authn.oauth.OAuthException"%>
<%@page import="com.google.gdata.client.docs.DocsService"%>
<%@page import="com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer"%>
<%@page import="com.google.gdata.client.authn.oauth.GoogleOAuthHelper"%>
<%@page import="com.google.gdata.client.authn.oauth.GoogleOAuthParameters"%>
<%@page import="com.google.appengine.api.users.User"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%
    // Authenticate User
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
    	
    	/* TODO: Set the keys accordingly!
    	 * Details can be found under:
    	 * http://code.google.com/intl/en-US/apis/gdata/docs/auth/oauth.html
    	 */
    	GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
		oauthParameters.setOAuthConsumerKey(KeyHolder.GOOGLE_API_KEY);
		oauthParameters.setOAuthConsumerSecret(KeyHolder.GOOGLE_API_SECRET);
		oauthParameters.setScope("https://docs.google.com/feeds/ https://www.google.com/calendar/feeds/");
		String protocol=request.getScheme();
		String domain=request.getServerName();
		String port=Integer.toString(request.getServerPort());
		String callbackUrl = protocol+"://"+domain+":"+port; 
		oauthParameters.setOAuthCallback(callbackUrl + "/oauth2callback");

		GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(
	            new OAuthHmacSha1Signer());
		
		String oauthUrl = null;
		
		try{
			oauthHelper.getUnauthorizedRequestToken(oauthParameters);
			oauthUrl = oauthHelper.createUserAuthorizationUrl(oauthParameters);
			request.getSession().setAttribute("oauthTokenSecret",
	                oauthParameters.getOAuthTokenSecret());
		}catch(OAuthException oae){
		}
%>
<p>Hello, <%= user.getNickname() %>! <br />
<br />
What's next?<br />
> <a href="http://expressflow.com/doc/10-min-fast-track.html" target="_blank">10 minutes Quick start</a><br/>
> <a href="/designer/Designer">Design my workflows</a> <br /> 
> <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign out</a> <br />
<br />
<!-- Google OAuth Service START -->
Access to Google Services<sup><a href="/website/doc/GoogleServiceHelp.jsp" target="_blank">?</a></sup>:<br />
<% if(ProcessSingleton.getInstance().getOAuthParameters(user.getEmail()) != null){ %>
<img src="/images/accept.png" /> Access enabled<br />
> <a href="/oauth2revoke">Revoke Access to Google Services</a><br />
<% }  else { %>
> <a href="<%= oauthUrl %>">Enable Access to Google Services</a><br />
<% } %>
<!-- Google OAuth Service END -->
<!--  Facebook OAuth Service START 
Access to Facebook Services: <br />
<div id="fb-root"></div>
      <script>
        window.fbAsyncInit = function() {
          FB.init({
            appId      : '124942834227785',
            status     : true, 
            cookie     : true,
            xfbml      : true
          });
        };
        (function(d){
           var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
           js = d.createElement('script'); js.id = id; js.async = true;
           js.src = "//connect.facebook.net/en_US/all.js";
           d.getElementsByTagName('head')[0].appendChild(js);
         }(document));
      </script>
<div class="fb-login-button">Login with Facebook</div>
 Facebook OAuth Service END -->
<br />
Administration
<br />
> <a href="/myprocesses">Your processes</a> <br />
> <a href="/mytasks">Your Tasks</a> 
</p>
<%
    } else {
%>
<p>
> <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Connect with my Google Account</a>
</p>
<%
    }
%> 