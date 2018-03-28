package ru.atom.chat;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.xml.bind.SchemaOutputResolver;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Controller
@RequestMapping("chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private Queue<String> messages = new ConcurrentLinkedQueue<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name, @RequestParam("pass") String pass) throws IOException {

        boolean flag= false;// flag for check if user is in base

        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }
        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in:(");
        }
        //NEED TO CHECK IF NAME EXIST IN BASE 

        HashMap<String,String> login_password;
        FileInputStream fis;
        fis = new FileInputStream("login_password.ser");

        try(ObjectInputStream ois = new ObjectInputStream(fis)) {

            login_password = (HashMap<String, String>) ois.readObject();

            if (login_password.get(name).equals(pass)) flag = true;



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(flag) {
            usersOnline.put(name, name);
            messages.add("[" + name + "] logged in");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("wrong login or password");
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> chat() {
        return new ResponseEntity<>(messages.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n")),
                HttpStatus.OK);
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Object> online() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//TODO
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity logout(@RequestParam("name") String name) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//TODO
    }


    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity say(@RequestParam("name") String name, @RequestParam("msg") String msg) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//TODO
    }

    @RequestMapping(
            path = "register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity register(@RequestParam("login") String login, @RequestParam("passwd") String pass) throws IOException {
        HashMap<String, String> login_password = null;
        FileInputStream fis;
        boolean isNameNotIn = true;
        fis = new FileInputStream("login_password.ser");
        try(ObjectInputStream ois = new ObjectInputStream(fis)) {

            login_password = (HashMap<String, String>) ois.readObject();

            if (login_password.get(login) == null) {
                login_password.put(login, pass);

            }else{
                isNameNotIn = false;
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = new FileOutputStream("login_password.ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(login_password);

        }
        if (isNameNotIn) {
            return ResponseEntity.ok().build();//TODO
        }else{
            return ResponseEntity.badRequest().body("try again with new name");
        }

    }

}
