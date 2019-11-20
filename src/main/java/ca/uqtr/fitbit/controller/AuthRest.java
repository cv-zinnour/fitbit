package ca.uqtr.fitbit.controller;


import ca.uqtr.fitbit.api.IApi;
import ca.uqtr.fitbit.entity.Auth;
import ca.uqtr.fitbit.service.auth.AuthService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthRest {

    private static final String url = "https://api.fitbit.com/oauth2/token";


    AuthService authService;
    IApi api;

    @Autowired
    public AuthRest(AuthService authService, IApi api){
        this.authService = authService;
        this.api = api;
    }

    @CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
    @RequestMapping(value = "/post", method = RequestMethod.POST, consumes = {"application/json"})
    public void post(@RequestParam(value = "authorizationCode" ) String authorizationCode) throws IOException {
        System.out.println("post  =  "+authorizationCode);

        JSONObject jsonObj = new JSONObject(api.getAccessTokenRefreshToken(url, authorizationCode));
        //System.out.println("7777777777  "+Utils.getAccessTokenRefreshToken(url, authorizationCode));
        System.out.println(jsonObj.toString());
        authService.save(new Auth(authorizationCode,
                jsonObj.getString("access_token"),
                jsonObj.getString("refresh_token"),
                false,
                jsonObj.getInt("expires_in"),
                jsonObj.getString("scope"),
                jsonObj.getString("token_type")));
    }

    @CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public boolean isAuth()  {
        System.out.println("isauth");
        if (authService.count() == 0)
            return false;
        else
            return true;
    }

    @CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
    @RequestMapping(value = "/accessToken", method = RequestMethod.GET, produces = "application/json")
    public String getAccessToken() throws IOException {
        return JSONObject.quote(authService.getAccessToken());
    }

    @CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
    @RequestMapping(value = "/authorizationCode", method = RequestMethod.PUT, produces = "application/json")
    public void upadteAuthorizationCode(@RequestParam("authorizationCode") String authorizationCode)  {
        System.out.println("Update");
        authService.updateAuthorizationCode(authorizationCode);
    }

    @CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public int delete(@RequestParam String accessToken)  {
        System.out.println("delete  "+accessToken);
        return authService.deleteByAccessToken(accessToken);
    }


}
