package com.example.noellesun.sakai;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Login extends AppCompatActivity {
    ArrayList<String> idarray = new ArrayList<>();
    private ProgressDialog pDialog;
    private String TAG = Login.class.getSimpleName();
    String target = "site:";
    String cookiestr;
    String basic = "https://shib.oit.duke.edu/idp/authn/external?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        //Use shib.duke to do the authentication
        WebView browser = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //set cookie to maintain the session
        final CookieManager cookieManager = CookieManager.getInstance();

        //Use webview to redirect to sakai login page
        browser.loadUrl("https://sakai.duke.edu/portal/logout");
        browser.loadUrl("https://sakai.duke.edu/portal/login");
        Log.i("url", browser.getUrl());

        browser.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("MyApp", view.getUrl());

                if (url.equals("https://sakai.duke.edu/portal")) {
                    cookiestr = cookieManager.getCookie(url);
                    new GetMember().execute();
                }
            }
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                if(url.length() >= basic.length() && url.substring(0, basic.length()).equals(basic)) {
                    Log.e("override url", url);
                    String summary = generate_login_html(url);
                    view.loadData(summary, "text/html; charset=utf-8", "utf-8");
                }
                return false; // then it is not handled by default action
            }
        });

    }
    //Use AsyncTask to retrieve json from sakai server on the background
    class GetMember extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://sakai.duke.edu/direct/membership.json",cookiestr);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray members = jsonObj.getJSONArray("membership_collection");

                    // looping through All memberships
                    for (int i = 0; i < members.length(); i++) {
                        JSONObject c = members.getJSONObject(i);

                        String userId = c.getString("userId");//get userId
                        Log.e(TAG, "UserId="+userId);
                        String id = c.getString("id");//userId::site::siteId
                        int startIndex = id.indexOf(target);
                        String siteId = id.substring(startIndex+5);//get siteId
                        Log.e(TAG, "SiteID="+siteId);
                        // adding userId, each siteId into idarray
                        if (i == 0){
                            idarray.add(userId);
                            idarray.add(siteId);
                        }
                        else {
                            idarray.add(siteId);
                        }
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

                Log.e("ID_ARRAY1:",idarray.toString());
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            //send userid and sitesid to sites view
            Intent intent = new Intent(Login.this, sites.class);
            Bundle b=new Bundle();
            b.putStringArrayList("ID_ARRAY",idarray);
            intent.putExtras(b);
            intent.putExtra("ID","Login");
            startActivity(intent);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }
    private String generate_login_html(String url){


        String summary = "<head>\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge;\">\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Duke | Log In</title>\n" +
                "    <meta name=\"description\" content=\"Duke - Log In\">\n" +
                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">\n" +
                "    <link rel=\"icon\" href=\"/favicon.ico\" type=\"image/x-icon\">\n" +
                "    <script type=\"text/javascript\" src=\"/idp/js/jquery-3.2.1.min.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      jQuery.extend({\n" +
                "        isValidSelector: function(selector) {\n" +
                "          try {\n" +
                "            var $element = $(selector);\n" +
                "          } catch(error) {\n" +
                "            return false;\n" +
                "          }\n" +
                "\n" +
                "          return true;\n" +
                "        }\n" +
                "      });\n" +
                "\n" +
                "      function passwordPopulated() {\n" +
                "        if ($(\"#j_password\").val().length > 0 || ($.isValidSelector(\"input:-webkit-autofill\") && $(\"#j_password:-webkit-autofill\").length > 0)) {\n" +
                "          return true;\n" +
                "        }\n" +
                "\n" +
                "        return false;\n" +
                "      }\n" +
                "    </script>\n" +
                "\n" +
                "    <script src=\"/idp/js/boomerang-oit.js\" type=\"text/javascript\"></script>\n" +
                "    <script type=\"application/javascript\">\n" +
                "      var isAgent = {\n" +
                "        OutlookApp: function() {\n" +
                "          return navigator.userAgent.match(/iPhone/);\n" +
                "        },\n" +
                "        any: function() {\n" +
                "          return (isAgent.OutlookApp());\n" +
                "        }\n" +
                "      };\n" +
                "    (function() {\n" +
                "      if( isAgent.any() ) { return; };\n" +
                "      // random beacon sampling\n" +
                "      var epoch = Math.floor(Date.now() / 1000);\n" +
                "      var urls = [\"https://beacon.oit.duke.edu\"];\n" +
                "      var num = Math.floor(Math.random() * urls.length);\n" +
                "      host = urls[num].replace(new RegExp('http.*://',''),'');\n" +
                "      var clientip = \"10.194.129.27\";\n" +
                "      var server = \"shib.oit.duke.edu\";\n" +
                "      BOOMR.init({\n" +
                "           beacon_url: \"https://via.oit.duke.edu/boomerangs/boompost\",\n" +
                "           site_domain: server,\n" +
                "           user_ip: clientip,\n" +
                "           beacon_type: 'POST',\n" +
                "           BW: {\n" +
                "             base_url: urls[num] + '/images/',\n" +
                "             cookie: server + '-bw',\n" +
                "             cookie_exp: 120,\n" +
                "             test_https: true,\n" +
                "             block_beacon: true\n" +
                "           },\n" +
                "           RT: {\n" +
                "             cookie: server + '-rt',\n" +
                "             cookie_exp: 120,\n" +
                "             test_https: true,\n" +
                "             block_beacon: true\n" +
                "           }\n" +
                "      }).\n" +
                "      addVar({\n" +
                "       \"clientip\": clientip,\n" +
                "       \"useragent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36\",\n" +
                "       \"http_ver\": \"HTTP/1.1\",\n" +
                "       \"method\": \"GET\",\n" +
                "       \"req_time\": epoch,\n" +
                "       \"uri_path\": \"/idp/login.jsp\",\n" +
                "       \"host\": host,\n" +
                "       \"protocol\": \"https\"\n" +
                "      });\n" +
                "    })();\n" +
                "    </script>\n" +
                "\n" +
                "  </head>\n" +
                "<body>\n" +
                "\n" +
                "            <div class=\"panel panel-default\">\n" +
                "<div class=\"container container-fluid\" style=\"text-align: center; /*让div内部文字居中*/\n" +
                "    background-color: #fff;\n" +
                "    border-radius: 20px;\n" +
                "    width: 300px;\n" +
                "    height: 350px;\n" +
                "    margin: auto;\n" +
                "    position: absolute;\n" +
                "    top: 0;\n" +
                "    left: 0;\n" +
                "    right: 0;\n" +
                "    bottom: 0;\">  \n" +
                "            <div class=\"panel panel-default\">\n" +
                "              \n" +
                "     <div class=\"panel-heading\">\n" +
                "                    <h3>Login</h3>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body\">\n" +
                "<form action=\"" + url + "\" method=\"post\">\n" +
                "\n" +
                "\n" +
                "          <div class=\"login-fields\">   \n" +
                "          <h4 for=\"j_username\" class=\" container-fluid\">NetID</h4>\n" +
                "          <input class=\"text\" type=\"text\" id=\"j_username\" name=\"j_username\" maxlength=\"50\" autocorrect=\"off\" autocapitalize=\"off\">\n" +
                "          <script type=\"text/javascript\">$(\"#j_username\").focus();</script>\n" +
                "          <br></br>\n" +
                "          <h4 for=\"j_password\" class=\"container-fluid\">Password</h4>\n" +
                "          <input class=\"text\" type=\"password\" id=\"j_password\" name=\"j_password\" value=\"\">\n" +
                "\n" +
                "\n" +
                "\n" +
                "          <span id=\"login_options_loading\"></span>\n" +
                "\n" +
                "          <div id=\"login_options_heading_required\" class=\"mfa\" style=\"display:none\">\n" +
                "            <h2>Multi-factor Authentication</h2>\n" +
                "            <div class=\"mfa-fields\">\n" +
                "              <div id=\"login_options\">\n" +
                "\n" +
                "<input id=\"multiFactorCookieStrength\" type=\"hidden\" value=\"0.0\">\n" +
                "<input id=\"additionalRequiredStrength\" type=\"hidden\" value=\"-0.5\">\n" +
                "</div>\n" +
                "              <span id=\"advanced_verification_options2\" style=\"display:none\">\n" +
                "                <span class=\"notes\">\n" +
                "                  Multi-factor authentication is already complete.\n" +
                "                </span>\n" +
                "              </span>\n" +
                "              <span id=\"advanced_verification_options3\" style=\"display:none\">\n" +
                "                <span class=\"notes\">\n" +
                "                  Multi-factor authentication is required.\n" +
                "                </span>\n" +
                "              </span>\n" +
                "\n" +
                "              <span id=\"advanced_verification_options1\" style=\"display:none\">\n" +
                "                <ul class=\"mfa-options\">\n" +
                "                  <li class=\"remember-mfa\">\n" +
                "                    <input type=\"checkbox\" id=\"rememberme\" name=\"rememberme\">\n" +
                "                    <label for=\"rememberme\">Remember device for 72 hours</label>\n" +
                "                    <i class=\"fa fa-question-circle tooltip\" tabindex=\"0\" aria-label=\"what's this?\">\n" +
                "                      <div class=\"tooltip-text\">\n" +
                "                        < a href=\"#\" class=\"tooltip-close\" aria-label=\"close\"><i class=\"fa fa-window-close-o\" aria-hidden=\"true\"></i></ a>\n" +
                "                        This option will allow you to skip multi-factor authentication for the next 72 hours if logging in from this same device and browser.  You will still be prompted to enter your NetID and password.\n" +
                "                      </div>\n" +
                "                    </i>\n" +
                "                  </li>\n" +
                "                </ul>  \n" +
                "              </span>\n" +
                "              <ul id=\"advanced_verification_links\" class=\"mfa-options\">\n" +
                "                <li class=\"mfa-indented\">< a href=\"https://idms-mfa.oit.duke.edu/mfa/help?open=authentication_forgotdevice_link#authentication_forgotdevice_anchor\" class=\"smaller\" target=\"_blank\">Forgot your device?\n" +
                "                <i class=\"fa fa-external-link-square\" aria-hidden=\"true\"></i>\n" +
                "              </ a></li>\n" +
                "                <li class=\"mfa-indented\">< a href=\"https://idms-mfa.oit.duke.edu/mfa/help?open=authentication_newdevice_link#authentication_newdevice_anchor\" class=\"smaller\" target=\"_blank\">Have a new device?\n" +
                "                  <i class=\"fa fa-external-link-square\" aria-hidden=\"true\"></i>\n" +
                "                </ a></li>\n" +
                "              </ul>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "          <input id=\"disableAutoCheck\" type=\"hidden\" value=\"1\">\n" +
                "          <input id=\"disableMFACheck\" type=\"hidden\" value=\"1\">\n" +
                "\n" +
                "          <input id=\"loginPageTime\" name=\"loginPageTime\" type=\"hidden\" value=\"1521923015028\">\n" +
                "<br></br>\n" +
                "          <button type=\"submit\" class=\"btn btn-primary\" id=\"Submit\" name=\"Submit\">Log In</button>\n" +
                "        <br></br>\n" +
                "         <a href=\"https://idms.oit.duke.edu/pwreset/\" id=\"forgot-pw-link\" class=\"smaller\" target=\"_blank\">Forgot your password?&nbsp;<i class=\"fa fa-external-link-square\" aria-hidden=\"true\"></i></a>\n" +
                "    <script type=\"text/javascript\">\n" +
                "    $(document).ready(function() {\n" +
                "\n" +
                "      $(\"#j_username\").keydown(function(event) {\n" +
                "        var code = event.keyCode || event.which;\n" +
                "        if (typeof code != 'undefined') {\n" +
                "          if (code != 9 && code != 13) {\n" +
                "            $(\"#disableMFACheck\").val(\"0\");\n" +
                "          }\n" +
                "        }\n" +
                "      });\n" +
                "\n" +
                "      $(\"#j_username\").blur(function(event) {\n" +
                "        event.preventDefault();\n" +
                "\n" +
                "        if ($(\"#j_username\").val().length == 0) {\n" +
                "          return;\n" +
                "        }\n" +
                "\n" +
                "        $(\"#disableAutoCheck\").val(\"1\");\n" +
                "        if ($(\"#disableMFACheck\").val() == \"1\") {\n" +
                "          return;\n" +
                "        }\n" +
                "\n" +
                "        $(\"#disableMFACheck\").val(\"1\");\n" +
                "\n" +
                "        if ($(\"#j_username\").val().length > 0) {\n" +
                "\n" +
                "          $('#login_options_loading').replaceWith(\"<span id='login_options_loading'><br />< img src='/idp/img/loading_animation.gif' alt='Reloading' /></span>\");\n" +
                "          $('#login_options').replaceWith(\"<div id='login_options'></div>\");\n" +
                "          var username = $('#j_username').val();\n" +
                "\n" +
                "          jQuery.post(\"/idp/authn/external\", { j_username_prefMech: username }, function(result) {\n" +
                "            $('#login_options').replaceWith(\"<div id='login_options'>\" + result + \"</div>\");\n" +
                "            $('#login_options_loading').replaceWith(\"<span id='login_options_loading'></span>\");\n" +
                "            if (1.0 == 1 && ($(\".expandLink\").length > 0 || parseFloat($(\"#multiFactorCookieStrength\").val()) > 0 || parseFloat($(\"#additionalRequiredStrength\").val()) > 0)) {\n" +
                "              $(\"#login_options_heading_required\").show();\n" +
                "\n" +
                "              if (parseFloat($(\"#multiFactorCookieStrength\").val()) > 0) {\n" +
                "                $(\"#advanced_verification_options1\").hide();\n" +
                "                $(\"#advanced_verification_options2\").show();\n" +
                "                $(\"#advanced_verification_options3\").hide();\n" +
                "                $(\"#advanced_verification_links\").hide();\n" +
                "              } else if ($(\".expandLink\").length == 1) {\n" +
                "                $(\"#advanced_verification_options1\").show();\n" +
                "                $(\"#advanced_verification_options2\").hide();\n" +
                "                $(\"#advanced_verification_options3\").hide();\n" +
                "                $(\"#advanced_verification_links\").show();\n" +
                "              } else {\n" +
                "                $(\"#advanced_verification_options1\").hide();\n" +
                "                $(\"#advanced_verification_options2\").hide();\n" +
                "                $(\"#advanced_verification_options3\").show();\n" +
                "                $(\"#advanced_verification_links\").hide();\n" +
                "              }\n" +
                "            } else {\n" +
                "              $(\"#login_options_heading_required\").hide();\n" +
                "              $(\"#advanced_verification_options1\").hide();\n" +
                "              $(\"#advanced_verification_options2\").hide();\n" +
                "              $(\"#advanced_verification_options3\").hide();\n" +
                "            }\n" +
                "          })\n" +
                "          .fail(function(error) {\n" +
                "            $('#login_options').replaceWith(\"<div id='login_options'>&nbsp;</div>\");\n" +
                "            $('#login_options_loading').replaceWith(\"<span id='login_options_loading'></span>\");\n" +
                "            $(\"#login_options_heading_required\").hide();\n" +
                "            $(\"#advanced_verification_options1\").hide();\n" +
                "            $(\"#advanced_verification_options2\").hide();\n" +
                "            $(\"#advanced_verification_options3\").hide();\n" +
                "          });\n" +
                "        }\n" +
                "      });\n" +
                "    });\n" +
                "\n" +
                "      window.onload = function () {\n" +
                "        setInterval(function() {\n" +
                "          if ($(\"#j_username\").val().length > 0 && passwordPopulated() && $(\"#disableAutoCheck\").val() == \"0\") {\n" +
                "            $(\"#disableAutoCheck\").val(\"1\");\n" +
                "            $(\"#j_username\").blur();\n" +
                "            $(\"#j_password\").focus();\n" +
                "          }\n" +
                "        }, 250);\n" +
                "      }\n" +
                "    </script>\n" +
                "</div></form>\n" +
                "     </div>\n" +
                "   </div>\n" +
                " </div>\n" +
                "</body>";

        Log.i("LogIn_html", summary);
        return summary;
    }
}
