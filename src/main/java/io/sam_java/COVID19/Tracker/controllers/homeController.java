package io.sam_java.COVID19.Tracker.controllers;

import io.sam_java.COVID19.Tracker.models.locationStats;
import io.sam_java.COVID19.Tracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class homeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        List<locationStats> allStats = coronaVirusDataService.getAllStatsList();
        int currentTotal = allStats.stream().mapToInt(stat -> stat.getLatest_total_cases()).sum();
        int diffTotal = allStats.stream().mapToInt(stat -> stat.getDiff_total_cases()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("currentTotal", currentTotal);
        model.addAttribute("diffTotal", diffTotal);
        return "home";
    }

}
