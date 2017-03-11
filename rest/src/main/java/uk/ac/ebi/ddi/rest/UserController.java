package uk.ac.ebi.ddi.rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController{

    @RequestMapping(value="/profile", method= RequestMethod.GET)
    @CrossOrigin()
    protected @ResponseBody String GetProfile(HttpSession session) throws Exception {
        return "{\"Name\":\"MyProfile\"}";
    }

    @RequestMapping(value="/logout", method= RequestMethod.POST)
    @CrossOrigin()
    protected
    String Logout(HttpSession session) throws Exception {

        session.invalidate();

        String url = "https://sandbox.orcid.org/oauth/signin?client_id=APP-FBTNY1E6990OKN73&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fsignin%2Forcid&scope=%2Fauthenticate&state=13725027-f513-4154-9774-d3e594f3206b";

        return "redirect:/#/";
    }
}