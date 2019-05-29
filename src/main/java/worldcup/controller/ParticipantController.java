package worldcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import worldcup.service.ParticipantService;

@Controller
@RequestMapping("/participant")
public class ParticipantController {

    @Autowired private ParticipantService participantService;

    @GetMapping("/calculatePoints")
    public String calculatePoints(@RequestParam Long matchId){
        participantService.calculatePoints(matchId);
        return "OK";
    }


}