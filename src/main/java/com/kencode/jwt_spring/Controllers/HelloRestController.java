package com.kencode.jwt_spring.Controllers;

import com.kencode.jwt_spring.Model.User;
import com.kencode.jwt_spring.Service.userService;
import com.kencode.jwt_spring.dtos.LoginDTO;
import com.kencode.jwt_spring.dtos.RegisterDTO;
import com.kencode.jwt_spring.dtos.Message;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import jakarta.servlet.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.Date;

@RestController
@RequestMapping("api")
public class HelloRestController{

    @Autowired
    private userService UserService;

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody RegisterDTO body) {

        User user = new User();
        user.setName(body.name);
        user.setEmail(body.email);
        user.setPassword(body.password);

        return ResponseEntity.ok(this.UserService.save(user));
    }

    @PostMapping("login")
    public ResponseEntity<Message> login(@RequestBody LoginDTO body, HttpServletResponse response){
        User user = this.UserService.findByEmail(body.email);
        if(user == null){
            return ResponseEntity.badRequest().body( new Message("user not found!"));
        }

        if (!user.comparePassword(body.password)) {
            return ResponseEntity.badRequest().body( new Message("invalid password!"));

        }

        String issuer = String.valueOf(user.getId());

        String jwt = Jwts.builder()
                .setIssuer(issuer)
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
                .signWith(SignatureAlgorithm.HS512, "secret").compact();

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        return ResponseEntity.ok( new Message("success"));
    }

    @GetMapping("user")
    public ResponseEntity<User> user(@CookieValue("jwt") String jwt) {
//        try {
//            if (jwt == null) {
//                return ResponseEntity.status(401).body( new Message("unauthenticated"));
//            }

            String body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).getBody().getIssuer();
            int body1 = Integer.parseInt(body);
            return ResponseEntity.ok(this.UserService.getById(body1));

//        } catch (Exception e) {
//            return ResponseEntity.status(401).body( new Message("unauthenticated"));
//        }
    }

    @PostMapping("logout")
    public  ResponseEntity<Message> logout(HttpServletResponse response )  {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok( new Message("success"));
    }




    @GetMapping("admin")
    public String helloAdmin() {
        return "Hello Admin";
    }
}
