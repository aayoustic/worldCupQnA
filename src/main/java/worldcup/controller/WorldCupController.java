package worldcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import worldcup.service.DailyStatsDataService;

@Controller
@RequestMapping("/worldCup")
public class WorldCupController {

    @Autowired
    private DailyStatsDataService dailyStatsDataService;

    @ResponseBody
    @GetMapping("/addDailyStats")
    public String addDailyStats(@RequestParam String matchId){
        dailyStatsDataService.addDailyStats(matchId);
        return "OK";
    }

    @ResponseBody
    @GetMapping("/loadSquad")
    public String loadSquad(@RequestParam String matchId){
        dailyStatsDataService.loadSquads(matchId);
        return "OK";
    }
}
